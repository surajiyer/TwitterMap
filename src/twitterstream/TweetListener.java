/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterstream;

import java.io.File;

/**
 *
 * @author s139662
 */
public interface TweetListener {
    
    /**
     * Do something when a new tweet arrives from the twitter stream.
     * 
     * @param coordinates the location from where the tweet was generated.
     * @param title the message within the tweet.
     */
    public void newTweet(String coordinates, String title);
    
    /**
     * If it cannot establish a connection to the twitter stream.
     */
    //public void responseFailure();
    
    /**
     * 
     * @param isRunning 
     */
    public void setRunningStatus(boolean isRunning);
    
    /**
     * When logging has completed, save the file to user-defined location.
     * @param f the file which has to saved.
     */
    public void loggingCompleted(File f);
}
