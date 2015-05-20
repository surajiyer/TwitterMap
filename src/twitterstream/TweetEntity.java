package twitterstream;

import java.util.Objects;
import twitter4j.Place;
import twitter4j.Status;

/**
 *
 * @author s139662
 */
public class TweetEntity {
    
    private String dataSeperator = ";";
    private long id;
    private long retweet_id;
    private int retweet_count;
    private int fav_count;
    private String text;
    private long creation_time;
    private String country_code;
    private String language_code;
    private long user_id;
    private String keywords;
    
    public TweetEntity(String dataSeperator, Status status, String keywords) {
        this(dataSeperator, status, status.getPlace(), status.getRetweetedStatus(), keywords);
    }
    
    private TweetEntity(String dataSeperator, Status status, Place place, 
            Status retweet, String keywords) {
        this.dataSeperator = dataSeperator;
        this.id = status.getId();
        this.retweet_id = retweet == null ? -1 : retweet.getId();
        this.retweet_count = status.getRetweetCount();
        this.fav_count = status.getFavoriteCount();
        this.text = formatText(status.getText());
        this.creation_time = status.getCreatedAt().getTime();
        this.country_code = place == null ? "und" : place.getCountryCode();
        this.language_code = status.getLang() == null ? "und" : status.getLang();
        this.user_id = status.getUser().getId();
        this.keywords = keywords == null ? "NULL" : keywords;
    }
    
    private String formatText(String text) {
        text = text.replace("\n", " ");
        text = text.replace("  ", " ");
        text = text.replace("'", "\'");
        
        return text;
    }
    
    public final long getID() {
        return id;
    }
    
    public final long getRetweetID() {
        return retweet_id;
    }
    
    public final int getRetweetCount() {
        return retweet_count;
    }
    
    public final int getFavCount() {
        return fav_count;
    }
    
    public final String getText() {
        return text;
    }
    
    public final long getTime() {
        return creation_time;
    }
    
    public final String getCountry() {
        return country_code;
    }
    
    public final String getLanguage() {
        return language_code;
    }
    
    public final long getUserID() {
        return user_id;
    }
    
    public final String getKeywords() {
        return keywords;
    }
    
    public static final String getCSVHeader(String dataSeperator) {
        final String s = dataSeperator;
        return "ID"+s+"Retweet ID"+s+"Retweet Count"+s+"Favorite Count"+s+"Text"
                +s+"Creation time"+s+"Country Code"+s+"Language Code"+s+"User ID"
                +s+"Keywords";
    }
    
    @Override
    public String toString() {
        final String s = dataSeperator;
        return id + s + retweet_id + s + retweet_count + s + fav_count + s + text + s
                + creation_time + s + language_code + s + country_code + s + user_id + s 
                + keywords;
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
        hash = 37 * hash + this.retweet_count;
        hash = 37 * hash + this.fav_count;
        hash = 37 * hash + Objects.hashCode(this.text);
        hash = 37 * hash + (int) (this.creation_time ^ (this.creation_time >>> 32));
        hash = 37 * hash + Objects.hashCode(this.country_code);
        hash = 37 * hash + (int) (this.user_id ^ (this.user_id >>> 32));
        hash = 37 * hash + Objects.hashCode(this.keywords);
        return hash;
    }
}