package m.tankbattle.Menu;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Vibrator;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import m.tankbattle.R;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnSignIn;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnSignIn = (Button) findViewById(R.id.btnSingIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        Intent i = null;
        Vibrator s = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        switch(v.getId()){
            case R.id.btnSingIn:
                s.vibrate(15);
                i = new Intent(this,SignInActivity.class);
                break;
            case R.id.btnSignUp:
                s.vibrate(15);
                i = new Intent(this,SignUpActivity.class);
                break;
        }
        startActivity(i);
        //finish();
    }
}
