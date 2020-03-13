package com.example.env.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.env.MainActivity;
import com.example.env.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.env.R;

public class ViewOwnListing extends AppCompatActivity {

    ImageView listingImage;
    TextView listingTitle;
    TextView listingPrice;
    TextView listingCategory;
    TextView listingDescription;
    Button editListingButton;
    Button deleteListingButton;

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

        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        String title = extras.getString("TITLE");
        listingTitle.setText(title);
        String price = extras.getString("PRICE");
        listingPrice.setText(price);
        String category = extras.getString("CATEGORY");
        listingCategory.setText(category);
        String description = extras.getString("DESCRIPTION");
        listingDescription.setText(description);
        byte[] byteArray = extras.getByteArray("IMAGE");
        Bitmap image = Utils.byteArrayToBitmap(byteArray);
        listingImage.setImageBitmap(image);

        setTitle(title); //title of the activity is set to title of listing


        editListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOwnListing.this,EditListing.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        deleteListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
