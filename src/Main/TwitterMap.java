package Main;

import twitterstream.TwitterFilterStream;

/**
 *
 * @author s121735
 */
public class TwitterMap implements BrowserListener {
    
    private final GUI gui;
    private final TwitterFilterStream tStream;
    
    public TwitterMap() {
        // initialize the GUI frame
        gui = new GUI(this);
        // initialize the twitter stream.
        tStream = new TwitterFilterStream();
    }
    
    public void run() {
        // Set the twitter stream parameters
        tStream.setRuntime(0); // Runtime in ms; 0 = unlimited.
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
    public void addKeyword(String s, boolean translate) {
        tStream.addKeyword(s, translate);
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
    
}
