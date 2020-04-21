package com.example.env.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.env.FirebaseUtils;
import com.example.env.Listing;
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
        String user = extras.getString("USER");
        String email = extras.getString("EMAIL");
        final String telegramID = extras.getString("TELEGRAMID");
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
                DatabaseReference toDelete = mDatabase.child("testProducts").child(String.valueOf(id));
                Log.d("Own_listing", String.valueOf(id));

                for (Listing i : FirebaseUtils.myUserListings.userListings) {
                    if (i.getId() == id) {
                        FirebaseUtils.myUserListings.removeListing(i);
                        break;
                    }
                }

                for (Listing i : HomeFragment.homeUserListings.userListings) {
                    if (i.getId() == id) {
                        HomeFragment.homeUserListings.removeListing(i);
                        break;
                    }
                }

                toDelete.removeValue();
                Log.d("OWN_LISTING_TAG", "delete complete");

                Toast.makeText(ViewOwnListing.this, "Listing deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewOwnListing.this, MainActivity.class);
                startActivity(intent);
            }

        });

    }

    @Override
    public boolean onSupportNavigateUp() { //make back button go back to home fragment
        Intent intent = new Intent(ViewOwnListing.this, MainActivity.class);
        startActivity(intent);
        return true;
    }

}
