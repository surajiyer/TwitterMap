/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import twitterstream.TwitterFilterStream;
import utils.MySQL4j;
import utils.NLP;

/**
 * Main class
 * 
 * @author S.S.Iyer
 */
public class TwitterMap implements GUIListener {
    
    private final TwitterFilterStream tStream;
    
    public TwitterMap() {
        
        // initializes the twitter stream.
        tStream = new TwitterFilterStream();
        
        // initializes the sentiment analysis pipeline.
        NLP.init();
    }
    
    public void init() {
        /* creates a login frame to input the twitter dev credentials prior to 
           loading the frame. */
        twitterLoginFrame tlf = new twitterLoginFrame(this);
        tlf.setVisible(true);
    }
    
    public void run() {
        // Initialize the main frame.
        final GUI gui = new GUI(this);
        
        // Set the twitter stream parameters
        tStream.setRuntime(0); // Runtime in ms; 0 = unlimited.
        tStream.setListener(gui);
        
        // Display the Main Frame GUI.
        gui.setVisible(true);
        
        // Add shut down hook to turn of the filter stream before exit.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            TwitterMap.this.tStream.disable();
        }));
    }

    public static void main(String[] args) {
        new TwitterMap().init();
    }

    @Override
    public void onBrowserLoadFailed() {
        tStream.disable();
    }

    @Override
    public void startTwitterStream() {
        tStream.enable();
    }

    @Override
    public void stopTwitterStream() {
        tStream.disable();
    }

    @Override
    public void addKeyword(String s) {
        tStream.addKeyword(s);
    }

    @Override
    public void clearAllKeywords() {
        tStream.removeAllKeywords();
    }

    @Override
    public void setRunningTime(long t) {
        tStream.setRuntime(t);
    }

    @Override
    public void removeKeyword(String s) {
        tStream.removeKeyword(s);
    }

    @Override
    public void translate(String... codes) {
        tStream.translate(codes);
    }

    @Override
    public void setMySQLDatabase(MySQL4j db) {
        tStream.setDatabase(db);
    }

    @Override
    public void setTwitterCredentials(String CONSUMER_KEY, String CONSUMER_SECRET, String API_KEY, String API_SECRET) {
        tStream.setTwitterCredentials(CONSUMER_KEY, CONSUMER_SECRET, API_KEY, API_SECRET);
    }

    @Override
    public MySQL4j getMySQLDatabase() {
        return tStream.getMySQLDatabase();
    }

    @Override
    public void useDatabase(boolean use) {
        tStream.useDatabase(use);
    }

    @Override
    public boolean existsStream() {
        return tStream.existsStream();
    }

    @Override
    public void loadMainFrame() {
        run();
    }

    @Override
    public String[] getTwitterCredentials() {
       return tStream.getTwitterCredentials();
    }
}
