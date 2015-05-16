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
    
    /**
     * When logging has completed, save the file to user-defined location.
     * @param tweets_file the file that stores the tweets.
     * @param users_file the file that stores the users who tweeted.
     */
    public void loggingCompleted(File tweets_file, File users_file);
}
