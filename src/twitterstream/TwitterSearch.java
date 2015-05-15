/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterstream;

import java.util.ArrayList;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author s139662
 */
public class TwitterSearch {
    /** Twitter API key */
    private static final String CONSUMER_KEY      = "0ofktWKMWWZwv3lb2BGxeSsxz";
    /** Twitter API Secret */
    private static final String CONSUMER_SECRET   = "VuyOjrVy9aPdiQoFXOw7HCS0ZHeUShfl7Bs5UktqEGDpIDUnMj";
    /** Twitter Token key */
    private static final String TOKEN_KEY    = "3063962920-rxltQ88TXf5BrvCiC2BBhm5A9ZiHgsMJOpmFTrl";
    /** Twitter Token Secret */
    private static final String TOKEN_SECRET = "Y4WALWynF1hZyW9NDV21E7qtRVhGt1cvumj7jVpPpAe3I";
    
    /** List of keywords to search for. */
    private final ArrayList<String> keywords;
    
    /** Gets configuration builder for authentication */
    private Configuration getAuth() {
        final ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(CONSUMER_KEY);
        cb.setOAuthConsumerSecret(CONSUMER_SECRET);
        cb.setOAuthAccessToken(TOKEN_KEY);
        cb.setOAuthAccessTokenSecret(TOKEN_SECRET);
        
        return cb.build();
    }
    
    public void loadKeywords() {
        
    }
    
    public void start() {
        TwitterStream twitterStream = new TwitterStreamFactory(getAuth()).getInstance();
        FilterQuery fq = new FilterQuery();
        fq.track();
        twitterStream.filter();
    }
}
