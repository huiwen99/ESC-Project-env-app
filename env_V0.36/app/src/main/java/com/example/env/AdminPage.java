package com.example.env;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.env.ui.home.ViewOwnListing;

import java.util.ArrayList;

public class AdminPage extends AppCompatActivity {

    TextView bannedWordsList;
    Button banWordsButton;
    TextView bannedUsersList;
    Button banUsersButton;
    Button banListingsButton;
    EditText wordInput;

    ArrayList<String> bannedWords;
    ArrayList<String> bannedUsers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        Toolbar toolbar = findViewById(R.id.toolbarAdmin);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        bannedWords = extras.getStringArrayList("BANNED_WORDS");

        

        bannedWordsList = findViewById(R.id.bannedWordsList);
        banWordsButton = findViewById(R.id.banWordsButton);
        bannedUsersList = findViewById(R.id.bannedUsersList);
        banUsersButton = findViewById(R.id.banUsersButton);
        banListingsButton = findViewById(R.id.banListingsButton);
        wordInput = findViewById(R.id.wordInput);

        bannedWordsList.setText(listToString(bannedWords));
        bannedUsersList.setText(listToString(bannedUsers));

        banWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = wordInput.getText().toString();
                if(!input.equals("")){
                    if(!bannedWords.contains(input)){
                        //to push to firebase, maybe dont need to pull here, just pull in home fragment and i'll pass it through intent
                        bannedWords.add(input);
                        bannedWordsList.setText(listToString(bannedWords));
                        Toast.makeText(AdminPage.this, "Added word to the banned list.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AdminPage.this, "Word is already banned.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AdminPage.this, "Nothing to ban.", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public boolean onSupportNavigateUp() { //make back button go back to home fragment
        Intent intent = new Intent(AdminPage.this,MainActivity.class);
        startActivity(intent);
        return true;
    }


    private String listToString(ArrayList<String> list){
        String newString = "";
        for(String s : list){
            newString+=s;
            newString+="\n";
        }
        return newString;
    }
}
