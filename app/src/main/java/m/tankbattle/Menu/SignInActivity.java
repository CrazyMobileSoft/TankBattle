package m.tankbattle.Menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import m.tankbattle.R;

import static android.content.ContentValues.TAG;

public class SignInActivity extends Activity implements View.OnClickListener{

    private EditText email;
    private EditText password;
    private Button SignIn;
    private Button LogInA;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        email = (EditText)findViewById(R.id.etUserNameLog);
        password = (EditText)findViewById(R.id.etPassLog);
        SignIn = (Button)findViewById(R.id.btnSingIn);
        LogInA = (Button)findViewById(R.id.btnLogAnonymous);

        SignIn.setOnClickListener(this);
        LogInA.setOnClickListener(this);


    }

    public void LoginUser(){
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();


        if(TextUtils.isEmpty(Email)){
            Toast.makeText(this,"Please enter email!",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(Password)){
            Toast.makeText(this,"Please enter password!",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) userUID = user.getUid();
                            updateLogInStatus();
                            startMenuActivity();
                        }else if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }

                    }
                });
    }
    public void LoginAnonymos(){
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) userUID = user.getUid();
                            createAnonymUser();
                        startMenuActivity();
                        }
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInAnonymously", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }

                        // ...
                    }
                });
    }
    public void createAnonymUser(){
        UserObj us = new UserObj("Anonymous",false, false, true, 0, 0);
        mDatabase.child("Users").child(userUID).setValue(us);
        mDatabase.child("Users").child(userUID).child("keyGame").setValue("null");

    }
    public void updateLogInStatus(){
        mDatabase.child("Users").child(userUID).child("logIn").setValue(true);
    }

    public void startMenuActivity(){
        startActivity(new Intent(SignInActivity.this, MenuActivity.class));
        finish();
        }


    @Override
    public void onClick(View v) {
        Vibrator s = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        if (v == SignIn){
            s.vibrate(15);
            LoginUser();
        }else if (v == LogInA){
            s.vibrate(15);
            LoginAnonymos();
        }
    }
}