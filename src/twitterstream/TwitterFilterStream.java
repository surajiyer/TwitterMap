/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterstream;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.ArrayUtils;
import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import utils.GoogleTranslation;
import utils.MySQL4j;

/**
 * Runs the twitter filter stream to retrieve real-time tweets.
 * 
 * @author S.S.Iyer
 */
public class TwitterFilterStream implements StatusListener {

    /**
     * Status of the filter stream.
     */
    public enum StreamStatus {
        ENABLED, DISABLED, PROCESSING;
    }
    
    /** Generate a twitter stream object to connect to the streaming API. */
    TwitterStream twitterStream;

    /** Date formats for file naming and console output. */
    private final SimpleDateFormat file_name_format;

    /** List of keywords to search for. */
    private final ArrayList<String> keywords;

    /** File output column data delimeter. */
    private final String DATA_SEPERATOR = ";&;";

    /** A tweet arrival event sListener */
    private TweetListener listener;

    /** Start time and running time of twitter stream in milliseconds. */
    private long startTime, runTime;

    /** To check if the twitter stream is/should be stopped. */
    private StreamStatus status;

    /** Writer objects to write the twitter stream data to file. */
    private File tweets_file, users_file;
    private PrintWriter tweets_writer, users_writer;
    
    /** Logger to log twitter filter stream data. */
    private File logfile;
    private final Logger logger;
    private FileHandler fh;
    
    /** MySQL object that can connect to a MySQL database and execute SQL queries. */
    private MySQL4j twitterDatabase;
    private boolean useDatabase;
    
    /** Language codes for keyword translation */
    private String[] translate_codes;
    
    /** Count of number of tweets retrieved. */
    private long tweets_count;
    
    /** FIFO tweets buffer for processing tweets on concurrent thread. */
    private final BlockingQueue q;
    private final Object stop = -1;
    
    public TwitterFilterStream() {
        twitterStream = null;
        keywords = new ArrayList<>();
        file_name_format = new SimpleDateFormat("dd-MM-yy_HH-mm");
        status = StreamStatus.DISABLED;
        twitterDatabase = null;
        translate_codes = null;
        logfile = null;
        logger = Logger.getLogger("TwitterStreamLog");
        fh = null;
        useDatabase = false;
        q = new ArrayBlockingQueue(20);
    }
    
    /** Gets configuration builder for authentication */
    private Configuration getAuth(String CONSUMER_KEY, String CONSUMER_SECRET, 
            String API_KEY, String API_SECRET) {
        final ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(CONSUMER_KEY);
        cb.setOAuthConsumerSecret(CONSUMER_SECRET);
        cb.setOAuthAccessToken(API_KEY);
        cb.setOAuthAccessTokenSecret(API_SECRET);

        return cb.build();
    }

    /**
     * Add keyword to search for in twitter stream.
     * @param keyword the keyword to search for.
     */
    public void addKeyword(String keyword) {
        keywords.add(keyword);
    }

    /**
     * Remove individual keywords from set of keywords.
     * @param keyword the keyword to remove.
     */
    public void removeKeyword(String keyword) {
        keywords.remove(keyword);
    }

    /**
     * Remove all existing keywords.
     */
    public void removeAllKeywords() {
        keywords.clear();
    }

    /**
     * Set the running time of the twitter streaming API (in milliseconds)
     * @param ms the time in milliseconds
     */
    public void setRuntime(long ms) {
        runTime = ms;
    }
    
    /**
     * Enable/Disable keyword translation to expand search.
     * @param languages language codes of languages to which the keyword must be
     * translated to.
     */
    public void translate(String... languages) {
        this.translate_codes = languages;
    }
    
    /**
     * Load new twitter credentials.
     * 
     * @param p configuration property containing the necessary twitter developer
     * credentials.
     */
    public void setTwitterCredentials(Properties p) {
        twitterStream = new TwitterStreamFactory(getAuth(
                p.getProperty("CONSUMER_KEY"), 
                p.getProperty("CONSUMER_SECRET"), 
                p.getProperty("API_KEY"), 
                p.getProperty("API_SECRET"))).getInstance();
        twitterStream.addListener(this);
    }
    
    /**
     * Sets the MySQL database to which to write data to.
     * 
     * @param p configuration property containing the necessary MySQL database 
     * credentials.
     */
    public void setDatabase(Properties p) {
        setDatabase(new MySQL4j(
                p.getProperty("SQL_USERNAME"), 
                p.getProperty("SQL_PASSWORD"), 
                p.getProperty("SQL_URL")));
    }
    
    /**
     * Sets the MySQL database to which to write data to.
     * 
     * @param db 
     */
    public void setDatabase(MySQL4j db) {
        twitterDatabase = db;
    }
    
    /**
     * Getter method for the current specified MySQL database.
     * @return the MySQL database object.
     */
    public MySQL4j getMySQLDatabase() {
        return twitterDatabase;
    }
    
    /**
     * Sets whether to use the given database or not.
     * @param use true if to be used, otherwise false.
     */
    public void useDatabase(boolean use) {
        useDatabase = use;
    }
    
    /**
     * If the Twitter streaming API is running, it returns true otherwise false.
     * @return true if twitter stream is enabled.
     */
    public boolean isRunning() {
        return status != StreamStatus.DISABLED;
    }
    
    /**
     * Returns whether the twitter stream object has been specified or not.
     * @return true if it exists, otherwise false.
     */
    public boolean existsStream() {
        return twitterStream != null;
    }
    
    /**
     * Return the credentials used to activate the twitter stream.
     * @return a properties object containing the CONSUMER_KEY, CONSUMER_SECRET,
     * API_KEY and API_SECRET.
     */
    public Properties getTwitterCredentials() {
        Configuration c = twitterStream.getConfiguration();
        Properties twitterProps = new Properties();
        twitterProps.setProperty("CONSUMER_KEY", c.getOAuthConsumerKey());
        twitterProps.setProperty("CONSUMER_SECRET", c.getOAuthConsumerSecret());
        twitterProps.setProperty("API_KEY", c.getOAuthAccessToken());
        twitterProps.setProperty("API_SECRET", c.getOAuthAccessTokenSecret());
        return twitterProps;
    }
 
    /**
     * Start reading the twitter stream.
     */
    public void enable() {
        
        if(status == StreamStatus.DISABLED) {
            // Check if at least 1 keyword exists.
            if(keywords.isEmpty()) {
                JDialog.setDefaultLookAndFeelDecorated(true);
                JOptionPane.showMessageDialog(null, "Oops! You forgot to specify "
                        + "minimum 1 keyword to search for.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create new files to write the data to.
            startTime = System.currentTimeMillis();
            logfile = new File(System.getenv("TMP")+"\\"+logger.getName()+"_"+
                        file_name_format.format(new Date(startTime)) + ".log");
            try {
                fh = new FileHandler(logfile.getAbsolutePath());
            } catch (Exception ex) {
                JDialog.setDefaultLookAndFeelDecorated(true);
                JOptionPane.showMessageDialog(null, "Failed to build stream log. ", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            fh.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                  return new SimpleDateFormat("hh:mm:ss").format(new Date(
                          record.getMillis())) + " "
                          + record.getLevel() + ":  "
                          + record.getSourceClassName() + " -:- "
                          + record.getSourceMethodName() + " -:- "
                          + record.getMessage() + "\n";
                }
            });
            logger.addHandler(fh);
            tweets_file = new File(System.getenv("TMP")+"\\Tweets_"+
                    file_name_format.format(new Date(startTime)) + ".csv");
            users_file = new File(System.getenv("TMP")+"\\Users_"+
                    file_name_format.format(new Date(startTime)) + ".csv");

            try {
                tweets_writer = new PrintWriter(tweets_file);
                tweets_writer.write(TweetEntity.getCSVHeader(DATA_SEPERATOR));
                tweets_writer.println();
                users_writer = new PrintWriter(users_file);
                users_writer.write(UserEntity.getCSVHeader(DATA_SEPERATOR));
                users_writer.println();
            } catch (IOException e) {
                JDialog.setDefaultLookAndFeelDecorated(true);
                JOptionPane.showMessageDialog(null, "Failed to create files to "
                        + "write data to.", "Error", JOptionPane.ERROR_MESSAGE);
                tweets_writer.close();
                tweets_file.delete();
                users_writer.close();
                users_file.delete();
                logfile.delete();
                return;
            }
            
            // Connect to the MySQL database if there exists one.
            if(twitterDatabase != null && useDatabase) {
                try {
                    twitterDatabase.connect();
                } catch (Exception ex) {
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    JOptionPane.showMessageDialog(null, "Unable to connect to MySQL database.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    status = StreamStatus.ENABLED;
                    disable(true);
                    return;
                }
            }
            
            // Reset the tweets count.
            tweets_count = 0;

            // Create a new streaming filter query (i.e. keywords to track, locations etc.)
            FilterQuery fq = new FilterQuery();
            String[] keys = keywords.toArray(new String[0]);
            if(translate_codes != null && translate_codes.length > 0) {
                for(String key : keys) {
                    try {
                        keys = ArrayUtils.addAll(keys, GoogleTranslation.translate(key, translate_codes));
                    } catch (Exception ex) {
                        JDialog.setDefaultLookAndFeelDecorated(true);
                        JOptionPane.showMessageDialog(null, "Could not translate keyword '"+key+"'.", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                        status = StreamStatus.ENABLED;
                        disable(true);
                        return;
                    }
                }
                fq.language(translate_codes);
            } else {
                fq.language(new String[]{"en"});
            }
            fq.track(keys);

            // Start the twitter stream.
            twitterStream.filter(fq);

            // Set twitter stream running status.
            listener.setRunningStatus(status = StreamStatus.ENABLED);
            logger.info("Twitter Stream Started");
            
            // Start processing tweets on a seperate thread.
            Thread t = new Thread(processTweets);
            t.setDaemon(true);
            t.start();
        }
    }
    
    /**
     * Stop reading the twitter stream.
     * @param error if an error occurred or not.
     */
    public void disable(boolean error) {
        
        if(status == StreamStatus.ENABLED || error) {
            // Stop the twitter stream.
            twitterStream.shutdown();
            
            // write the number of retrieved tweets to log file.
            logger.log(Level.INFO, new StringBuilder("Retrieved ").append(tweets_count).append(" tweet(s)").toString());
            
            // Set twitter stream running status.
            logger.info("Twitter Stream Stopped.");
            
            // Set the twitter status to processing.
            if(!error) {
                listener.setRunningStatus(status = StreamStatus.PROCESSING);
                logger.info("Processing remaining tweets now.");
                try {
                    q.put(stop);
                } catch (InterruptedException ex) {}
                return;
            }
        }
        
        if(status == StreamStatus.PROCESSING || error) {
            // close the MySQL database connection.
            if(twitterDatabase != null && useDatabase) {
                try {
                    twitterDatabase.close();
                } catch (SQLException ex) {
                    logger.severe("Failed to terminate the connection to the MySQL database properly.");
                }
            }
            
            // Set twitter stream running status.
            listener.setRunningStatus(status = StreamStatus.DISABLED);
            logger.info("All tweets processed. Shutting down.");
            
            // close the file output streams.
            tweets_writer.close();
            users_writer.close();
            fh.close();
                    
            // When logging has completed, save the file to user-defined location.
            JFileChooser fc = new JFileChooser();
            boolean confirmed;
            do{
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int retValue = fc.showSaveDialog(null);
                if(retValue == JFileChooser.APPROVE_OPTION) {
                    tweets_file.renameTo(new File(fc.getSelectedFile()+"\\"+tweets_file.getName()));
                    users_file.renameTo(new File(fc.getSelectedFile()+"\\"+users_file.getName()));
                    logfile.renameTo(new File(fc.getSelectedFile()+"\\"+logfile.getName()));
                    confirmed = true;
                } else {
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    int choice = JOptionPane.showConfirmDialog(null, "Are you sure you "
                            + "don't want to save the file?", "Confirm", 
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    confirmed = choice == JOptionPane.YES_OPTION;
                    if(confirmed) {
                        tweets_file.delete();
                        users_file.delete();
                        logfile.delete();
                    }
                }
            }while(!confirmed);
        }
        
        // if there was some error, stop the concurrent processing thread.
        if(error) {
            try {
                q.put(stop);
            } catch (InterruptedException ex) {}
        }
    }

    /**
     * Set the tweet sListener for each generated tweets.
     * @param t object of a class that implements the TweetListener class.
     */
    public void setListener(TweetListener t) {
        this.listener = t;
    }
    
    /**
     * Given a tweet, get the first keyword it is related to.
     * @param tweet the status text
     * @return related keyword or null if no related keyword found
     */
    public String getRelatedKeyword(String tweet) {
        tweet = tweet.toLowerCase();
        String related = "";
        for(String key : keywords) {
            if(tweet.contains(key.toLowerCase())) {
                related += "," + key;
            }
        }
        if(related.equals(""))
            return null;
        return related.substring(1);
    }

    @Override
    public void onStatus(Status status) {
        if (runTime != 0 && System.currentTimeMillis() >= startTime + runTime) {
            disable(false);
            return;
        }
        
        tweets_count++;
        TweetEntity te = new TweetEntity(status, getRelatedKeyword(status.getText()));
        UserEntity ue = new UserEntity(status.getUser());
        try {
            q.put(new Object[]{te, ue});
        } catch (InterruptedException ex) {
            logger.severe("Adding tweet to processing queue was interrupted.");
        }
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice sdn) {}

    @Override
    public void onTrackLimitationNotice(int i) {}

    @Override
    public void onScrubGeo(long l, long l1) {}

    @Override
    public void onStallWarning(StallWarning sw) {}

    @Override
    public void onException(Exception excptn) {
        excptn.printStackTrace();
        logger.severe("Connection to Twitter public stream failed.");
        disable(true);
    }
    
    /** Runnable to process tweets on separate thread. */
    Runnable processTweets = new Runnable() {

        @Override
        public void run() {
            
            TweetEntity te = null;
            UserEntity ue = null;
            GeoLocation geo;
            String sql = "NULL";
            
            while(true) {
                try {
                    Object o = q.take();
                    if(o == stop)
                        break;
                    Object[] entities = (Object[]) o;
                    te = (TweetEntity) entities[0];
                    ue = (UserEntity) entities[1];
                } catch (InterruptedException ex) {
                    logger.severe("Concurrent processing of tweet interrupted.");
                }
                
                // Notify listeners of new coordinates
                if(( geo = te.getGeoLocation()) != null) {
                    listener.newTweet(geo.getLatitude(), geo.getLongitude(), 
                            TweetEntity.getLinkedText(te.getText()));
                }

                if(twitterDatabase != null && useDatabase) {
                    try {
                        sql = te.getSQLInsertQuery();
                        twitterDatabase.executeSQLQuery(te.getSQLInsertQuery());
                        sql = ue.getSQLInsertQuery();
                        twitterDatabase.executeSQLQuery(ue.getSQLInsertQuery());
                    } catch (SQLException ex) {
                        // code 1062 corresponds to insertion of tuple with duplicate primary key.
                        // code 0 corresponds to no connection.
                        if(ex.getErrorCode() != 1062) {
                            logger.log(Level.SEVERE, new StringBuilder("Error Query: ").append(sql).toString());
                            logger.severe("Failed to write the twitter data to the MySQL database properly.");
                            disable(true);
                            return;
                        }
                    }
                }

                // Write tweet data to CSV file.
                tweets_writer.write(te.toString());
                tweets_writer.println();
                users_writer.write(ue.toString());
                users_writer.println();

                // Stop the twitter stream if any error(s) while writing to file.
                if(tweets_writer.checkError() || users_writer.checkError()) {
                    logger.severe("Failed to write stream data to file.");
                    disable(true);
                }
            }
            
            disable(false);
        }
    };
}
