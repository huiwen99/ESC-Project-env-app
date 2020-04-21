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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.ArrayList;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;

import static android.graphics.ImageDecoder.createSource;

public class AddListing extends AppCompatActivity {
    //variables declarations for custom spinner in AddListing
    private ArrayList<CategoryItem> mCategoryItemForAddListing;
    private CategoryAdapter mAdapterForAddListing;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    //NOTE: wtf is this declaration correct im not sure - Dan

    private String TAG = "ListingDebug";

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

    private ArrayList<String> bannedWordsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //custom spinner
        initListForAddListing();
        newListingCategory = findViewById(R.id.newListingCategory);

        mAdapterForAddListing = new CategoryAdapter(this, mCategoryItemForAddListing);
        newListingCategory.setAdapter(mAdapterForAddListing);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        bannedWordsList = intent.getStringArrayListExtra("BANNED_WORDS");


        imageSelected = findViewById(R.id.imageSelected);
        newListingTitle = findViewById(R.id.newListingTitle);
        newListingPrice = findViewById(R.id.newListingPrice);
        newListingDescription = findViewById(R.id.newListingDescription);
        addNewListing = findViewById(R.id.addNewListing);

        //newListingUser = findViewById(R.id.newListingUser);

        newListingCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryItem clickedItemForAddListing = (CategoryItem) parent.getItemAtPosition(position);
                String clickedCategoryNameForAddListing = clickedItemForAddListing.getCategoryName();
                Toast.makeText(AddListing.this, clickedCategoryNameForAddListing + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                String category = mCategoryItemForAddListing.get(newListingCategory.getSelectedItemPosition()).getCategoryName();
                String description = newListingDescription.getText().toString();
                //String user = newListingUser.getText().toString();



                if(bitmap==null){
                    Toast.makeText(AddListing.this, "Please choose a photo", Toast.LENGTH_SHORT).show();
                }else
                    if(title.equals("")||price.equals("")||description.equals("")){
                    Toast.makeText(AddListing.this, "Please fill in the blanks", Toast.LENGTH_SHORT).show();
                }else if(checkForBannedWords(title)||checkForBannedWords(price)||checkForBannedWords(description)){
                        Toast.makeText(AddListing.this, "Error: Listing details contains banned words.", Toast.LENGTH_SHORT).show();
                    } else{

                    resultIntent.putExtra(KEY_TITLE, title);
                    resultIntent.putExtra(KEY_PRICE, price);
                    resultIntent.putExtra(KEY_CATEGORY, category);
                    resultIntent.putExtra(KEY_DESCRIPTION, description);
                    //resultIntent.putExtra(KEY_USER, user);
//
                    byte[] byteArray = Utils.bitmapToByteArray(bitmap);
//
                    resultIntent.putExtra(KEY_IMAGE, byteArray);

                    long listingTimestamp = System.currentTimeMillis();

                        try {
                            FirebaseUtils.pushListing(listingTimestamp, title, price, byteArray, category, description, currentUser);
                            FirebaseUtils.getAllListings();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        setResult(resultCode, resultIntent);
                    finish();
//                    Intent intent = new Intent(AddListing.this,MainActivity.class);
//                    startActivity(intent);
                }


            }
        });

    }

    // TODO: upload the info onto firebase
    // THIS IS A TEST
    // REPLACE LATER WITH PROPER DATA
//    private void pushListing(String title, String price, byte[] imageHex, String category,String description) {
//        long listingTimestamp = System.currentTimeMillis();
//        Log.d(TAG, "Entering pushListing function");
//        String imageHexString = new String(Hex.encodeHex(imageHex));
//        Log.d(TAG, "Converted to hex string");
//        ListingForDatabase listing = new ListingForDatabase(title, price, imageHexString, category, description);
//        mDatabase.child("testProducts").child(String.valueOf(listingTimestamp)).setValue(listing);
//
//    }

    //select image from files

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
//            Uri fullPhotoUri = data.getData();
            Uri fullPhotoUri=null;
            Bundle extras = data.getExtras();
            if(extras==null || !extras.containsKey(KEY_IMAGE)){
                fullPhotoUri = data.getData();
            }else {
                fullPhotoUri = (Uri) extras.get(KEY_IMAGE);
            }
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

    private boolean checkForBannedWords(String s){
        String s1 = s.toLowerCase();
        boolean hasBannedWord=false;
        for(String word : bannedWordsList){
            if(s1.contains(word)){
                hasBannedWord=true;
                break;
            }
        }
        return hasBannedWord;
    }

    private void initListForAddListing() { //method to populate array list for custom spinner
        mCategoryItemForAddListing = new ArrayList<>();
        mCategoryItemForAddListing.add(new CategoryItem("General", R.drawable.general));
        mCategoryItemForAddListing.add(new CategoryItem("Acrylic", R.drawable.acrylic));
        mCategoryItemForAddListing.add(new CategoryItem("Arts and Crafts", R.drawable.artsandcrafts));
        mCategoryItemForAddListing.add(new CategoryItem("Adhesives", R.drawable.adhesives));
        mCategoryItemForAddListing.add(new CategoryItem("Cables and Wires", R.drawable.cablesandwires));
        mCategoryItemForAddListing.add(new CategoryItem("Electronics", R.drawable.electronics));
        mCategoryItemForAddListing.add(new CategoryItem("Events", R.drawable.events));
        mCategoryItemForAddListing.add(new CategoryItem("Hardware", R.drawable.hardware));
        mCategoryItemForAddListing.add(new CategoryItem("Lighting", R.drawable.lighting));
        mCategoryItemForAddListing.add(new CategoryItem("Microelectronics", R.drawable.microelectronics));
        mCategoryItemForAddListing.add(new CategoryItem("Robotic Mechanical", R.drawable.roboticmechanical));
        mCategoryItemForAddListing.add(new CategoryItem("Sealants and Tapes", R.drawable.sealantsandtape));
        mCategoryItemForAddListing.add(new CategoryItem("Software", R.drawable.software));
        mCategoryItemForAddListing.add(new CategoryItem("Tools", R.drawable.tools));
        mCategoryItemForAddListing.add(new CategoryItem("Wood", R.drawable.wood));
    }

}
