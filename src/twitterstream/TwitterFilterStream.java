package twitterstream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.json.*;
import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.exceptions.OAuthConnectionException;
import org.scribe.model.*;
import org.scribe.oauth.*;
import static utils.translate.translate_all;

/**
 * 
 * @author s139662
 */
public class TwitterFilterStream implements Runnable {

    /** Twitter Stream URI link */
    private static final String STREAM_URI   = "https://stream.twitter.com/1.1/statuses/filter.json";
    /** Twitter API key */
    private static final String API_KEY      = "0ofktWKMWWZwv3lb2BGxeSsxz";
    /** Twitter API Secret */
    private static final String API_SECRET   = "VuyOjrVy9aPdiQoFXOw7HCS0ZHeUShfl7Bs5UktqEGDpIDUnMj";
    /** Twitter Token key */
    private static final String TOKEN_KEY    = "3063962920-rxltQ88TXf5BrvCiC2BBhm5A9ZiHgsMJOpmFTrl";
    /** Twitter Token Secret */
    private static final String TOKEN_SECRET = "Y4WALWynF1hZyW9NDV21E7qtRVhGt1cvumj7jVpPpAe3I";
    
    /** Output file name date format. */
    private final SimpleDateFormat sdf;
    
    /** List of keywords to search for. */
    private final ArrayList<String> keywords;
    
    /** Running time of twitter stream in milliseconds. */
    private double runtime;
    
    /** To check if the twitter stream is/should be stopped. */
    private boolean STOPPED = true;
    
    /** To check if twitter stream should log the input to file. */
    private boolean logging;
    
    /** A tweet arrival event listener */
    private TweetListener listener;

    public TwitterFilterStream() {
        this.logging = true;
        this.keywords = new ArrayList<>();
        this.sdf = new SimpleDateFormat("hh:mm:ss");
        this.runtime = 0d;
    }

    /**
     * Add keyword to search for in twitter stream.
     * @param keyword the keyword to search for.
     */
    public void addKeyword(String keyword) {
        keywords.add(keyword);
        for(String s : translate_all(keyword)) {
            keywords.add(s);
        }
    }
    
    /**
     * Remove existing keyword.
     */
    public void removeAllKeywords() {
        keywords.clear();
    }

    public void setRuntime(double ms) {
        runtime = ms;
    }

    /**
     * Set file logging on/off.
     * 
     * @param logging if true, it will sink the coordinates to a text file.
     */
    public void logTweets(boolean logging) {
        this.logging = logging;
    }
    
    /**
     * Set the tweet listener for each generated tweets.
     * @param t object of a class that implements the TweetListener class.
     */
    public void setListener(TweetListener t) {
        this.listener = t;
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

    @Override
    public void run() {
        
        if(keywords.isEmpty()) {
            disable();
            JDialog.setDefaultLookAndFeelDecorated(true);
            JOptionPane.showMessageDialog(null, "Oops! You forgot to specify "
                    + "minimum 1 keyword to search for.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if(STOPPED) return;
        
        // Enter your consumer key and secret below
        OAuthService service = new ServiceBuilder()
                .provider(TwitterApi.class)
                .apiKey(API_KEY)
                .apiSecret(API_SECRET)
                .build();

            // Set your access token
        Token accessToken = new Token(TOKEN_KEY, TOKEN_SECRET);

        // Generate a request
        System.out.println(sdf.format(new Date( System.currentTimeMillis())) + 
                " INFO: Connecting to Twitter public stream");
        OAuthRequest request = new OAuthRequest(Verb.POST, STREAM_URI);
        request.addHeader("version", "HTTP/1.1");
        request.addHeader("host", "stream.twitter.com");
        request.setConnectionKeepAlive(true);
        request.addHeader("user-agent", "Twitter Stream Reader");
        String keys = keywords.get(0);
        for(int i = 1; i < keywords.size(); i++) {
            keys += ","+keywords.get(i);
        }
        request.addBodyParameter("track", keys);
        service.signRequest(accessToken, request);
        Response response;
        try {
            response = request.send();
        } catch (OAuthConnectionException e) {
            System.out.println(sdf.format(new Date( System.currentTimeMillis())) + 
                    " ERROR: Connection to Twitter public stream failed.");
            disable();
            return;
        }
            
        System.out.println(sdf.format(new Date( System.currentTimeMillis())) +
                " INFO: Twitter Stream Started.");
        
        // Create a reader and a filewriterto read Twitter's stream
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getStream()));
        File output = null;
        Writer writer = null;
        
        try {

            // Used as offset for runtime, and unique file naming.
            long startTime = System.currentTimeMillis();

            // Create output file
            if (logging) {
                SimpleDateFormat f = new SimpleDateFormat("dd-MM-yy_HH-mm");
                output = new File(System.getenv("TMP")+"\\Tweets_Coordinates_"+f.format(new Date(startTime)) + ".txt");
                writer = new FileWriter(output);
            }

            String line;
            JSONObject obj;
            JSONArray coord;
            int maxNrFailures = 2;
            int readFailCount = 0;

            // While people are tweeting
            while ((line = reader.readLine()) != null) {
                
                /* Close writer and return after runtime ms or if stream is 
                   manually stopped. */
                if (STOPPED || (runtime != 0 && System.currentTimeMillis() >= startTime + runtime)) {
                    System.out.println(sdf.format(new Date( System.currentTimeMillis())) + 
                    " INFO: Twitter Stream Stopped.");
                    break;
                }
                                
                try {
                    // Convert the tweet-string to JSON
                    obj = new JSONObject(line);
                
                    if (!obj.isNull("geo")) {

                        // Create the array with the coordinates, used for the event and output
                        coord = obj.getJSONObject("geo").getJSONArray("coordinates");

                        // Notify listeners of new coordinates
                        //listener.newTweet(coord.toString(), obj.getString("text"));

                        // Print the coordinates to an output file
                        if (logging) {
                            writer.write(coord.get(0).toString() + "," + coord.get(1).toString() + "\r\n");
                            writer.flush();
                        }
                    }
                } catch (JSONException e) {
                    System.out.println(sdf.format(new Date( System.currentTimeMillis())) + 
                            " ERROR: Failed to read stream.");
                    if(++readFailCount >= maxNrFailures) {
                        JDialog.setDefaultLookAndFeelDecorated(true);
                        int choice = JOptionPane.showConfirmDialog(null, "Reading twitter "
                                + "stream has failed "+maxNrFailures+" time(s). "
                                + "Do you want to continue?", "Confirm",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (choice == JOptionPane.NO_OPTION) {
                            break;
                        } else if (choice == JOptionPane.YES_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                            readFailCount = 0;
                            choice = Integer.parseInt(JOptionPane.showInputDialog(null, 
                                    "Do you want increase the number of tries? (0 = infinite)", 
                                    "Question", JOptionPane.QUESTION_MESSAGE));
                            if(choice == 0) {
                                maxNrFailures = Integer.MAX_VALUE;
                            } else {
                               maxNrFailures = choice;
                            }
                        }
                        
                    }
                }
                
            }
        } catch (IOException e) {
            System.out.println(sdf.format(new Date( System.currentTimeMillis())) + 
                    " ERROR: Failed to create file or write stream to file.");
        } finally {
            if (logging) {
                try {
                    writer.close();
                    //listener.loggingCompleted(output);
                } catch (IOException ignore) {}
            }
            disable();
        }
        
    }
}
