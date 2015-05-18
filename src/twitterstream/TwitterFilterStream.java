/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterstream;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import static utils.translate.translate_all;

/**
 *
 * @author s139662
 */
public class TwitterFilterStream implements StatusListener {

    // Generate a twitter stream object to connect to the streaming API.
    TwitterStream twitterStream;

    /** Date formats for file naming and console output. */
    private final SimpleDateFormat console_format, file_name_format;

    /** List of keywords to search for. */
    private final Map<String, List<String>> keywords;

    private final String DATA_SEPERATOR = ";&;";

    /** A tweet arrival event sListener */
    private TweetListener listener;

    /** Start time and running time of twitter stream in milliseconds. */
    private long startTime, runTime;

    /** To check if the twitter stream is/should be stopped. */
    private boolean ENABLED;

    /** Writer objects to write the twitter stream data to file. */
    File tweets_file, users_file;
    PrintWriter tweets_writer, users_writer;

    public TwitterFilterStream() {
        twitterStream = new TwitterStreamFactory(getAuth()).getInstance();
        twitterStream.addListener(this);
        keywords = new HashMap<>();
        console_format = new SimpleDateFormat("hh:mm:ss");
        file_name_format = new SimpleDateFormat("dd-MM-yy_HH-mm");
        ENABLED = false;
    }

    /** Gets configuration builder for authentication */
    private Configuration getAuth() {
        final ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("0ofktWKMWWZwv3lb2BGxeSsxz");
        cb.setOAuthConsumerSecret("VuyOjrVy9aPdiQoFXOw7HCS0ZHeUShfl7Bs5UktqEGDpIDUnMj");
        cb.setOAuthAccessToken("3063962920-rxltQ88TXf5BrvCiC2BBhm5A9ZiHgsMJOpmFTrl");
        cb.setOAuthAccessTokenSecret("Y4WALWynF1hZyW9NDV21E7qtRVhGt1cvumj7jVpPpAe3I");

        return cb.build();
    }

    /**
     * Add keyword to search for in twitter stream.
     * @param keyword the keyword to search for.
     * @param translate translate keywords to {Dutch, French, Chinese, Spanish, 
     * Hindi, Italian, Tamil, Russian, Arabic, German, Japanese, Korean, 
     * Lithuanian, Vietnamese} if true to expand search.
     */
    public void addKeyword(String keyword, boolean translate) {
        if(translate)
            try {
                keywords.put(keyword, Arrays.asList(translate_all(keyword)));
            } catch (Exception ex) {
                keywords.put(keyword, null);
                JDialog.setDefaultLookAndFeelDecorated(true);
                JOptionPane.showMessageDialog(null, "Could not translate the keyword.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        else {
            keywords.put(keyword, null);
        }
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
                tweets_writer.write(TweetEntity.CSV_FILE_HEADER);
                tweets_writer.println();
                users_writer = new PrintWriter(users_file);
                users_writer.write(UserEntity.CSV_FILE_HEADER);
                users_writer.println();
            } catch (IOException e) {
                System.out.println(console_format.format(new Date( System.currentTimeMillis())) +
                                   " ERROR: Failed to create files to write data to.");
                tweets_writer.close();
                users_writer.close();
                return;
            }

            // Create a new streaming filter query (i.e. keywords to track, locations etc.)
            FilterQuery fq = new FilterQuery();
            String[] keys = keywords.keySet().toArray(new String[0]);
            for(String key : keys) {
                try {
                    keys = ArrayUtils.addAll(keys, translate_all(key));
                } catch (Exception ex) {
                    Logger.getLogger(TwitterFilterStream.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            for(List<String> keyList : keywords.values()) {
                if(keyList == null) continue;
                keys = ArrayUtils.addAll(keys, keyList.toArray(new String[0]));
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

        tweets_writer.write(new TweetEntity(DATA_SEPERATOR, status, null).toString());
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
