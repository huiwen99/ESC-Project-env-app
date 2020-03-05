package com.example.env;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.graphics.ImageDecoder.createSource;

public class AddListing extends AppCompatActivity {

    ImageView imageSelected;
    EditText newListingTitle;
    EditText newListingPrice;
    Spinner newListingCategory;
    EditText newListingDescription;
    Button addNewListing;

    Bitmap bitmap = null;

    public final static int REQUEST_IMAGE_GET = 2000;
    public final static String KEY_TITLE = "Title";
    public final static String KEY_PRICE = "Price";
    public final static String KEY_CATEGORY = "Category";
    public final static String KEY_DESCRIPTION = "Description";
    public final static String KEY_IMAGE = "Image";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageSelected = findViewById(R.id.imageSelected);
        newListingTitle = findViewById(R.id.newListingTitle);
        newListingPrice = findViewById(R.id.newListingPrice);
        newListingCategory = findViewById(R.id.newListingCateogry);
        newListingDescription = findViewById(R.id.newListingDescription);
        addNewListing = findViewById(R.id.addNewListing);

        imageSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }

            }
        });

        addNewListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resultCode = Activity.RESULT_OK;
                Intent resultIntent = new Intent();

                String title = newListingTitle.getText().toString();
                String price = newListingPrice.getText().toString();
                String category = newListingCategory.getSelectedItem().toString();
                String description = newListingDescription.getText().toString();


                if(bitmap==null){
                    Toast.makeText(AddListing.this, "Please choose a photo", Toast.LENGTH_SHORT).show();
                }else if(title.equals("")||price.equals("")||description.equals("")){
                    Toast.makeText(AddListing.this, "Please fill in the blanks", Toast.LENGTH_SHORT).show();
                }else{

                    resultIntent.putExtra(KEY_TITLE, title);
                    resultIntent.putExtra(KEY_PRICE, price);
                    resultIntent.putExtra(KEY_CATEGORY, category);
                    resultIntent.putExtra(KEY_DESCRIPTION, description);

                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                    byte[] byteArray = bStream.toByteArray();

                    resultIntent.putExtra(KEY_IMAGE, byteArray);

                    setResult(resultCode, resultIntent);
                    finish();
                }


            }
        });

    }

    //select image from files
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            imageSelected.setImageURI(fullPhotoUri);
            try{
                ParcelFileDescriptor parcelFileDescriptor =
                        getContentResolver().openFileDescriptor(fullPhotoUri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);

                parcelFileDescriptor.close();
                }
            catch(FileNotFoundException ex){
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            }
            catch(IOException ex){
                Toast.makeText(this, "IO Exception", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
