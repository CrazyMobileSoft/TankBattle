package m.tankbattle.Menu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import m.tankbattle.Game.RunningGame;
import m.tankbattle.Game.TankObj;
import m.tankbattle.R;




public class ConnectRoomActivity extends Activity implements View.OnClickListener  {

    private Button btnRefresh;
    //private Button btnPlay;
    private ConnectService  connectService;
    private ArrayList<UserObj> playlist;
    private DatabaseReference mDatabase ;
    private String userUID;
    private ProgressDialog progressDialog;
    private String key = "";
    private static String enemyUid = "";
    public static int ScreenY;
    public static int ScreenX;
    public static boolean startActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_room);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        //btnPlay = (Button)findViewById(R.id.btnPlay);
        //btnPlay.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ScreenY = displayMetrics.heightPixels;
        ScreenX = displayMetrics.widthPixels;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) userUID = user.getUid();
        try {
            mDatabase.child("Users").child(userUID).child("logIn").setValue(true);
            mDatabase.child("Users").child(userUID).child("play").setValue(false);
            mDatabase.child("Users").child(userUID).child("ready").setValue(true);
            //mDatabase.child("Users").child(userUID).child("keyGame").setValue("null");
            // mDatabase.child("LogInPlayers").child(userUID).setValue(userUID);
        }catch (Exception e) {throw e;}



        //Intent ServiceIntent = new Intent(this, ConnectService.class);
        //this.startService(ServiceIntent);
        //registerReceiver(MessageFromService, new IntentFilter("start"));
        connectService = new ConnectService(getApplicationContext());
        connectService.WaitForSignal();
        refreshList();

    }
    /**private final BroadcastReceiver MessageFromService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startActivity();
        }
    };*/

    public void creatingGame(String enemyUID){
        enemyUid = enemyUID;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) userUID = user.getUid();

        try {
            key = mDatabase.child("Games").push().getKey();
            //mDatabase.child("Games").child(key).child("pushCode").setValue(key);
          //  TankObj post = new TankObj(tanks.get(i).getposX(), tanks.get(i).getposY() + tankSpeed, tanks.get(i).getLife(), 2);
         //   childUpdates.put("/Games/" + RunningGame.KeyGame + "/" + tanks.get(i).getUserid() + "/tank", post.toMap());
            mDatabase.child("Games").child(key).child(userUID).child("tank").child("posX").setValue(ScreenX/2);
            mDatabase.child("Games").child(key).child(userUID).child("tank").child("posY").setValue((int)(ScreenY - (ScreenY/19.2)));
            mDatabase.child("Games").child(key).child(userUID).child("tank").child("rotateTo").setValue(1);
            mDatabase.child("Games").child(key).child(userUID).child("tank").child("life").setValue(10);
            mDatabase.child("Users").child(enemyUID).child("keyGame").setValue(key);
            mDatabase.child("Users").child(enemyUID).child("play").setValue(true);
            mDatabase.child("Users").child(userUID).child("keyGame").setValue(key);
            startActivity(key);
        }catch (Exception e){
            throw e;
        }

    }

    public void startActivity(String key){
        Intent newActivity = new Intent(ConnectRoomActivity.this, RunningGame.class);
        newActivity.putExtra("STRING_KEY_GAME", key);
        startActivity(newActivity);
        finish();
        /*Query query = mDatabase.child("Users").child(userUID).child("keyGame");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                key = dataSnapshot.getValue().toString();
                Intent newActivity = new Intent(ConnectRoomActivity.this, RunningGame.class);
                newActivity.putExtra("STRING_KEY_GAME", key);
                startActivity(newActivity);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


    }

    public void UserList(ArrayList<UserObj> users){
        final ArrayList<UserObj> list = new ArrayList<>();
        if (!users.isEmpty()){
            for (UserObj u: users){
                if (!u.getUserUid().equalsIgnoreCase(userUID)){
                   list.add(new UserObj(u.getS(), u.getUserUid()));
                }
            }

            ListView UserList = (ListView) findViewById(R.id.listView);
            MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, list);
            UserList.setAdapter(adapter);

            UserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectUser = list.get(position).getUserUid();
                    creatingGame(selectUser);
                }
            });
            playlist.clear();
        }
    }

    public void refreshList(){
        playlist = new ArrayList<>();

        mDatabase.child("Users").orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    UserObj us = snap.getValue(UserObj.class);
                    String useruid = snap.getKey();
                    if (us.isLogIn() && us.isReady() && (!us.isPlay()) && (!playlist.contains(useruid))) {
                        playlist.add(new UserObj(us.getName() + " win: " + us.getWin() + " lose: " + us.getLose(), useruid));
                    }
                }
                if (!playlist.isEmpty())UserList(playlist);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ConnectRoomActivity.this , "Failed to load from database.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mDatabase.child("Users").child(userUID).child("ready").setValue(true);
        mDatabase.child("Users").child(userUID).child("play").setValue(false);

    }
    @Override
    public void onStop(){
        super.onStop();
        //stopService(new Intent(ConnectRoomActivity.this ,ConnectService.class));
        mDatabase.child("Users").child(userUID).child("ready").setValue(false);
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //unregisterReceiver(MessageFromService);
        mDatabase.child("Users").child(userUID).child("ready").setValue(false);
    }

    @Override
    public void onClick(View v) {
        Vibrator s = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        if(v == btnRefresh){
            s.vibrate(15);
            refreshList();
        }//else if(v == btnPlay){
        //s.vibrate(15);
        //btnPlay();
        //}
    }

}