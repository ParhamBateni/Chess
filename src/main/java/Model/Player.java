package Model;

import java.util.ArrayList;

public class Player {
    public static ArrayList<Player> players;

    private String username;
    private String password;
    private long point;

    static{
        players=new ArrayList<>();
    }

    public Player(String username,String password){
        setUsername(username);
        setPassword(password);
        this.point=0;
        players.add(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getPoint() {
        return point;
    }

    public void changePoint(long value){
        this.point+=value;
    }
}
