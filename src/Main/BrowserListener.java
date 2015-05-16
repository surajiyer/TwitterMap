package Main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s139662
 */
public interface BrowserListener {
    
    /**
     * When browser fails to load Google maps correctly, it must shut down
     * the twitter stream.
     */
    public void onBrowserLoadFailed();
    
    /**
     * When browser loads Google maps correctly, it must start the twitter 
     * stream.
     */
    public void onBrowserLoadSuccess();
    
    /**
     * Start Twitter Stream.
     */
    public void startTwitter();
    
    /**
     * Stop Twitter Stream.
     */
    public void stopTwitter();
    
    /**
     * Add a new keyword to 
     * @param s 
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
    public void setRunningTime(double t);
}
