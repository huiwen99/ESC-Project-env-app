package com.example.env;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class StartScreen extends AppCompatActivity {

    private static final long SPLASH_SCREEN = 5000 ; //5s
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

        //5s to navigate to login activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartScreen.this, LoginActivity.class);
                startActivity(intent);
                finish(); //if do not finish, when the user presses the back button in login activity, it will go back to splash screen which is what we dont want.
            }
        },SPLASH_SCREEN);

    }
}
