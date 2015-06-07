/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import utils.MySQL4j;

/**
 *
 * @author S.S.Iyer
 */
public interface GUIListener {
    
    /**
     * When browser fails to load Google maps correctly, it must shut down
     * the twitter stream.
     */
    public void onBrowserLoadFailed();
    
    /**
     * Start Twitter Stream.
     */
    public void startTwitterStream();
    
    /**
     * Stop Twitter Stream.
     */
    public void stopTwitterStream();
    
    /**
     * Add a new keyword to track from the twitter filter stream.
     * @param s keyword to track
     */
    public void addKeyword(String s);
    
    /**
     * Remove specific keyword.
     * @param s the keyword to remove (if it exists)
     */
    public void removeKeyword(String s);
    
    /**
     * Clear all keywords.
     */
    public void clearAllKeywords();
    
    /**
     * Set the running time of the twitter stream.
     * @param t the running time in milliseconds.
     */
    public void setRunningTime(long t);
    
    /**
     * Enable/Disable translation of keywords.
     * @param codes language coed for the languages in which the keywords must 
     * be translated to.
     */
    public void translate(String... codes);
    
    /**
     * Sets the MySQL database to use.
     * @param db the MySQL database object
     */
    public void setMySQLDatabase(MySQL4j db);
    
    /**
     * Load a new twitter stream instance with given twitter credentials.
     * @param CONSUMER_KEY
     * @param CONSUMER_SECRET
     * @param API_KEY
     * @param API_SECRET 
     */
    public void setTwitterCredentials(String CONSUMER_KEY, String CONSUMER_SECRET, 
            String API_KEY, String API_SECRET);
    
    public String[] getTwitterCredentials();
    
    public MySQL4j getMySQLDatabase();
    
    /**
     * Set whether to use a MySQL database.
     * @param use true if to be used, otherwise false.
     */
    public void useDatabase(boolean use);
    
    /**
     * Returns whether the twitter stream object has been specified or not.
     * @return true if it exists, otherwise false.
     */
    public boolean existsStream();
    
    /**
     * Load the main GUI.
     */
    public void loadMainFrame();
}
