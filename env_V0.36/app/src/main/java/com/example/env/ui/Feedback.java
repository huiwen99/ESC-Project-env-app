package com.example.env.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.env.R;
import com.google.android.material.textfield.TextInputEditText;

public class Feedback extends AppCompatActivity {

    private TextInputEditText mFeedback;
    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mFeedback = (TextInputEditText) findViewById(R.id.feedContent);
        mSubmit = (Button) findViewById(R.id.feedButton);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback = mFeedback.getText().toString();

                if(!(TextUtils.isEmpty(feedback))){
                    submit_feedback();
                }

            }

            private void submit_feedback() {
                //push up to firebase
            }
        });
    }
}
