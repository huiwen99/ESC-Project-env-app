package com.example.env.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.env.AddListing;
import com.example.env.FirebaseUtils;
import com.example.env.Listing;
import com.example.env.R;
import com.example.env.Utils;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileNotFoundException;

import static com.example.env.AddListing.REQUEST_IMAGE_GET;

public class EditListing extends AppCompatActivity {

    ImageView editImage;
    EditText editTitle;
    EditText editPrice;
    Spinner editCategory;
    EditText editDescription;
    Button editListing;

    Bundle extras; //bundle for moving information across activities
    Bitmap bitmap;
    String title;
    String price;
    String category;
    String description;

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editImage = findViewById(R.id.editImage);
        editTitle = findViewById(R.id.editTitle);
        editPrice = findViewById(R.id.editPrice);
        editCategory = findViewById(R.id.editCateogry);
        editDescription = findViewById(R.id.editDescription);
        editListing = findViewById(R.id.editListing);

        Intent intent = getIntent();
        extras = intent.getExtras();
        title = extras.getString("TITLE");
        editTitle.setText(title);
        price = extras.getString("PRICE");
        price = Listing.priceTextToNumString(price);
        editPrice.setText(price);
        category = extras.getString("CATEGORY");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCategory.setAdapter(adapter);
        if (category != null) {
            int spinnerPosition = adapter.getPosition(category);
            editCategory.setSelection(spinnerPosition);
        }

        description = extras.getString("DESCRIPTION");
        editDescription.setText(description);
        byte[] byteArray = extras.getByteArray("IMAGE");
        bitmap = Utils.byteArrayToBitmap(byteArray);
        editImage.setImageBitmap(bitmap);

        setTitle("Editing: "+title); //set title of activity

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }

            }
        });


        editListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap==null){
                    Toast.makeText(EditListing.this, "Please choose a photo", Toast.LENGTH_SHORT).show();
                }else if(title.equals("")||price.equals("")||description.equals("")){
                    Toast.makeText(EditListing.this, "Please fill in the blanks", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(EditListing.this,ViewOwnListing.class);

                    title = editTitle.getText().toString();
                    price = editPrice.getText().toString();
                    price = Listing.numInputToPriceText(price);
                    category = editCategory.getSelectedItem().toString();
                    description = editDescription.getText().toString();

                    extras.putString("TITLE",title);
                    extras.putString("PRICE",price);
                    extras.putString("CATEGORY",category);
                    extras.putString("DESCRIPTION",description);

                    byte[] byteArray = Utils.bitmapToByteArray(bitmap);

                    extras.putByteArray("IMAGE",byteArray);

                    //push this to firebase
                    long listingTimestamp = System.currentTimeMillis();
                    try {
                        FirebaseUtils.pushListing(listingTimestamp, title, price, byteArray, category, description, currentUser);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    intent.putExtras(extras);
                    startActivity(intent);




                }
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //select image from files
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            editImage.setImageURI(fullPhotoUri);
            try{
                bitmap = Utils.decodeUri(this,fullPhotoUri,200,300);
            }
            catch(FileNotFoundException ex){
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            }
            catch(Exception ex){
                Toast.makeText(this, "Exception caught", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
