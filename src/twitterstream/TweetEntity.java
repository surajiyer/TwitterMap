package twitterstream;

import java.util.List;
import java.util.Objects;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.Status;
import twitterstream.parser.Autolink;
import twitterstream.parser.Extractor;
import utils.NLP;

/**
 * Twitter tweets
 * 
 * @author S.S.Iyer
 */
public class TweetEntity {
    
    private static final String DATA_SEPERATOR = ";";
    private long id;
    private long retweet_id;
    private int retweet_count;
    private int fav_count;
    private String text;
    private long creation_time;
    private String country_code;
    private GeoLocation geoLocation;
    private String language_code;
    private long user_id;
    private String keywords;
    private int sentiment;
    
    public TweetEntity(Status status, String keywords) {
        this(status, status.getPlace(), status.getGeoLocation(), status.getRetweetedStatus(), keywords);
    }
    
    private TweetEntity(Status status, Place place, GeoLocation geo, Status retweet, String keywords) {
        this.id = status.getId();
        this.retweet_id = retweet == null ? -1 : retweet.getId();
        this.user_id = status.getUser().getId();
        this.text = cleanText(status.getText());
        this.fav_count = status.getFavoriteCount();
        this.retweet_count = status.getRetweetCount();
        this.creation_time = status.getCreatedAt().getTime();
        this.country_code = place == null ? "und" : place.getCountryCode();
        this.geoLocation = geo;
        this.language_code = status.getLang() == null ? "und" : status.getLang();
        this.keywords = keywords;
        this.sentiment = analyseSentiment(text);
    }
    
    /**
     * Cleans the text. Replaces all new lines, gaps and question marks with
     * a space to make the text more cleaner for text mining.
     * 
     * @param text the tweet text
     * @return the cleaned text
     */
    private String cleanText(String text) {
        return text.replaceAll("\\P{Print}", "")
                .replace("\n", " ")
                .replaceAll(" +", " ")
                .trim();
    }
    
    public final long getID() {
        return id;
    }
    
    public final long getRetweetID() {
        return retweet_id;
    }
    
    public final long getUserID() {
        return user_id;
    }
    
    /**
     * Returns the text (the actual message) of the tweet.
     * @return the tweet status text
     */
    public final String getText() {
        return text;
    }
    
    /**
     * Autolinks the URL links, hastags, mentions and cashtags in tweet texts.
     * @param text
     * @return the autolinked tweet text.
     */
    public final static String getLinkedText(String text) {
        text = new Autolink().autoLink(text);
        return text;
    }
    
    public final static String getFormattedText(String text) {
        Extractor extractor =  new Extractor();
        List<String> urls = extractor.extractURLs(text);
        for(String url : urls) {
            System.out.println(url);
            text = text.replace(url, "");
        }
        text = text.replaceAll(" +", " ").trim();
        return text;
    }
    
    public static void main(String[] args) {
        String text = "Amnesty pleit voor Europese opvang honderdduizend Syriërs: http://bit.ly/1KhSyYQ";
        text = " Hi my name is Suraj.. http://t.co url www.twitter.com ";
        //text = "www.twitter.com, www.yahoo.co.jp, t.co/blahblah, www.poloshirts.uk.com";
        System.out.println(getFormattedText(text));
    }
    
    public final int getFavCount() {
        return fav_count;
    }
    
    public final int getRetweetCount() {
        return retweet_count;
    }
    
    public final long getTime() {
        return creation_time;
    }
    
    public final String getCountryCode() {
        return country_code;
    }
    
    public final GeoLocation getGeoLocation() {
        return geoLocation;
    }
    
    public final String getLanguage() {
        return language_code;
    }
    
    public final String getKeywords() {
        return keywords;
    }
    
    public final int getSentiment() {
        return sentiment;
    }
    
    public static final String getCSVHeader(String DATA_SEPERATOR) {
        final String s = DATA_SEPERATOR;
        return "ID"+s+"Retweet ID"+s+"User ID"+s+"Text"+s+"Favorite Count"+s+
                "Retweet Count"+s+"Creation time"+s+"Country Code"+s+"Geolocation"
                +s+"Language Code"+s+"Keywords"+s+"Sentiment";
    }
    
    public String getSQLInsertQuery() {
        return "INSERT tweets VALUES(" 
                + id + "," 
                + retweet_id + "," 
                + user_id + ",'" 
                + text.replace("\'", "\\\'").replace("\"", "\\\"") + "'," 
                + fav_count + "," 
                + retweet_count + "," 
                + creation_time + ",'" 
                + country_code + "'," 
                + (geoLocation == null ? "NULL" : "'["+geoLocation.getLatitude()+","+geoLocation.getLongitude()+"]'") + ",'" 
                + language_code + "'," 
                + (keywords == null ? "NULL" : "'"+keywords+"'") + "," 
                + sentiment + ")";
    }
    
    @Override
    public String toString() {
        final String s = DATA_SEPERATOR;
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
     * Analyze the sentiment of tweet text based on given keyword.
     * @param text the tweet text
     * @return a score ranging from 0 (Very bad) to 4 (Very good).
     */
    private static int analyseSentiment(String text) {
        if(text == null)
            throw new IllegalArgumentException("Tweet text cannot be null.");
        return NLP.findSentiment(text);
    }
}