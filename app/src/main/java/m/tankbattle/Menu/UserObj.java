package m.tankbattle.Menu;

import m.tankbattle.Game.TankObj;

/**
 * Created by Mato on 25.10.2016.
 */

public class UserObj {

    private boolean play;
    private boolean logIn;
    private boolean ready;
    private int win;
    private int lose;
    private String name;
    private String userUid;
    private String ss;
    private String keyGame;
    //private TankObj tank;

    public UserObj(){}

    public UserObj(String name,boolean ready, boolean play, boolean logIn, int win, int lose){
        this.name = name;
        this.play = play;
        this.logIn = logIn;
        this.win = win;
        this.lose = lose;
        this.ready = ready;
    }
    public UserObj(String ss, String uid){
        this.ss = ss;
        this.userUid = uid;
    }

    public UserObj(String userUid, String name, int win, int lose, String keyGame){
        this.userUid = userUid;
        this.name = name;
        this.win = win;
        this.lose = lose;
        this.keyGame = keyGame;
    }
    public String getS(){
        return ss;
    }
    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public boolean isLogIn() {
        return logIn;
    }

    public void setLogIn(boolean logIn) {
        this.logIn = logIn;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public String getKeyGame() {
        return keyGame;
    }
    public void setKeyGame(String keyGame) {
        this.keyGame = keyGame;
    }
    public void setLose(int lose) {
        this.lose = lose;
    }
    public boolean isReady() {
        return ready;
    }
    @Override
    public String toString() {
        return "UserObj{" +
                "play=" + play +
                ", logIn=" + logIn +
                ", win=" + win +
                ", lose=" + lose +
                ", name='" + name + '\'' +
                ", userUid='" + userUid + '\'' +
                '}';
    }
}
