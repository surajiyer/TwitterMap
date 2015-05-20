package twitterstream;

import java.util.Objects;
import twitter4j.User;

/**
 *
 * @author s139662
 */
public class UserEntity {
    
    private String dataSeperator = ";";
    private final long id;
    private final String name;
    private final int followers;
    private final int favourites;
    private final int friends;
    
    public UserEntity(String dataSeperator, User user) {
        this(dataSeperator, user.getId(), user.getName(), 
                user.getFollowersCount(), user.getFavouritesCount(),
                user.getFriendsCount());
    }
    
    public UserEntity(String dataSeperator, long id, String name, 
            int followers, int favourites, int friends) {
        this.dataSeperator = dataSeperator;
        this.id = id;
        this.name = formatText(name);
        this.followers = followers;
        this.favourites = favourites;
        this.friends = friends;
    }
    
    private String formatText(String text) {
        text = text.replace("\n", " ");
        text = text.replace("  ", " ");
        
        return text;
    }
    
    public final long getID() {
        return id;
    }
    
    public final String getName() {
        return name;
    }
    
    public final int getFollowers() {
        return followers;
    }
    
    public final int getFavourites() {
        return favourites;
    }
    
    public final int getFriends() {
        return friends;
    }
    
    public static final String getCSVHeader(String dataSeperator) {
        final String s = dataSeperator;
        return "ID"+s+"Username"+s+"Number of followers"+s+"Favourite Count"+s
                +"Number of friends";
    }
    
    @Override
    public String toString() {
        final String s = dataSeperator;
        return id + s + name + s + followers + s + favourites + s + friends;
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
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + this.followers;
        hash = 59 * hash + this.favourites;
        hash = 59 * hash + this.friends;
        return hash;
    }

}