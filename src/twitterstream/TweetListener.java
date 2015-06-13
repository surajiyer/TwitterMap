/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterstream;

import twitterstream.TwitterFilterStream.StreamStatus;

/**
 *
 * @author S.S.Iyer
 */
public interface TweetListener {
    
    /**
     * Do something when a new tweet arrives from the twitter stream.
     * 
     * @param lat the latitude of the coordinate.
     * @param lon the longitude of the coordinate.
     * @param title the message within the tweet.
     */
    public void newTweet(double lat, double lon, String title);
    
    /**
     * Set whether the filter stream is running or not.
     * @param s
     */
    public void setRunningStatus(StreamStatus s);
}
