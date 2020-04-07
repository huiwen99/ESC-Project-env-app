package com.example.env.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.env.MainActivity;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_other_listing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        otherListingImage = findViewById(R.id.otherListingImage);
        otherListingTitle = findViewById(R.id.otherListingTitle);
        otherListingPrice = findViewById(R.id.otherListingPrice);
        otherListingCategory = findViewById(R.id.otherListingCategory);
        otherListingDescriptionText = findViewById(R.id.otherListingDescriptionText);
        otherListingDescription = findViewById(R.id.otherListingDescription);
        chatButton = findViewById(R.id.chatButton);
        otherListingEmail = findViewById(R.id.otherListingEmail);


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
        String user = extras.getString("USER");
        otherListingEmail.setText(user);

        setTitle(title+" by "+user);

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://t.me/HeizerSpider";
//              string url will be changed to a dynamic url, concatenating the telegram username(from firebase) at the end of "https://t.me/"
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
