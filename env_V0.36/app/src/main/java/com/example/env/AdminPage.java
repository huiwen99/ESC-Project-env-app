package com.example.env;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.env.ui.home.ViewOwnListing;

import java.util.ArrayList;

public class AdminPage extends AppCompatActivity {

    TextView bannedWordsList;
    Button banWordsButton;
    TextView bannedUsersList;
    Button banUsersButton;
    Button banListingsButton;

    ArrayList<String> bannedWords = new ArrayList<String>();
    ArrayList<String> bannedUsers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        Toolbar toolbar = findViewById(R.id.toolbarAdmin);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //to pull from firebase?
        bannedWords.add("damn");
        bannedWords.add("fuck");
        bannedWords.add("bitch");
        bannedWords.add("bastard");

        bannedWordsList = findViewById(R.id.bannedWordsList);
        banWordsButton = findViewById(R.id.banWordsButton);
        bannedUsersList = findViewById(R.id.bannedUsersList);
        banUsersButton = findViewById(R.id.banUsersButton);
        banListingsButton = findViewById(R.id.banListingsButton);

        bannedWordsList.setText(listToString());

        


    }

    @Override
    public boolean onSupportNavigateUp() { //make back button go back to home fragment
        Intent intent = new Intent(AdminPage.this,MainActivity.class);
        startActivity(intent);
        return true;
    }


    private String listToString(){
        String newString = "";
        for(String s : bannedWords){
            newString+=s;
            newString+="\n";
        }
        return newString;
    }
}
