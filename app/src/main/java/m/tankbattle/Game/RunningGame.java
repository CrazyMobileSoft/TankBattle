package m.tankbattle.Game;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import m.tankbattle.R;

public class RunningGame extends Activity {

    private DrawGame drawGame;
    private static final int maxObj = 15;
    public static Bitmap [] bitmaps = new Bitmap[maxObj];
    protected static int ScreenY;
    protected static int ScreenX;
    private DatabaseReference mDatabase;
    protected static String KeyGame;
    protected static String userUID;
    protected static Boolean Colision = false;
    //protected static UserObj us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_game);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Zistenie velkosti displeja a zapísanie do premených
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ScreenY = displayMetrics.heightPixels;
        ScreenX = displayMetrics.widthPixels;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                KeyGame= null;
            } else {
                KeyGame= extras.getString("STRING_KEY_GAME");
            }
        } else {
            KeyGame = (String) savedInstanceState.getSerializable("STRING_KEY_GAME");
        }

       // mDatabase = FirebaseDatabase.getInstance().getReference();
       // mDatabase.child("Users").child(userUID).child("play").setValue(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) userUID = user.getUid();


        bitmaps[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg2),
                ScreenX, ScreenY, true);
        bitmaps[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.list3),
                ScreenX, (int)(ScreenY/5.34), true);
        bitmaps[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.arup),
                (int)(ScreenX/10.8), (int)(ScreenY/19.2), true);
        bitmaps[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ardown),
                (int)(ScreenX/10.8), (int)(ScreenY/19.2), true);
        bitmaps[4] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.arright),
                (int)(ScreenX/10.8), (int)(ScreenY/19.2), true);
        bitmaps[5] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.arleft),
                (int)(ScreenX/10.8), (int)(ScreenY/19.2), true);

        bitmaps[6] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.tankm2),
                (int)(ScreenX/10.8), (int)(ScreenY/19.2), true);

        bitmaps[7] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.enemytank),
                (int)(ScreenX/10.8), (int)(ScreenY/19.2), true);

        bitmaps[8] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.boxa),
                (int)(ScreenX/9), (int)(ScreenY/16), true);



        drawGame = new DrawGame(this,ScreenY,ScreenX);
        setContentView(drawGame);

    }


    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle, source.getWidth()/2 , source.getHeight()/2);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
