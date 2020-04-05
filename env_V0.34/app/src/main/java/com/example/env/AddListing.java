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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
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
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Hex;

import static android.graphics.ImageDecoder.createSource;

public class AddListing extends AppCompatActivity {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    //NOTE: wtf is this declaration correct im not sure - Dan

    // for Firebase Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    StorageReference storageRef = storage.getReferenceFromUrl("gs://envfirebaseproject.appspot.com/");

    private String TAG = "ListingDebug";
    public static Uri downloadUri;

    // get user for pushing listing
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ImageView imageSelected;
    EditText newListingTitle;
    EditText newListingPrice;
    Spinner newListingCategory;
    EditText newListingDescription;
    Button addNewListing;
    //EditText newListingUser;

    Bitmap bitmap = null;

    public final static int REQUEST_IMAGE_GET = 2000;
    public final static String KEY_TITLE = "Title";
    public final static String KEY_PRICE = "Price";
    public final static String KEY_CATEGORY = "Category";
    public final static String KEY_DESCRIPTION = "Description";
    public final static String KEY_IMAGE = "Image";
    public final static String KEY_USER = "User";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("We're in AddListing");
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

        //newListingUser = findViewById(R.id.newListingUser);

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
                //String user = newListingUser.getText().toString();


                if (bitmap == null) {
                    Toast.makeText(AddListing.this, "Please choose a photo", Toast.LENGTH_SHORT).show();
                } else if (title.equals("") || price.equals("") || description.equals("")) {
                    Toast.makeText(AddListing.this, "Please fill in the blanks", Toast.LENGTH_SHORT).show();
                } else {

                    resultIntent.putExtra(KEY_TITLE, title);
                    resultIntent.putExtra(KEY_PRICE, price);
                    resultIntent.putExtra(KEY_CATEGORY, category);
                    resultIntent.putExtra(KEY_DESCRIPTION, description);
                    //resultIntent.putExtra(KEY_USER, user);

                    byte[] byteArray = Utils.bitmapToByteArray(bitmap);

                    resultIntent.putExtra(KEY_IMAGE, byteArray);

                    try {
                        FirebaseUtils.pushListing(title, price, byteArray, category, description, currentUser);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    setResult(resultCode, resultIntent);
                    finish();
                }


            }
        });

    }

    // TODO: upload the info onto firebase
    // THIS IS A TEST
    // REPLACE LATER WITH PROPER DATA
    /*private void pushListing(String title, String price, byte[] imageBytes, String category, String description) throws InterruptedException {
        // will be the item name in our DB

        long listingTimestamp = System.currentTimeMillis();
        //Log.d(TAG, "Entering pushListing function");

        String imageName = String.format("%d.jpg", listingTimestamp);
        System.out.println(imageName);

        final StorageReference imageref = storageRef.child(imageName);

        UploadTask uploadTask = imageref.putBytes(imageBytes);
        System.out.println("upload task created");
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                System.out.println("Upload failed");
                System.out.println(exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                System.out.println("Successful upload");
                System.out.println(taskSnapshot.getMetadata());
            }
        });

        System.out.println("preparing for urltask");


        //supposed to get us the URL to download this image
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Uri> task) {
                                         if (task.isSuccessful()) {

                                             System.out.println("Get Uri successful");
                                             downloadUri = task.getResult();

                                         } else {
                                             System.out.println("Get Uri failed");
                                         }

                                     }
                                 }

        );

        *//*System.out.println("supposedly done getting URL?");

        String stringURL = imageref.getDownloadUrl().getResult().toString();

        System.out.println("declared stringurl");*//*

        ListingForDatabase listing = new ListingForDatabase(title, price, category, description, currentUser);
        mDatabase.child("testProducts").child(String.valueOf(listingTimestamp)).setValue(listing);
        //String imageHexString = new String(Hex.encodeHex(imageHex));
        //Log.d(TAG, "Converted to hex string");


    }*/

    //select image from files
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            imageSelected.setImageURI(fullPhotoUri);
            try {
                bitmap = Utils.decodeUri(this, fullPhotoUri, 200, 300);
            } catch (FileNotFoundException ex) {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(this, "Exception caught", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
