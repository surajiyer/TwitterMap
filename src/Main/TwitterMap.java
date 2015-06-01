package Main;

import twitterstream.TwitterFilterStream;
import utils.MySQL4j;

/**
 *
 * @author s121735
 */
public class TwitterMap implements GUIListener {
    
    private final GUI gui;
    private final TwitterFilterStream tStream;
    
    public TwitterMap() {        
        // initialize the twitter stream.
        tStream = new TwitterFilterStream();
        // initialize the GUI frame
        gui = new GUI(this);
    }
    
    public void run() {
        // Set the twitter stream parameters
        tStream.setRuntime(0); // Runtime in ms; 0 = unlimited.
        tStream.useDatabase();
        tStream.setListener(gui);
        
        // Show the GUI
        gui.setVisible(true);
    }

    public static void main(String[] args) {
        new TwitterMap().run();
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
        tStream.useDatabase(db);
    }

    @Override
    public void setTwitterCredentials(String CONSUMER_KEY, String CONSUMER_SECRET, String API_KEY, String API_SECRET) {
        tStream.setTwitterCredentials(CONSUMER_KEY, CONSUMER_SECRET, API_KEY, API_SECRET);
    }
    
}
