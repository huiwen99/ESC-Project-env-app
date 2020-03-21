package com.example.env.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.env.R;
import com.example.env.Utils;

public class ViewOtherListing extends AppCompatActivity {

    ImageView otherListingImage;
    TextView otherListingTitle;
    TextView otherListingPrice;
    TextView otherListingCategory;
    TextView otherListingDescriptionText;
    TextView otherListingDescription;
    Button chatButton;
    TextView otherListingEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        otherListingImage = findViewById(R.id.otherListingImage);
        otherListingTitle = findViewById(R.id.otherListingTitle);
        otherListingPrice = findViewById(R.id.otherListingPrice);
        otherListingCategory = findViewById(R.id.otherListingCategory);
        otherListingDescriptionText = findViewById(R.id.otherListingDescriptionText);
        otherListingDescription = findViewById(R.id.otherListingDescription);
        chatButton = findViewById(R.id.chatButton);
        otherListingEmail = findViewById(R.id.otherListingEmail);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        String title = extras.getString("TITLE");
        otherListingTitle.setText(title);
        String price = extras.getString("PRICE");
        otherListingPrice.setText(price);
        String category = extras.getString("CATEGORY");
        otherListingCategory.setText(category);
        String description = extras.getString("DESCRIPTION");
        otherListingDescription.setText(description);
        byte[] byteArray = extras.getByteArray("IMAGE");
        Bitmap image = Utils.byteArrayToBitmap(byteArray);
        otherListingImage.setImageBitmap(image);
        String user = extras.getString("User");
        otherListingEmail.setText(user);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_other_listing);
    }
}
