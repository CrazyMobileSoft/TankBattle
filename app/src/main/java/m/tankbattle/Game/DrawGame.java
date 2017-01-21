package m.tankbattle.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import m.tankbattle.Menu.ObjBitmap;

/**
 * Created by Mato on 19.10.2016.
 */

public class DrawGame extends View {

    protected static int EventIndex;
    protected static ArrayList<ObjBitmap> staticBitmaps;
    protected static ArrayList<BoxObj> boxs;
    protected static ArrayList<TankObj> tanks;
    private DatabaseReference mDatabase;
    private DatabaseUpdate databaseUpdate;
    private EventUpdate eventUpdate;
   // private String userUID;


    public DrawGame(Context context, int height, int width) {
        super(context);
        databaseUpdate = new DatabaseUpdate(width);
        staticBitmaps = new ArrayList<>();
        eventUpdate = new EventUpdate();
        boxs = new ArrayList<>();
        tanks = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();



        createBitmaps();
    }

    public void onDraw(Canvas canvas) {

        for (int i = 0; i < staticBitmaps.size(); i++) {
            canvas.drawBitmap(staticBitmaps.get(i).getBitmap(),
                    staticBitmaps.get(i).getposX(), staticBitmaps.get(i).getposY(), null);
        }

        for (int i = 0; i < boxs.size(); i++) {
            canvas.drawBitmap(boxs.get(i).getBitmap(),
                    boxs.get(i).getposX(), boxs.get(i).getposY(), null);
        }


        databaseUpdate.update();
        eventUpdate.update();
        for (int i = 0; i < tanks.size(); i++) {
            canvas.drawBitmap(tanks.get(i).getBitmap(),
                    tanks.get(i).getposX(), tanks.get(i).getposY(), null);
        }


        invalidate();
    }

    public void createBitmaps() {
        //pozadia s typom 0
        staticBitmaps.add(new ObjBitmap(RunningGame.bitmaps[0], 0, 0, 0));
        //lista s typom 1
        staticBitmaps.add(new ObjBitmap(RunningGame.bitmaps[1], 0, 0, 1));
        //sipky s typom 2
        staticBitmaps.add(new ObjBitmap(RunningGame.bitmaps[2], (int) (RunningGame.ScreenX / 10.8), 20, 2));
        staticBitmaps.add(new ObjBitmap(RunningGame.bitmaps[3], (int) (RunningGame.ScreenX / 10.8), (int) (RunningGame.ScreenY / 8.73),2));
        staticBitmaps.add(new ObjBitmap(RunningGame.bitmaps[4], (int) (RunningGame.ScreenX /5.4 ), (int) (RunningGame.ScreenY / 16), 2));
        staticBitmaps.add(new ObjBitmap(RunningGame.bitmaps[5], 0,(int)(RunningGame.ScreenY / 16), 2));
        staticBitmaps.add(new ObjBitmap(RunningGame.RotateBitmap(RunningGame.bitmaps[6], 180), (int)(RunningGame.ScreenX - (int)(RunningGame.ScreenX/10.8)) , (int)(RunningGame.ScreenY/38.4), 3));
        staticBitmaps.add(new ObjBitmap(RunningGame.bitmaps[7], (int) (RunningGame.ScreenX - (int)(RunningGame.ScreenX/5.4)), (int)(RunningGame.ScreenY/38.4), 3));
       //box s typom 4
        ArrayList<String> cisla = new ArrayList<>();
        cisla.add("10010111101");
        cisla.add("01100100100");
        cisla.add("00000001101");
        cisla.add("01011000100");
        cisla.add("10100010001");
        cisla.add("00001010011");
        cisla.add("11101111010");
        for (int i = 0; i< cisla.size(); i++){
            for (int j = 0; j<11; j++){
                Character sub = cisla.get(i).charAt(j);
                if (sub ==('1')){
                    boxs.add(new BoxObj(RunningGame.bitmaps[8], (int)RunningGame.ScreenX -((i+2)*(RunningGame.ScreenX/9)),
                            (int)(RunningGame.ScreenY)-((j+2)*(RunningGame.ScreenY/16)) ,4));
                }
            }
        }

        tanks.add(new TankObj(RunningGame.bitmaps[6], RunningGame.ScreenX/2 ,(int) (RunningGame.ScreenY - (RunningGame.ScreenY/19.2)),  10, 1, null));
        tanks.add(new TankObj(RunningGame.bitmaps[7], RunningGame.ScreenX/2,(int)(RunningGame.ScreenY/5.2),  10, 2, null));
        //tank s typom 3
       final Query query = mDatabase.child("Games").child(RunningGame.KeyGame).orderByValue();
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i=0;
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            String useruid = snap.getKey();
                            tanks.get(i).setUserid(useruid);
                            if (useruid.equalsIgnoreCase(RunningGame.userUID)) {
                                TankObj post = new TankObj(tanks.get(i).getposX(), tanks.get(i).getposY(), tanks.get(i).getLife(), tanks.get(i).getRotateTo());
                                Map<String, Object> postValues = post.toMap();
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("/Games/" + RunningGame.KeyGame + "/" + tanks.get(i).getUserid() + "/tank"  , postValues);
                                mDatabase.updateChildren(childUpdates);
                            }
                            i++;
                        }
                        query.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("DATABASE ERROR");
                    }
        });

    }


    public boolean onTouchEvent(MotionEvent touchEvent) {
        int maskedAction = touchEvent.getActionMasked();

        boolean hold = false;
        if (maskedAction == MotionEvent.ACTION_MOVE || maskedAction == MotionEvent.ACTION_DOWN) {
            hold = true;
            for (int i = 2; i < 6; i++) {
                if (((touchEvent.getX() > DrawGame.staticBitmaps.get(i).getposX()) &&
                        (touchEvent.getX() < (DrawGame.staticBitmaps.get(i).getposX() + DrawGame.staticBitmaps.get(i).getBitmap().getWidth())))
                        && ((touchEvent.getY() > DrawGame.staticBitmaps.get(i).getposY()) &&
                        (touchEvent.getY() < (DrawGame.staticBitmaps.get(i).getposY() + DrawGame.staticBitmaps.get(i).getBitmap().getHeight())))) {
                    DrawGame.EventIndex = i-1;
                }
            }
        }
        if (maskedAction == MotionEvent.ACTION_UP ){
            hold = false;

        }
        /**if (maskedAction == MotionEvent.ACTION_POINTER_1_DOWN || maskedAction == MotionEvent.ACTION_DOWN){
         fire = true;
         System.out.println(fire);}*/

        // if (maskedAction == MotionEvent.ACTION_POINTER_1_UP|| maskedAction == MotionEvent.ACTION_UP)fire = false;
        if (!hold)DrawGame.EventIndex = 0;
        return true;
    }

}



