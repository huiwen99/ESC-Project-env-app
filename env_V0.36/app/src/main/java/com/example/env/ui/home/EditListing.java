package com.example.env.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.env.AddListing;
import com.example.env.CategoryAdapter;
import com.example.env.CategoryItem;
import com.example.env.FirebaseUtils;
import com.example.env.Listing;
import com.example.env.R;
import com.example.env.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.env.AddListing.REQUEST_IMAGE_GET;

public class EditListing extends AppCompatActivity {

    private ArrayList<CategoryItem> mCategoryItemForEditListing;
    private CategoryAdapter mAdapterForEditListing;

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

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    // for Firebase Storage
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    static StorageReference storageRef = storage.getReferenceFromUrl("gs://envfirebaseproject.appspot.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //custom spinner
        initListForEditListing();

        editCategory = findViewById(R.id.editCategory);

        mAdapterForEditListing = new CategoryAdapter(this, mCategoryItemForEditListing);
        editCategory.setAdapter(mAdapterForEditListing);

        editImage = findViewById(R.id.editImage);
        editTitle = findViewById(R.id.editTitle);
        editPrice = findViewById(R.id.editPrice);
        editDescription = findViewById(R.id.editDescription);
        editListing = findViewById(R.id.editListing);

        Intent intent = getIntent();
        extras = intent.getExtras();
        title = extras.getString("TITLE");
        editTitle.setText(title);
        price = extras.getString("PRICE");
        final String oldPrice = price;
        price = Listing.priceTextToNumString(price);
        editPrice.setText(price);
        category = extras.getString("CATEGORY");

        final long id = extras.getLong("ID");
        String user = extras.getString("USER");
        String email = extras.getString("EMAIL");
        final String telegramID = extras.getString("TELEGRAMID");



        //NOTE: might need
        final String oldTitle = title;



/*        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCategory.setAdapter(adapter);
        
        if (category != null) {
            int spinnerPosition = mAdapterForEditListing.getPosition(category);
            editCategory.setSelection(spinnerPosition);
        }
*/
//local variable to find previous spinner position
        int tempPos = 0, spinnerPosition = 0;
        if (category != null) {
            // fetch element of the list with all category names and iamges
            for (CategoryItem item : mCategoryItemForEditListing) {
                if (item.getCategoryName().equals(category)) {
                    spinnerPosition = tempPos;
                }
                tempPos++;
            }
            editCategory.setSelection(spinnerPosition);
        }

        description = extras.getString("DESCRIPTION");
        final String oldDescription = description;
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

//                    // delete old listing, reuse this for delete button also
//                    ValueEventListener postListener = new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            // Get object and use the values to update the UI
//                            Log.d("EDIT_TAG", "getting values");
//                            Object allListing = dataSnapshot.getValue();
//                            // ...
//                            HashMap allListingHashmap = ((HashMap) allListing); // cast this bitch into a hashmap
//                            //System.out.println(allListingHashmap);
//
//                            for (Object item : allListingHashmap.values()) {
//                                final HashMap itemHashmap = ((HashMap) item); // this is the hashmap of each item
//                                System.out.println(itemHashmap);
//                                String cloudTitle = (String) itemHashmap.get("title");
//                                String cloudPrice = (String) itemHashmap.get("price");
//                                String cloudDescription = (String) itemHashmap.get("description");
//                                Log.d("EDIT_TAG", "Cloud: " + cloudTitle +" "+ cloudPrice +" "+ cloudDescription);
//                                Log.d("EDIT_TAG", "Old: " + oldTitle +" "+ oldPrice +" "+ oldDescription);
//
//                                if (cloudTitle.equals(oldTitle) && cloudDescription.equals(oldDescription)
//                                && cloudPrice.equals(oldPrice)) {
//                                    DatabaseReference toDelete = mDatabase.child("testProducts").child((String) itemHashmap.get("imgNumber"));
//                                    toDelete.removeValue();
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            // Getting Post failed, log a message
//                            Log.w("HOME_TAG", "loadPost:onCancelled", databaseError.toException());
//                            // ...
//                        }
//                    };
//                    mDatabase.child("testProducts").addListenerForSingleValueEvent(postListener);
//
//                    //end of workaround

                    title = editTitle.getText().toString();
                    price = editPrice.getText().toString();
                    price = Listing.numInputToPriceText(price);
                    category = mCategoryItemForEditListing.get(editCategory.getSelectedItemPosition()).getCategoryName();
                    description = editDescription.getText().toString();

                    extras.putString("TITLE",title);
                    extras.putString("PRICE",price);
                    extras.putString("CATEGORY",category);
                    extras.putString("DESCRIPTION",description);

                    byte[] byteArray = Utils.bitmapToByteArray(bitmap);

                    extras.putByteArray("IMAGE",byteArray);

                    //push this to firebase
                    long listingTimestamp = id;
                    try {
                        Log.d("EDIT_TAG", "prepare to push listing");
                        FirebaseUtils.editListing(listingTimestamp, title, price.substring(1), byteArray, category, description, currentUser);
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
        super.onActivityResult(requestCode, resultCode, data);
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
    private void initListForEditListing() { //method to populate array list for custom spinner
        mCategoryItemForEditListing = new ArrayList<>();
        mCategoryItemForEditListing.add(new CategoryItem("General", R.drawable.general));
        mCategoryItemForEditListing.add(new CategoryItem("Acrylic", R.drawable.acrylic));
        mCategoryItemForEditListing.add(new CategoryItem("Arts and Crafts", R.drawable.artsandcrafts));
        mCategoryItemForEditListing.add(new CategoryItem("Adhesives", R.drawable.adhesives));
        mCategoryItemForEditListing.add(new CategoryItem("Cables and Wires", R.drawable.cablesandwires));
        mCategoryItemForEditListing.add(new CategoryItem("Electronics", R.drawable.electronics));
        mCategoryItemForEditListing.add(new CategoryItem("Events", R.drawable.events));
        mCategoryItemForEditListing.add(new CategoryItem("Hardware", R.drawable.hardware));
        mCategoryItemForEditListing.add(new CategoryItem("Lighting", R.drawable.lighting));
        mCategoryItemForEditListing.add(new CategoryItem("Microelectronics", R.drawable.microelectronics));
        mCategoryItemForEditListing.add(new CategoryItem("Robotic Mechanical", R.drawable.roboticmechanical));
        mCategoryItemForEditListing.add(new CategoryItem("Sealants and Tapes", R.drawable.sealantsandtape));
        mCategoryItemForEditListing.add(new CategoryItem("Software", R.drawable.software));
        mCategoryItemForEditListing.add(new CategoryItem("Tools", R.drawable.tools));
        mCategoryItemForEditListing.add(new CategoryItem("Wood", R.drawable.wood));
    }


}
