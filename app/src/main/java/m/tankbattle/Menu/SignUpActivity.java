package m.tankbattle.Menu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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

public class SignUpActivity extends Activity implements View.OnClickListener{

    private EditText Email;
    private EditText UserName;
    private EditText Password;
    private EditText ConfirmPassword;
    private Button SignUp;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private String userUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        Email = (EditText)findViewById(R.id.etEmailReg);
        UserName = (EditText)findViewById(R.id.etUserNameReg);
        Password = (EditText)findViewById(R.id.etPassReg);
        ConfirmPassword = (EditText)findViewById(R.id.etPassRegConfirm);
        SignUp = (Button)findViewById(R.id.btnSingup);

        SignUp.setOnClickListener(this);




    }

    public void registerUser(){
        String email = Email.getText().toString().trim();
        String passConf = ConfirmPassword.getText().toString().trim();
        String password = Password.getText().toString().trim();



        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email!",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(passConf)){
            Toast.makeText(this, "Please enter confirmation password!", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password!",Toast.LENGTH_LONG).show();
            return;
        }
        if (passConf.equalsIgnoreCase(password)) {
            progressDialog.setMessage("Registering Please Wait...");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) userUID = user.getUid();
                                startLogInActivity();

                            } else if (!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Authentication failed. Try Again!",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }
                        }
                    });
        }else {
            Toast.makeText(SignUpActivity.this, "Authentication failed. Password are not same!",
                    Toast.LENGTH_SHORT).show();
            progressDialog.hide();
        }
    }
    public void createNewUser(){
        String username = UserName.getText().toString();
        UserObj user = new UserObj(username,false, false, false, 0, 0);
        mDatabase.child("Users").child(userUID).setValue(user);
        mDatabase.child("Users").child(userUID).child("keyGame").setValue("null");
    }

    public void startLogInActivity(){
        createNewUser();
        mAuth.signOut();
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        Vibrator s = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        if (v == SignUp){
            s.vibrate(15);
            registerUser();
        }
    }
}
