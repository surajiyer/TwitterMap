/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitterstream.TwitterFilterStream;
import utils.NLP;

/**
 * Main class
 * 
 * @author S.S.Iyer
 */
public class TwitterMap implements GUIListener {
    
    private final TwitterFilterStream tStream;
    private final String WORKING_DIR;
    
    public TwitterMap() {
        // Set the working directory (where the JAR file is running from).
        WORKING_DIR = new File(TwitterMap.class.getProtectionDomain().
                getCodeSource().getLocation().getPath()).getParent()+File.separator;
        
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
            TwitterMap.this.tStream.disable(false);
        }));
    }

    public static void main(String[] args) {
        new TwitterMap().init();
    }

    @Override
    public void onBrowserLoadFailed() {
        tStream.disable(true);
    }

    @Override
    public void startTwitterStream() {
        tStream.enable();
    }

    @Override
    public void stopTwitterStream() {
        tStream.disable(false);
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
    public void setMySQL(Properties p) {
        tStream.setDatabase(p);
        updateProperties(p);
    }

    @Override
    public void setTwitterCredentials(Properties p) {
        tStream.setTwitterCredentials(p);
        updateProperties(p);
    }

    @Override
    public Properties getProperties() throws FileNotFoundException, IOException {
        FileInputStream fis;
        fis = new FileInputStream(WORKING_DIR+"settings.ini");
        Properties p = new Properties();
        p.load(fis);
        return p;
    }

    @Override
    public void useDatabase(boolean use) {
        tStream.useDatabase(use);
        Properties p = new Properties();
        p.setProperty("USE_MYSQL", Boolean.toString(use));
        updateProperties(p);
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
    public void updateProperties(Properties p) {
        Properties p1 = null;
        FileOutputStream fos = null;
        try {
            p1 = getProperties();
        } catch (FileNotFoundException ex) {
            try {
                fos = new FileOutputStream(new File(WORKING_DIR+"settings.ini"));
            } catch (FileNotFoundException ex1) {}
        } catch (IOException ex) {}
        if(p1 == null)
            p1 = new Properties();
        p1.putAll(p);
        if(fos == null) {
            try {
                fos = new FileOutputStream(WORKING_DIR+"settings.ini");
            } catch (FileNotFoundException ex) {}
        }
        if(fos != null) {
            try {
                p1.store(fos, "preferences");
                fos.close();
            } catch (IOException ex) {}
        }
    }
}
