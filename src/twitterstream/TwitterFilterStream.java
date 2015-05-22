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
import javax.swing.JDialog;
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
 *
 * @author s139662
 */
public class TwitterFilterStream implements StatusListener {

    /** Generate a twitter stream object to connect to the streaming API. */
    TwitterStream twitterStream;

    /** Date formats for file naming and console output. */
    private final SimpleDateFormat console_format, file_name_format;

    /** List of keywords to search for. */
    private final ArrayList<String> keywords;

    /** File output column data delimeter. */
    private final String DATA_SEPERATOR = ";";

    /** A tweet arrival event sListener */
    private TweetListener listener;

    /** Start time and running time of twitter stream in milliseconds. */
    private long startTime, runTime;

    /** To check if the twitter stream is/should be stopped. */
    private boolean ENABLED;

    /** Writer objects to write the twitter stream data to file. */
    File tweets_file, users_file;
    PrintWriter tweets_writer, users_writer;
    
    /** MySQL object that can connect to a MySQL database and execute SQL queries. */
    private MySQL4j twitterDatabase;
    
    /** Language codes for keyword translation */
    private String[] translate_codes;

    public TwitterFilterStream() {
        twitterStream = new TwitterStreamFactory(getAuth()).getInstance();
        twitterStream.addListener(this);
        keywords = new ArrayList<>();
        console_format = new SimpleDateFormat("hh:mm:ss");
        file_name_format = new SimpleDateFormat("dd-MM-yy_HH-mm");
        ENABLED = false;
        twitterDatabase = null;
        translate_codes = null;
    }

    /** Gets configuration builder for authentication */
    private Configuration getAuth() {
        return getAuth("0ofktWKMWWZwv3lb2BGxeSsxz",
                "VuyOjrVy9aPdiQoFXOw7HCS0ZHeUShfl7Bs5UktqEGDpIDUnMj",
                "3063962920-rxltQ88TXf5BrvCiC2BBhm5A9ZiHgsMJOpmFTrl",
                "Y4WALWynF1hZyW9NDV21E7qtRVhGt1cvumj7jVpPpAe3I");
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
     * Set whether you want to write the output to a MySQL database or not.
     * User: root, Pass: 32ZfbSTRaFDqsrWw
     * User: s139662, Pass: rvH6X6a7rN9bJtUD
     */
    public void useDatabase() {
        useDatabase("s139662",  "rvH6X6a7rN9bJtUD", 
                "jdbc:mysql://surajiyer96.synology.me:3306/twitter_filter_stream");
    }
    
    /**
     * Set whether you want to write the output to a MySQL database or not.
     * User: root, Pass: 32ZfbSTRaFDqsrWw
     * User: s139662, Pass: rvH6X6a7rN9bJtUD
     * 
     * @param username
     * @param password
     * @param url
     */
    public void useDatabase(String username, String password, String url) {
        twitterDatabase = new MySQL4j(username, password, url);
    }
    
    /**
     * If the Twitter streaming API is running, it returns true otherwise false.
     * @return true if twitter stream is enabled.
     */
    public boolean isRunning() {
        return ENABLED;
    }

    /**
     * Start reading the twitter stream.
     */
    public void enable() {
        
        if(!ENABLED) {
            // Check if at least 1 keyword exists.
            if(keywords.isEmpty()) {
                JDialog.setDefaultLookAndFeelDecorated(true);
                JOptionPane.showMessageDialog(null, "Oops! You forgot to specify "
                        + "minimum 1 keyword to search for.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create new files to write the data to.
            startTime = System.currentTimeMillis();
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
                System.out.println(console_format.format(new Date( System.currentTimeMillis())) +
                                   " ERROR: Failed to create files to write data to.");
                tweets_writer.close();
                users_writer.close();
                return;
            }
            
            if(twitterDatabase != null) {
                try {
                    twitterDatabase.connect();
                } catch (Exception ex) {
                    System.out.println(console_format.format(new Date( System.currentTimeMillis())) +
                                   " ERROR: Failed to connect to the MySQL database.");
                    ENABLED = true;
                    disable();
                    return;
                }
            }

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
                                "Warning", JOptionPane.WARNING_MESSAGE);
                        ENABLED = true;
                        disable();
                        return;
                    }
                }
            }
            fq.track(keys);

            // Start the twitter stream.
            twitterStream.filter(fq);

            // Set twitter stream running status.
            listener.setRunningStatus(ENABLED = true);
            System.out.println(console_format.format(new Date(startTime))
                    + " INFO: Twitter Stream Started."); 
        }
    }

    /**
     * Stop reading the twitter stream.
     */
    public void disable() {
        
        if(ENABLED) {
            // Stop the twitter stream.
            twitterStream.shutdown();
            
            // close the file output streams.
            tweets_writer.close();
            users_writer.close();
            listener.loggingCompleted(tweets_file, users_file);
            
            if(twitterDatabase != null) {
                try {
                    twitterDatabase.close();
                } catch (SQLException ex) {
                    System.out.println(console_format.format(new Date( System.currentTimeMillis())) 
                            + " ERROR: Failed to terminate the connection to the MySQL database properly.");
                }
            }
            
            // Set twitter stream running status.
            listener.setRunningStatus(ENABLED = false);
            System.out.println(console_format.format(new Date( System.currentTimeMillis())) 
                    + " INFO: Twitter Stream Stopped.");
        }
    }

    /**
     * Set the tweet sListener for each generated tweets.
     * @param t object of a class that implements the TweetListener class.
     */
    public void setListener(TweetListener t) {
        this.listener = t;
    }

    @Override
    public void onStatus(Status status) {
        if (runTime != 0 && System.currentTimeMillis() >= startTime + runTime) {
            disable();
            return;
        }

        // Notify listeners of new coordinates
        if(status.getGeoLocation() != null) {
            GeoLocation geo = status.getGeoLocation();
            listener.newTweet(Double.toString(geo.getLatitude()),
                    Double.toString(geo.getLongitude()), status.getText());
        }
        
        TweetEntity te = new TweetEntity(DATA_SEPERATOR, status, null);
        
        if(twitterDatabase != null) {
            try {
                twitterDatabase.executeSQLQuery("INSERT tweets VALUES(" + te.getID() 
                        + "," + te.getRetweetID() + "," + te.getRetweetCount() + "," + te.getFavCount()
                        + ",'" + te.getText() + "'," + te.getTime() + ",'" + te.getCountry() + "','"
                        + te.getLanguage() + "'," + te.getUserID() + "," + (te.getKeywords() == null
                        ? "NULL" : "'"+te.getKeywords()+"'") + ")");
            } catch (SQLException ex) {
                System.out.println(console_format.format(new Date( System.currentTimeMillis())) 
                            + " ERROR: Failed to write the twitter data to the MySQL"
                            + " database properly.");
                disable();
                return;
            }
        }

        tweets_writer.write(te.toString());
        tweets_writer.println();
        users_writer.write(new UserEntity(DATA_SEPERATOR, status.getUser()).toString());
        users_writer.println();
        
        // Stop the twitter stream if any error(s) while writing to file.
        if(tweets_writer.checkError() || users_writer.checkError()) {
            System.out.println(console_format.format(new Date(System.currentTimeMillis())) +
                    " ERROR: Failed to write stream data to file.");
            disable();
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
        System.out.println(console_format.format(new Date( System.currentTimeMillis())) +
                           " ERROR: Connection to Twitter public stream failed.");
        
        disable();
    }
}
