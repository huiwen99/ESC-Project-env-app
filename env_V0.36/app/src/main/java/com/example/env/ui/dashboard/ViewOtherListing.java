package com.example.env.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.env.FirebaseUtils;
import com.example.env.LoginActivity;
import com.example.env.R;
import com.example.env.Utils;
import com.example.env.Feedback;

public class ViewOtherListing extends AppCompatActivity {

    ImageView otherListingImage;
    TextView otherListingTitle;
    TextView otherListingPrice;
    TextView otherListingCategory;
    TextView otherListingDescriptionText;
    TextView otherListingDescription;
    ImageButton chatButton;
    ImageButton bookmarkButton;
    ImageButton reportUserButton;
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
        bookmarkButton = findViewById(R.id.bookmarkButton);
        reportUserButton = findViewById(R.id.reportUserButton);
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

        final long id = extras.getLong("ID");
        String email = extras.getString("EMAIL");
        otherListingEmail.setText(email);
        final String telegramID = extras.getString("TELEGRAMID");
        //Log.d("TELE_ID", "viewotherlisting "+email);
        //Log.d("TELE_ID", "viewotherlisting "+telegramID);



        setTitle(title+" by "+email);

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://t.me/"+ telegramID;
                Log.d("TELE_ID", url);
//              string url will be changed to a dynamic url, concatenating the telegram username(from firebase) at the end of "https://t.me/"
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtils.addBookmark(FirebaseUtils.myCurrentUser.getUid(), id);
                Toast.makeText(getApplicationContext(), "Added to your bookmarks", Toast.LENGTH_SHORT).show();
            }
        });

        reportUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ViewOtherListing.this, Feedback.class);
                mainIntent.putExtras(extras);
                startActivity(mainIntent);
                finish();
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
