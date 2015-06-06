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
    private String geoLocation;
    private String language_code;
    private long user_id;
    private String keywords;
    private int sentiment;
    
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
        this.text = cleanText(status.getText());
        this.creation_time = status.getCreatedAt().getTime();
        this.country_code = place == null ? "und" : place.getCountryCode();
        this.language_code = status.getLang() == null ? "und" : status.getLang();
        this.user_id = status.getUser().getId();
        this.keywords = keywords;
        this.sentiment = analyseSentiment(text, keywords);
    }
    
    /**
     * Cleans the text. Replaces all new lines, gaps and question marks with
     * a space to make the text more cleaner for text mining.
     * 
     * @param text the tweet text
     * @return the cleaned text
     */
    private String cleanText(String text) {
        text = text.replace("?", " ");
        text = text.replace("\n", " ");
        text = text.replace("\t", " ");
        return text;
    }
    
    /**
     * Used to escape single and double quotes to add them properly to the 
     * MySQL database.
     * 
     * @param text the tweet text
     * @return the escaped text
     */
    public String escapeText(String text) {
        text = text.replace("\'", "\\\'");
        text = text.replace("\"", "\\\"");
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
    
    /**
     * Returns the text (the actual message) of the tweet.
     * @param escape true if all special characters must be escaped in the output
     * for cleaner printing/writing.
     * @return the tweet status text
     */
    public final String getText(boolean escape) {
        if(escape)
            return escapeText(text);
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
    
    public final int getSentiment() {
        return sentiment;
    }
    
    public static final String getCSVHeader(String dataSeperator) {
        final String s = dataSeperator;
        return "ID"+s+"Retweet ID"+s+"User ID"+s+"Text"+s+"Favorite Count"+s+
                "Retweet Count"+s+"Creation time"+s+"Country Code"+s+"Geolocation"
                +s+"Language Code"+s+"Keywords"+s+"Sentiment";
    }
    
    @Override
    public String toString() {
        final String s = dataSeperator;
        return id + s + retweet_id + s + user_id + s + text + s + fav_count + s 
                + retweet_count + s + creation_time + s + country_code + s 
                + geoLocation + s + language_code + s + keywords + s + sentiment;
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

    /**
     * Analyse the sentiment of tweet text based on given keyword.
     * @param text the tweet text
     * @return a score ranging from 0 (Very bad) to 4 (Very good).
     */
    private int analyseSentiment(String text, String keyword) {
        if(text == null)
            throw new IllegalArgumentException("Tweet text cannot be null.");
        if(keyword == null)
            throw new IllegalArgumentException("Keyword cannot be null.");
        return 0;
    }
}