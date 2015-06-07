package twitterstream;

import java.util.Objects;
import twitter4j.User;

/**
 * Twitter user
 * 
 * @author S.S.Iyer
 */
public class UserEntity {
    
    private String dataSeperator = ";";
    private final long id;
    private final String username;
    private final int nr_of_followers;
    private final int fav_count;
    private final int nr_of_friends;
    
    public UserEntity(String dataSeperator, User user) {
        this(dataSeperator, user.getId(), user.getName(), user.getFollowersCount(),
                user.getFavouritesCount(), user.getFriendsCount());
    }
    
    public UserEntity(String dataSeperator, long id, String name, 
            int followers, int favourites, int friends) {
        this.dataSeperator = dataSeperator;
        this.id = id;
        this.username = cleanText(name);
        this.nr_of_followers = followers;
        this.fav_count = favourites;
        this.nr_of_friends = friends;
    }
    
    private String cleanText(String text) {
        text = text.replace("?", " ");
        text = text.replace("\n", " ");
        text = text.replace("\t", " ");
        return text;
    }
    
    public String escapeText(String text) {
        text = text.replace("\'", "\\\'");
        text = text.replace("\"", "\\\"");
        return text;
    }
    
    public final long getID() {
        return id;
    }
    
    public final String getName(boolean escape) {
        if(escape)
            return escapeText(username);
        return username;
    }
    
    public final int getNrOfFollowers() {
        return nr_of_followers;
    }
    
    public final int getFavCount() {
        return fav_count;
    }
    
    public final int getNrOfFriends() {
        return nr_of_friends;
    }
    
    public static final String getCSVHeader(String dataSeperator) {
        final String s = dataSeperator;
        return "ID"+s+"Username"+s+"Number of followers"+s+"Favourite Count"+s
                +"Number of friends";
    }
    
    @Override
    public String toString() {
        final String s = dataSeperator;
        return id + s + username + s + nr_of_followers + s + fav_count + s + nr_of_friends;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserEntity)) {
            return false;
        }
        
        UserEntity entity = (UserEntity) obj;
        return this.hashCode() == entity.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 59 * hash + Objects.hashCode(this.username);
        hash = 59 * hash + this.nr_of_followers;
        hash = 59 * hash + this.fav_count;
        hash = 59 * hash + this.nr_of_friends;
        return hash;
    }

}