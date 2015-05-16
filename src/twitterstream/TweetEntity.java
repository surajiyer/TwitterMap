package twitterstream;

import java.util.Objects;
import twitter4j.Place;
import twitter4j.Status;

/**
 *
 * @author s139662
 */
public class TweetEntity {
    
    private String dataSeperator = ";&;";
    private long id;
    private long retweetid;
    private int retweets;
    private int favourites;
    private String text;
    private long creationTime;
    private String countryCode;
    private String language;
    private long userID;
    private String keywords;
    
    public TweetEntity(String dataSeperator, Status status, String keywords) {
        this(dataSeperator, status, status.getPlace(), 
                status.getRetweetedStatus(), keywords);
    }
    
    private TweetEntity(String dataSeperator, Status status, Place place, 
            Status retweet, String keywords) {
        String countrycode = "";
        if (place != null) {
            countrycode = place.getCountryCode();
        }
        long retweetStatusID = -1;
        if (retweet != null) {
            retweetStatusID = retweet.getId();
        }
        String lang = "";
        if (status.getLang() != null) {
            lang = status.getLang();
        }
        this.dataSeperator = dataSeperator;
        this.id = status.getId();
        this.retweetid = retweetStatusID;
        this.retweets = status.getRetweetCount();
        this.favourites = status.getFavoriteCount();
        this.text = formatText(status.getText());
        this.creationTime = status.getCreatedAt().getTime();
        this.countryCode = countrycode;
        this.language = lang;
        this.userID = status.getUser().getId();
        this.keywords = keywords;
    }
    
    public TweetEntity(String dataSeperator, long id, long retweetid, int retweets, 
            int favourites, String text, long time, String country, 
            String language, long userId, String keywords) {
        this.dataSeperator = dataSeperator;
        this.id = id;
        this.retweetid = retweetid;
        this.retweets = retweets;
        this.favourites = favourites;
        this.text = formatText(text);
        this.creationTime = time;
        this.countryCode = country;
        this.language = language;
        this.userID = userId;
        this.keywords = keywords;
    }
    
    private String formatText(String text) {
        text = text.replace("\n", " ");
        text = text.replace("  ", " ");
        
        return text;
    }
    
    public final long getID() {
        return id;
    }
    
    public final long getRetweetID() {
        return retweetid;
    }
    
    public final int getRetweets() {
        return retweets;
    }
    
    public final int getFavourites() {
        return favourites;
    }
    
    public final String getText() {
        return text;
    }
    
    public final long getTime() {
        return creationTime;
    }
    
    public final String getCountry() {
        return countryCode;
    }
    
    public final String getLanguage() {
        return language;
    }
    
    public final long getUserID() {
        return userID;
    }
    
    public final String getKeywords() {
        return keywords;
    }
    
    @Override
    public String toString() {
        final String s = dataSeperator;
        return id + s + retweets + s + favourites + s + text + s + creationTime + s + 
                countryCode + s + userID + s + keywords;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TweetEntity)) {
            return false;
        }
        
        TweetEntity entity = (TweetEntity) obj;
        return this.hashCode() == entity.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 37 * hash + this.retweets;
        hash = 37 * hash + this.favourites;
        hash = 37 * hash + Objects.hashCode(this.text);
        hash = 37 * hash + (int) (this.creationTime ^ (this.creationTime >>> 32));
        hash = 37 * hash + Objects.hashCode(this.countryCode);
        hash = 37 * hash + (int) (this.userID ^ (this.userID >>> 32));
        hash = 37 * hash + Objects.hashCode(this.keywords);
        return hash;
    }
}