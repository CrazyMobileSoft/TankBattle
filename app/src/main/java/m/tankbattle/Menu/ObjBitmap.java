package m.tankbattle.Menu;

import android.graphics.Bitmap;

/**
 * Created by Mato on 16.10.2016.
 */

public class ObjBitmap {

    private int posx, posy;
    private int type;
    private Bitmap bitmap;
    //Konštruktor
    public ObjBitmap(){}

    public ObjBitmap(Bitmap bitmap, int posx, int posy, int type){
        this.bitmap = bitmap;
        this.posx = posx;
        this.posy = posy;
        this.type = type;
    }
    public ObjBitmap(Bitmap bitmap, int posx , int posy){
        this.bitmap = bitmap;
        this.posx = posx;
        this.posy = posy;
    }
    public ObjBitmap(int posx, int posy){
        this.posx = posx;
        this.posy = posy;
    }


    //getery a setery
    public Bitmap getBitmap(){
        return bitmap;
    }
    public int getType(){
        return type;
    }
    public int getposX(){
        return posx;
    }
    public int getposY(){
        return posy;}
    public int getWidth(){
        return bitmap.getWidth();}
    public int getHeight(){
        return bitmap.getHeight();}

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    public void setType(int type){
        this.type = type;
    }
    public void setPosX(int posx){
        this.posx = posx;
    }
    public void setPosY(int posy){
        this.posy = posy;
    }
}
