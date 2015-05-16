/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterstream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.ArrayUtils;
import twitter4j.FilterQuery;
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
public class TwitterSearch {
    
    /** Output file name date format. */
    private final SimpleDateFormat sdf;
    
    /** List of keywords to search for. */
    private final Map<String, ArrayList<String>> keywords;
    
    /** A tweet arrival event sListener */
    private TweetListener listener;
    
    /** Running time of twitter stream in milliseconds. */
    private double runtime;
    
    /** To check if the twitter stream is/should be stopped. */
    private boolean STOPPED = true;
    
    public TwitterSearch() {
        this.keywords = new HashMap<>();
        this.sdf = new SimpleDateFormat("hh:mm:ss");
        this.runtime = 0d;
    }
    
    /** Gets configuration builder for authentication */
    private Configuration getAuth() {
        /** Twitter API key */
        final String CONSUMER_KEY    = "0ofktWKMWWZwv3lb2BGxeSsxz";
        /** Twitter API Secret */
        final String CONSUMER_SECRET = "VuyOjrVy9aPdiQoFXOw7HCS0ZHeUShfl7Bs5UktqEGDpIDUnMj";
        /** Twitter Token key */
        final String TOKEN_KEY       = "3063962920-rxltQ88TXf5BrvCiC2BBhm5A9ZiHgsMJOpmFTrl";
        /** Twitter Token Secret */
        final String TOKEN_SECRET    = "Y4WALWynF1hZyW9NDV21E7qtRVhGt1cvumj7jVpPpAe3I";
        
        final ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(CONSUMER_KEY);
        cb.setOAuthConsumerSecret(CONSUMER_SECRET);
        cb.setOAuthAccessToken(TOKEN_KEY);
        cb.setOAuthAccessTokenSecret(TOKEN_SECRET);
        
        return cb.build();
    }
    
    /**
     * Add keyword to search for in twitter stream.
     * @param keyword the keyword to search for.
     */
    public void addKeyword(String keyword) {
        keywords.put(keyword, (ArrayList<String>) Arrays.asList(translate_all(keyword)));
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
     * Start reading the twitter stream.
     */
    public void enable() {
        STOPPED = false;
        listener.setRunningStatus(STOPPED);
    }
    
    /**
     * Stop reading the twitter stream.
     */
    public void disable() {
        STOPPED = true;
        listener.setRunningStatus(STOPPED);
        System.out.println(sdf.format(new Date( System.currentTimeMillis())) + 
                    " INFO: Twitter Stream Stopped.");
    }
    
    /**
     * Set the tweet sListener for each generated tweets.
     * @param t object of a class that implements the TweetListener class.
     */
    public void setListener(TweetListener t) {
        this.listener = t;
    }
    
    public void start() {
        if(keywords.isEmpty()) {
            disable();
            JDialog.setDefaultLookAndFeelDecorated(true);
            JOptionPane.showMessageDialog(null, "Oops! You forgot to specify "
                    + "minimum 1 keyword to search for.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        TwitterStream twitterStream = new TwitterStreamFactory(getAuth()).getInstance();
        // Create a reader and a filewriterto read Twitter's stream
        File output = null;
        Writer writer = null;
        // Used as offset for runtime, and unique file naming.
        long startTime = System.currentTimeMillis();
        
        // Create output file
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yy_HH-mm");
        output = new File(System.getenv("TMP")+"\\Tweets_Coordinates_"+f.format(new Date(startTime)) + ".txt");
        try {
            writer = new FileWriter(output);
        } catch (IOException e) {
            System.out.println(sdf.format(new Date( System.currentTimeMillis())) + 
                    " ERROR: Failed to create file or write stream to file.");
            try {
                writer.close();
                listener.loggingCompleted(output);
            } catch (IOException ignore) {}
            disable();
            return;
        }
        
        StatusListener sListener = new StatusListener() {

            @Override
            public void onStatus(Status status) {
                if (STOPPED || (runtime != 0 && System.currentTimeMillis() >= startTime + runtime)) {
                    System.out.println(sdf.format(new Date( System.currentTimeMillis())) + " INFO: Twitter Stream Stopped.");
                    twitterStream.shutdown();
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice sdn) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onTrackLimitationNotice(int i) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onScrubGeo(long l, long l1) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onStallWarning(StallWarning sw) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onException(Exception excptn) {
                System.out.println(sdf.format(new Date( System.currentTimeMillis())) + 
                        " ERROR: Connection to Twitter public stream failed.");
                disable();
            }
        };
        twitterStream.addListener(sListener);
        FilterQuery fq = new FilterQuery();
        String[] keys = keywords.keySet().toArray(new String[0]);
        for(ArrayList<String> keyList : keywords.values()) {
            keys = ArrayUtils.addAll(keys, keyList.toArray(new String[0]));
        }
        fq.track(keys);
        twitterStream.filter(fq);
    }
}
