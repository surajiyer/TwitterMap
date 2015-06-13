/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

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
     * @param db the MySQL database credentials configuration properties.
     */
    public void setMySQL(Properties db);
    
    /**
     * Load a new twitter stream instance with given twitter credentials.
     * @param p Properties containing the necessary twitter dev credentials.
     */
    public void setTwitterCredentials(Properties p);
    
    /**
     * Get current settings properties.
     * @return MySQL object containing the database credentials.
     * @throws java.io.FileNotFoundException if settings file not found.
     */
    public Properties getProperties() throws FileNotFoundException, IOException;
    
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
    
    /**
     * Update existing properties with given set of properties (@code p}
     * @param p properties to be updated.
     */
    public void updateProperties(Properties p);
}
