package com.example.env;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private SignInButton signInButton;
    //private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    //private Button btnSignOut;
    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = findViewById(R.id.sign_in_button);
        //btnSignOut = findViewById(R.id.sign_out_button);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("498775728967-sklolm1bbumf00pulesrc4dojfqle4qf.apps.googleusercontent.com")//.requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null || mAuth.getCurrentUser() != null){
            startActivity(new Intent(this, MainActivity.class));
            Toast.makeText(this, "User is logged in already", Toast.LENGTH_SHORT).show();
        }

        signInButton.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                }
                //new Intent();
                //Intent intent = new Intent(LoginActivity.this, MainActivity.class); //uncomment this to go to main activity
                //startActivity(intent);

        });

        //btnSignOut.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //mGoogleSignInClient.signOut();
                //Toast.makeText(LoginActivity.this, "You are Logged Out",Toast.LENGTH_SHORT).show();
                //btnSignOut.setVisibility(View.INVISIBLE);

            //}
        //});

    }
    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if(requestCode == RC_SIGN_IN){
        // The task returned from this call is always completed, no need to attach a listener
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
        //handleSignInResult(task);
        //startActivity(new Intent(LoginActivity.this, MainActivity.class));
        //finish();
            try {
                GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);
                //Toast.makeText(this,"Your google account is connected to our application.", Toast.LENGTH_SHORT).show();

                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAcc.getIdToken(),null);
                mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(), "Your Google Account is Connected to our application", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }

    }
}
