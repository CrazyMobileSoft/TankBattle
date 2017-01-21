package m.tankbattle.Game;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mato on 19.10.2016.
 */

public class TankObj  {
    private int life;
    private int rotateTo; //dolava-1, doprava-2 ,nahor-3 ,nadol-4
    private String userId;
    private int posX, posY;
   // private int type;
    private Bitmap bitmap;
    public Map<String, Boolean> stars = new HashMap<>();

    //Konštruktor
    public TankObj(){}

    public TankObj(Bitmap bitmap,int posX, int posY, int life, int rotateTo, String userid) {
        this.bitmap = bitmap;
        this.posX = posX;
        this.posY = posY;
        this.life = life;
        this.rotateTo = rotateTo;
        this.userId = userid;
    }

    public TankObj(int posX , int posY, int life, int rotateTo){
        this.posX = posX;
        this.posY = posY;
        this.life = life;
        this.rotateTo = rotateTo;
    }

    //Getery a setery
    public Bitmap getBitmap(){
        return bitmap;
    }

    public int getWidth(){
        return bitmap.getWidth();}
    public int getHeight(){
        return bitmap.getHeight();}
    public int getposX(){
        return posX;
    }
    public int getposY(){
        return posY;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    public void setPosX(int posx){
        this.posX = posx;
    }
    public void setPosY(int posy){
        this.posY = posy;
    }
    public int getRotateTo() { return rotateTo; }
    public int getLife(){
        return life;
    }

    public void setRotateTo(int rotateTo){
        this.rotateTo = rotateTo;
    }
    public void setLife(int life){
        this.life = life;
    }
    public String getUserid() {
        return userId;
    }

    public void setUserid(String userid) {
        this.userId = userid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("posX", posX);
        result.put("posY", posY);
        result.put("life", life);
        result.put("rotateTo", rotateTo);
        return result;
    }

    //pohyb doprava
    public void MoveRight(int tankSpeed){
        setPosY((int) (getposY() + tankSpeed));
        if (getposY() > RunningGame.ScreenY - (int) (RunningGame.ScreenX / 11)) setPosY(RunningGame.ScreenY - (int) (RunningGame.ScreenX / 11));
    }
    //pohyb dolava
    public void MoveLeft(int tankSpeed){
        setPosY((int) (getposY() + (-tankSpeed)));
        if (getposY() < 0) setPosY(0);
    }
    //pohyb nahor
    public void MoveUp(int tankSpeed){
        setPosX((int) (getposX() + (tankSpeed)));
        if (getposX() > RunningGame.ScreenX - (int) (RunningGame.ScreenX / 11)) setPosX(RunningGame.ScreenX - (int) (RunningGame.ScreenX / 11));
    }
    //pohyb nadol
    public void MoveDown(int tankSpeed){
        setPosX((int) (getposX() + (-tankSpeed)));
        if (getposX() < 0) setPosX(0);
    }
    //otočenie bitmapy doprava
    public void rotateRight(){
        switch (rotateTo) {
            case 4:
                setBitmap(RunningGame.RotateBitmap(getBitmap(), 270));
                break;
            case 3:
                setBitmap(RunningGame.RotateBitmap(getBitmap(), 90));
                break;
            case 1:
                setBitmap(RunningGame.RotateBitmap(getBitmap(), 180));
                break;
        }
        rotateTo = 2;
    }
    //otočenie bitmapy dolava
    public void rotateLeft(){
        switch (rotateTo) {
            case 4:
                setBitmap(RunningGame.RotateBitmap(getBitmap(), 90));
                break;
            case 3:
                setBitmap(RunningGame.RotateBitmap(getBitmap(), 270));
                break;
            case 2:
                setBitmap(RunningGame.RotateBitmap(getBitmap(), 180));
                break;
        }
        rotateTo = 1;
    }
    //otočenie bitmapy nahor
    public void rotateUp(){
        switch (rotateTo) {
            case 4:
                setBitmap(RunningGame.RotateBitmap(getBitmap(), 180));
                break;
            case 2:
                setBitmap(RunningGame.RotateBitmap(getBitmap(), 270));
                break;
            case 1:
                setBitmap(RunningGame.RotateBitmap(getBitmap(), 90));
                break;
        }
        rotateTo = 3;
    }
    //otočenie bitmapy nadol
    public void rotateDown(){
        switch (rotateTo) {
            case 3:
                setBitmap(RunningGame.RotateBitmap(getBitmap(), 180));
                break;
            case 2:
                setBitmap(RunningGame.RotateBitmap(getBitmap(), 90));
                break;
            case 1:
                setBitmap(RunningGame.RotateBitmap(getBitmap(), 270));
                break;
        }
        rotateTo = 4;
    }
}
