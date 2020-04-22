package com.example.env;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private TextView tvRegister;

    private FirebaseAuth mAuth;

//    private Toolbar mToolbar;

    private ProgressDialog mLoginProgress;

    private ArrayList<String> bannedUsersList = new ArrayList<>(); //to pull from firebase

    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //bannedUsersList.add("test@gmail.com");

        //Toolbar set
//        mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle(R.string.login);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //progress dialogue
        mLoginProgress = new ProgressDialog(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        Log.d("TEST1", "now in login");


        mEmail = (TextInputEditText) findViewById(R.id.login_email);
        mPassword = (TextInputEditText) findViewById(R.id.login_password);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        Button mlogin_btn = (Button) findViewById(R.id.login_btn);
        //Button mregister_btn = (Button) findViewById(R.id.login_to_create);
        Log.d("TEST1", "got the textboxes");

        mlogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();


                if (!(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))) {
                    if(checkUserBan(email)){
                        Toast.makeText(LoginActivity.this, "User is banned.", Toast.LENGTH_SHORT).show();
                    }else {
                        mLoginProgress.setTitle("Logging in");
                        mLoginProgress.setMessage("Please wait while we verify your account");
                        mLoginProgress.setCanceledOnTouchOutside(false);
                        mLoginProgress.show();
                        login_user(email, password);
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Please enter your email/password", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
    }

    private void login_user(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            boolean isBanned = checkUserBan2(email);
                            if (!isBanned) {
                                //setup the required variables and stuff
                                FirebaseUtils.updateCurrentTelegramID();
                                FirebaseUtils.updateCurrentEmail();
                                FirebaseUtils.getMyBookmarks();

                                mLoginProgress.dismiss();
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            } else {
                                mLoginProgress.hide();
                                Toast.makeText(LoginActivity.this, "You are banned", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            mLoginProgress.hide();
                            Toast.makeText(LoginActivity.this, "Unable to log in", Toast.LENGTH_LONG).show();

                        }
                    }
        });
    }

    private boolean checkUserBan(String email){
        String email1 = email.toLowerCase();
        Log.i("cali",email1);
        boolean isBanned=false;
        for(String user : bannedUsersList){
            if(email1.equals(user)){
                isBanned=true;
                break;
            }
        }
        Log.i("cali",String.valueOf(isBanned));
        return isBanned;
    }

    private boolean checkUserBan2(String userEmail) {
        Log.i("banned", "checking ban now");
        Log.i("banned", "checking "+userEmail);
        boolean isBanned = false;
        for (String user : FirebaseUtils.bannedUsersList) {
            if(userEmail.equals(user)) {
                isBanned = true;
                break;
            }
        }
        Log.i("banned", String.valueOf(isBanned));
        return isBanned;
    }
}