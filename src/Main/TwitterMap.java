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
        stopTwitter();
    }

    @Override
    public void startTwitter() {
        tStream.enable();
        new Thread(tStream).start();
    }

    @Override
    public void stopTwitter() {
        tStream.disable();
    }
    
    @Override
    public void onBrowserLoadSuccess() {}

    @Override
    public void addKeyword(String s) {
        tStream.addKeyword(s);
    }

    @Override
    public void clearAllKeywords() {
        tStream.removeAllKeywords();
    }

    @Override
    public void setRunningTime(double t) {
        tStream.setRuntime(t);
    }

    @Override
    public void removeKeyword(String s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
