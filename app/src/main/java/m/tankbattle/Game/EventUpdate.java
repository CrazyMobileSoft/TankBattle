package m.tankbattle.Game;

import android.graphics.Rect;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import m.tankbattle.Menu.ObjBitmap;

import static m.tankbattle.Game.DrawGame.tanks;

/**
 * Created by Mato on 19.01.2017.
 */

public class EventUpdate {

    private static DatabaseReference mDatabase;
    private int Index;

    public EventUpdate() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void update() {
        findCollisionTank();
    }


    public boolean findCollision(TankObj obj1, ObjBitmap obj2) {
        Rect Rect1 = new Rect(obj1.getposX(), obj1.getposY(), obj1.getposX() + obj1.getWidth(), obj1.getposY() + obj1.getHeight());
        Rect Rect2 = new Rect(obj2.getposX(), obj2.getposY(), obj2.getposX() + obj2.getWidth(), obj2.getposY() + obj2.getHeight());
        if (Rect1.intersect(Rect2)) {
            RunningGame.Colision = true;
            return true;
        }
        return false;
    }

    public boolean findCollision(TankObj obj1, TankObj obj2) {
        Rect Rect1 = new Rect(obj1.getposX(), obj1.getposY(), obj1.getposX() + obj1.getWidth(), obj1.getposY() + obj1.getHeight());
        Rect Rect2 = new Rect(obj2.getposX(), obj2.getposY(), obj2.getposX() + obj2.getWidth(), obj2.getposY() + obj2.getHeight());
        if (Rect1.intersect(Rect2)) {
            RunningGame.Colision = true;
            return true;
        }
        return false;
    }


    public int getIndexOf(String useruid) {
        for (TankObj tank : DrawGame.tanks) {
            if (useruid.equalsIgnoreCase(tank.getUserid())) {
                return DrawGame.tanks.indexOf(tank);
            }
        }
        return -1;
    }

    public void findCollisionTank() {
        if (getIndexOf(RunningGame.userUID) >= 0) {
            Index = getIndexOf(RunningGame.userUID);
            for (BoxObj box : DrawGame.boxs) {
                if (findCollision(DrawGame.tanks.get(Index), box)) {
                    if (DrawGame.tanks.get(Index).getRotateTo() == 1) {
                        mDatabase.child("Games").child(RunningGame.KeyGame).child(RunningGame.userUID).child("tank").child("posY").setValue(box.getposY() + box.getHeight()); //tank ide dolava
                        DrawGame.tanks.get(Index).setPosY(box.getposY() + box.getHeight());
                    } else if (DrawGame.tanks.get(Index).getRotateTo() == 2) {
                        mDatabase.child("Games").child(RunningGame.KeyGame).child(RunningGame.userUID).child("tank").child("posY").setValue(box.getposY() - DrawGame.tanks.get(Index).getHeight());//tank ide doprava
                        DrawGame.tanks.get(Index).setPosY(box.getposY() - DrawGame.tanks.get(Index).getHeight());
                    } else if (DrawGame.tanks.get(Index).getRotateTo() == 3) {
                        mDatabase.child("Games").child(RunningGame.KeyGame).child(RunningGame.userUID).child("tank").child("posX").setValue(box.getposX() - DrawGame.tanks.get(Index).getWidth());//tank ide nahor
                        DrawGame.tanks.get(Index).setPosX(box.getposX() - DrawGame.tanks.get(Index).getWidth());
                    } else if (DrawGame.tanks.get(Index).getRotateTo() == 4) {
                        mDatabase.child("Games").child(RunningGame.KeyGame).child(RunningGame.userUID).child("tank").child("posX").setValue(box.getposX() + box.getWidth());//tank ide nadol
                        DrawGame.tanks.get(Index).setPosX(box.getposX() + box.getWidth());
                    }
                }
            }


            if (findCollision(DrawGame.tanks.get(Index), DrawGame.staticBitmaps.get(1))) {
                mDatabase.child("Games").child(RunningGame.KeyGame).child(RunningGame.userUID).child("tank").child("posY")
                        .setValue(DrawGame.staticBitmaps.get(1).getposY() + DrawGame.staticBitmaps.get(1).getHeight());//tank ide dolava
                tanks.get(Index).setPosY(DrawGame.staticBitmaps.get(1).getposY() + DrawGame.staticBitmaps.get(1).getHeight());
            }


            for (TankObj tank : tanks) {
                if (!tank.equals(tanks.get(Index))) {
                    if (findCollision(DrawGame.tanks.get(Index), tank)) {
                        if (DrawGame.tanks.get(Index).getRotateTo() == 1) {
                            mDatabase.child("Games").child(RunningGame.KeyGame).child(RunningGame.userUID).child("tank").child("posY").setValue(tank.getposY() + tank.getHeight());
                            DrawGame.tanks.get(Index).setPosY(tank.getposY() + tank.getHeight());//tank ide dolava
                        } else if (DrawGame.tanks.get(Index).getRotateTo() == 2) {
                            mDatabase.child("Games").child(RunningGame.KeyGame).child(RunningGame.userUID).child("tank").child("posY").setValue(tank.getposY() - tank.getHeight());
                            DrawGame.tanks.get(Index).setPosY(tank.getposY() - tank.getHeight());//tank ide doprava
                        } else if (DrawGame.tanks.get(Index).getRotateTo() == 3) {
                            mDatabase.child("Games").child(RunningGame.KeyGame).child(RunningGame.userUID).child("tank").child("posX").setValue(tank.getposX() - tank.getWidth());
                            DrawGame.tanks.get(Index).setPosX(tank.getposX() - tank.getWidth());//tank ide nahor
                        } else if (DrawGame.tanks.get(Index).getRotateTo() == 4) {
                            mDatabase.child("Games").child(RunningGame.KeyGame).child(RunningGame.userUID).child("tank").child("posX").setValue(tank.getposX() + tank.getWidth());
                            DrawGame.tanks.get(Index).setPosX(tank.getposX() + tank.getWidth());//tank ide nadol
                        }
                    }
                }
            }
        }
    }
}
