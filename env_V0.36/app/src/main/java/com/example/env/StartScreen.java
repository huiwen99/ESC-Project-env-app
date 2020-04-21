package com.example.env;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StartScreen extends AppCompatActivity {

    private static final long SPLASH_SCREEN = 4000 ; //4s to go from start screen to login screen
    //Variables
    Animation topAnim, bottomAnim;
    ImageView image; //image of the env logo
    TextView env, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getActionBar().hide();
        setContentView(R.layout.activity_start_screen);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        // for Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://envfirebaseproject.appspot.com/");

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Set ids

        image = findViewById(R.id.imageView);
        env = findViewById(R.id.textView);
        slogan = findViewById(R.id.textView2);

        image.setAnimation(topAnim);
        env.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        FirebaseUtils.getBannedUsers();
        Log.d("START_SCREEN", "got banned users, now getting emails");
        FirebaseUtils.updateCurrentEmail();
        Log.d("START_SCREEN", "got emails, now getting listings");
        FirebaseUtils.getAllListings();
        Log.d("START_SCREEN", "got listings, done");

        //4s to navigate to login activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartScreen.this, LoginActivity.class);
                Log.d("TEST1", "preparing to go to login");
                startActivity(intent);
                finish(); //if do not finish, when the user presses the back button in login activity, it will go back to splash screen which is what we dont want.
            }
        },SPLASH_SCREEN);

    }
}
