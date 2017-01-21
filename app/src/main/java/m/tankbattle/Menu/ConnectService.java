package m.tankbattle.Menu;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import m.tankbattle.Game.RunningGame;

/**
 * Created by Mato on 15.12.2016.
 */

public class ConnectService {

    private DatabaseReference mDatabase;
    private String userUID;
    private final Context context;
    private boolean activityStart = true;

    public ConnectService(Context context) {
        this.context = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) userUID = user.getUid();
    }


    public void WaitForSignal() {

        final Query query = mDatabase.child("Users").child(userUID).orderByValue();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String keyGame = dataSnapshot.child("keyGame").getValue().toString();
                    String play = dataSnapshot.child("play").getValue().toString();
                    if ((!keyGame.equalsIgnoreCase("null")) && play.equalsIgnoreCase("true")) {
                        mDatabase.child("Games").child(keyGame).child(userUID).child("tank").child("posX").setValue((int) (ConnectRoomActivity.ScreenX / 2));
                        mDatabase.child("Games").child(keyGame).child(userUID).child("tank").child("posY").setValue((int) (ConnectRoomActivity.ScreenY / 5.2));
                        mDatabase.child("Games").child(keyGame).child(userUID).child("tank").child("rotateTo").setValue(2);
                        mDatabase.child("Games").child(keyGame).child(userUID).child("tank").child("life").setValue(10);
                        if (activityStart) {
                            startActivity(keyGame);
                            activityStart = false;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("DATABASE ERROR");
            }
        });


    }

    public void startActivity(String key) {
        Intent newActivity = new Intent(context, RunningGame.class);
        newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        newActivity.putExtra("STRING_KEY_GAME", key);
        context.startActivity(newActivity);
    }
}
