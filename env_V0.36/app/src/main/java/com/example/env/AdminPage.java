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
    EditText userInput;
    EditText listingInput;

    ArrayList<String> bannedWords;
    ArrayList<String> bannedUsers = new ArrayList<String>(); //to pull from firebase


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
        userInput = findViewById(R.id.userInput);
        listingInput = findViewById(R.id.listingInput);

        bannedUsers = FirebaseUtils.bannedUsersList;

        bannedWordsList.setText(listToString(bannedWords));
        bannedUsersList.setText(listToString(bannedUsers));



        banWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = wordInput.getText().toString().trim();
                if(!input.equals("")){
                    if(!bannedWords.contains(input)){
                        FirebaseUtils.addBannedWord(input);
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

        banUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = userInput.getText().toString().trim();
                if(!input.equals("")){
                    if(!bannedUsers.contains(input)){
                        //to push to firebase
                        bannedUsers.add(input);
                        bannedUsersList.setText(listToString(bannedUsers));
                        FirebaseUtils.addBannedUser(input);
                        Toast.makeText(AdminPage.this, "Added user to the banned list.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AdminPage.this, "User is already banned.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AdminPage.this, "Nothing to ban.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        banListingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = listingInput.getText().toString().trim();
                if(!input.equals("")){
                    if(true){//replace to check if listing exists or something
                        //add code to delete listing based on listing ID
                        FirebaseUtils.deleteLisitng(input);

                        Toast.makeText(AdminPage.this, "Listing has been deleted.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AdminPage.this, "No such listing ID.", Toast.LENGTH_SHORT).show();
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
