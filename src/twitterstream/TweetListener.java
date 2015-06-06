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
     * @param lat the latitude of the coordinate.
     * @param lon the longitude of the coordinate.
     * @param title the message within the tweet.
     */
    public void newTweet(String lat, String lon, String title);
    
    /**
     * If it cannot establish a connection to the twitter stream.
     */
    //public void responseFailure();
    
    /**
     * 
     * @param isRunning 
     */
    public void setRunningStatus(boolean isRunning);
}
