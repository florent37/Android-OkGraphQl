package florent37.github.com.rxgraphql.model;

import java.util.List;

/**
 * Created by florentchampigny on 12/07/2017.
 */

public class Character {
    private String name;
    private List<Character> friends;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Character> getFriends() {
        return friends;
    }

    public void setFriends(List<Character> friends) {
        this.friends = friends;
    }
}
