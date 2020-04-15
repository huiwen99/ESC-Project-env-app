package com.example.env.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.env.MainActivity;
import com.example.env.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.env.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class ViewOwnListing extends AppCompatActivity {

    ImageView listingImage;
    TextView listingTitle;
    TextView listingPrice;
    TextView listingCategory;
    TextView listingDescription;
    ImageButton editListingButton;
    ImageButton deleteListingButton;
    //TextView listingEmail;

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    // for Firebase Storage
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    static StorageReference storageRef = storage.getReferenceFromUrl("gs://envfirebaseproject.appspot.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_own_listing);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listingImage = findViewById(R.id.listingImage);
        listingTitle = findViewById(R.id.listingTitle);
        listingPrice = findViewById(R.id.listingPrice);
        listingCategory = findViewById(R.id.listingCategory);
        listingDescription = findViewById(R.id.listingDescription);
        editListingButton = findViewById(R.id.editListingButton);
        deleteListingButton = findViewById(R.id.deleteListingButton);
        //listingEmail = findViewById(R.id.listingEmail);

        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        final String title = extras.getString("TITLE");
        listingTitle.setText(title);
        final String price = extras.getString("PRICE");
        listingPrice.setText(price);
        String category = extras.getString("CATEGORY");
        listingCategory.setText(category);
        final String description = extras.getString("DESCRIPTION");
        listingDescription.setText(description);
        final long id = extras.getLong("ID");
        byte[] byteArray = extras.getByteArray("IMAGE");
        Bitmap image = Utils.byteArrayToBitmap(byteArray);
        listingImage.setImageBitmap(image);
        String user = extras.getString("User");
        //listingEmail.setText(user);

        setTitle(title); //title of the activity is set to title of listing


        editListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOwnListing.this, EditListing.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        //can delete using id maybe @dan
        deleteListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get object and use the values to update the UI
                        Log.d("EDIT_TAG", "getting values");
                        Object allListing = dataSnapshot.getValue();
                        // ...
                        HashMap allListingHashmap = ((HashMap) allListing); // cast this bitch into a hashmap
                        //System.out.println(allListingHashmap);

                        for (Object item : allListingHashmap.values()) {
                            final HashMap itemHashmap = ((HashMap) item); // this is the hashmap of each item
                            System.out.println(itemHashmap);
                            String cloudTitle = (String) itemHashmap.get("title");
                            String cloudPrice = (String) itemHashmap.get("price");
                            String cloudDescription = (String) itemHashmap.get("description");
                            Log.d("EDIT_TAG", "Cloud: " + cloudTitle +" "+ cloudPrice +" "+ cloudDescription);
                            Log.d("EDIT_TAG", "Old: " + title +" "+ price +" "+ description);

                            if (cloudTitle.equals(title) && cloudDescription.equals(description)
                                    && cloudPrice.equals(price)) {
                                DatabaseReference toDelete = mDatabase.child("testProducts").child((String) itemHashmap.get("imgNumber"));
                                toDelete.removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w("OWN_LISTING_TAG", "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                };
                mDatabase.child("testProducts").addListenerForSingleValueEvent(postListener);


            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() { //make back button go back to home fragment
        Intent intent = new Intent(ViewOwnListing.this,MainActivity.class);
        startActivity(intent);
        return true;
    }

}
