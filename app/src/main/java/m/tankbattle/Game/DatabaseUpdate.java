package m.tankbattle.Game;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static m.tankbattle.Game.DrawGame.tanks;

/**
 * Created by Mato on 29.12.2016.
 */

public class DatabaseUpdate {

    private static DatabaseReference mDatabase;
    private static FirebaseUser user;
    private static String userUid;
    private int tankSpeed = 0;
    private Map<String, Object> childUpdates;


    public DatabaseUpdate(int width) {
        this.tankSpeed = (int) width/216;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) userUid = user.getUid();
        childUpdates = new HashMap<>();
    }

    public void update() {
        tankPositionGamepad();
        updateTanks();
    }

    public void updateTanks() {
        Query query = mDatabase.child("Games").child(RunningGame.KeyGame).orderByValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    TankObj tank = snap.child("tank").getValue(TankObj.class);
                    int i = (getIndexOf(snap.getKey()));
                    if (i >= 0) {
                        DrawGame.tanks.get(i).setPosX(tank.getposX());
                        DrawGame.tanks.get(i).setPosY(tank.getposY());
                        switch (tank.getRotateTo()) {
                            case 1: {
                                DrawGame.tanks.get(i).rotateLeft();
                                break;
                            }
                            //pohyb doprava
                            case 2: {
                                DrawGame.tanks.get(i).rotateRight();
                                break;
                            }
                            //pohyb dohora
                            case 3: {
                                DrawGame.tanks.get(i).rotateUp();
                                break;
                            }
                            //pohyb tanku dole
                            case 4: {
                                DrawGame.tanks.get(i).rotateDown();
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("DATABASE ERROR");
            }
        });
    }

    public int getIndexOf(String useruid) {
        for (TankObj tank : tanks) {
            if (useruid.equalsIgnoreCase(tank.getUserid())) {
                return tanks.indexOf(tank);
            }
        }
        return -1;
    }

    public void tankPositionGamepad() {

        int i = getIndexOf(userUid);
        //pohyb tanku dolava
        switch (DrawGame.EventIndex) {
            case 1: {
                if (!RunningGame.Colision) {
                    TankObj post = new TankObj(tanks.get(i).getposX(), tanks.get(i).getposY() + (-tankSpeed), tanks.get(i).getLife(), 1);
                    childUpdates.put("/Games/" + RunningGame.KeyGame + "/" + tanks.get(i).getUserid() + "/tank", post.toMap());
                    mDatabase.updateChildren(childUpdates);
                }
                break;
            }
            //pohyb doprava
            case 2: {

                if ((tanks.get(i).getposY() + tankSpeed) > RunningGame.ScreenY - (int) (RunningGame.ScreenX / 11)) {
                    TankObj post = new TankObj(tanks.get(i).getposX(), tanks.get(i).getposY(), tanks.get(i).getLife(), 2);
                    childUpdates.put("/Games/" + RunningGame.KeyGame + "/" + tanks.get(i).getUserid() + "/tank", post.toMap());

                } else if (!RunningGame.Colision){
                    TankObj post = new TankObj(tanks.get(i).getposX(), tanks.get(i).getposY() + tankSpeed, tanks.get(i).getLife(), 2);
                    childUpdates.put("/Games/" + RunningGame.KeyGame + "/" + tanks.get(i).getUserid() + "/tank", post.toMap());
                }
                mDatabase.updateChildren(childUpdates);
                break;
            }
            //pohyb dohora
            case 3: {

                if ((tanks.get(i).getposX() + tankSpeed) > RunningGame.ScreenX - (int) (RunningGame.ScreenX / 11)) {
                    TankObj post = new TankObj(tanks.get(i).getposX(), tanks.get(i).getposY(), tanks.get(i).getLife(), 3);
                    childUpdates.put("/Games/" + RunningGame.KeyGame + "/" + tanks.get(i).getUserid() + "/tank", post.toMap());
                } else if (!RunningGame.Colision){
                    TankObj post = new TankObj(tanks.get(i).getposX() + (tankSpeed), tanks.get(i).getposY(), tanks.get(i).getLife(), 3);
                    childUpdates.put("/Games/" + RunningGame.KeyGame + "/" + tanks.get(i).getUserid() + "/tank", post.toMap());
                }
                mDatabase.updateChildren(childUpdates);
                break;
            }
            //pohyb tanku dole
            case 4: {
                if (tanks.get(i).getposX() - tankSpeed < 0) {
                    TankObj post = new TankObj(0, tanks.get(i).getposY(), tanks.get(i).getLife(), 4);
                    childUpdates.put("/Games/" + RunningGame.KeyGame + "/" + tanks.get(i).getUserid() + "/tank", post.toMap());
                } else if (!RunningGame.Colision){
                    TankObj post = new TankObj(tanks.get(i).getposX() + (-tankSpeed), tanks.get(i).getposY(), tanks.get(i).getLife(), 4);
                    childUpdates.put("/Games/" + RunningGame.KeyGame + "/" + tanks.get(i).getUserid() + "/tank", post.toMap());
                }
                mDatabase.updateChildren(childUpdates);
                break;
            }

        }
        childUpdates.clear();
        RunningGame.Colision = false;
    }
}



