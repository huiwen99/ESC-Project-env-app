package com.example.env.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.env.AddListing;
import com.example.env.Listing;
import com.example.env.ListingAdapter;
import com.example.env.MainActivity;
import com.example.env.R;
import com.example.env.RecyclerViewItemListener;
import com.example.env.UserListings;
import com.example.env.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment implements RecyclerViewItemListener {

    private HomeViewModel homeViewModel;

    RecyclerView recyclerView;
    ListingAdapter listingAdapter;
    ImageButton addListingButton;
    UserListings userListings;
    TextView username;
    private LinearLayout linearLayout;

    final int REQUEST_CODE_IMAGE = 1000;

    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    // for Firebase Storage
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    static StorageReference storageRef = storage.getReferenceFromUrl("gs://envfirebaseproject.appspot.com/");

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });



        ((MainActivity) getActivity()).showButton();

        Context context = container.getContext();

        addListingButton = root.findViewById(R.id.addListingButton);
        username = root.findViewById(R.id.username);
        recyclerView = root.findViewById(R.id.userListingRecyclerView);
        linearLayout = root.findViewById(R.id.linearLayout);



        //adding to userListings
        ArrayList<Integer> drawableId = new ArrayList<Integer>();
        drawableId.add(R.drawable.fan);
        userListings = new UserListings();



        //TODO: display this info

//        for(Integer rid:drawableId){
//            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), rid);
//            String imageName = context.getResources().getResourceEntryName(rid);
//            String price = "5";
//            String category = "General";
//            String description = "test";
//            String user = "env@gmail.com";
//            userListings.addListing(imageName,price,bitmap, category, description, user);
//            Log.d("HOME_TAG", String.valueOf(userListings.userListings));
//        }

        //collect listings from firebase
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get object and use the values to update the UI
                Log.d("HOME_TAG", "getting value");
                Object allListing = dataSnapshot.getValue();
                // ...
                HashMap allListingHashmap = ((HashMap) allListing); // cast this bitch into a hashmap
                System.out.println(allListingHashmap);

                for (Object item : allListingHashmap.values()) {
                    final HashMap itemHashmap = ((HashMap) item); // this is the hashmap of each item
                    System.out.println(itemHashmap);

                    String imageName = String.format("%s.jpg", itemHashmap.get("imgNumber").toString());
                    StorageReference imageref = storageRef.child(imageName);

                    imageref.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap imgBitmap = Utils.byteArrayToBitmap(bytes);
                            String imageName = itemHashmap.get("title").toString();
                            String price = itemHashmap.get("price").toString().substring(1);
                            String category = itemHashmap.get("category").toString();
                            String description = itemHashmap.get("description").toString();
                            String user = itemHashmap.get("user").toString();

                            //to change this to actual listing id
                            long id = 1010;

                            Log.d("HOME_TAG", currentUser);
                            Log.d("HOME_TAG", user);
                            Log.d("HOME_TAG", String.valueOf(currentUser.equals(user)));


                            if (user.equals(currentUser)) {
                                userListings.addListing(imageName, price, imgBitmap, category, description, user, id);
                                Log.d("HOME_TAG", "added item");
                                //Log.d("HOME_TAG", String.valueOf(userListings.userListings));
                                refreshRecyclerView();
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("HOME_TAG", String.valueOf(exception));
                        }
                    });

                }
                refreshRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("HOME_TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("testProducts").addListenerForSingleValueEvent(postListener);


        //initializing recyclerview
//        listingAdapter = new ListingAdapter(context, userListings, this);
//        recyclerView.setAdapter(listingAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        addListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddListing.class);
//                startActivityForResult(intent, REQUEST_CODE_IMAGE);
                startActivity(intent);
            }
        });

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), AdminPage.class);
//                startActivity(intent);
            }
        });


        return root;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == REQUEST_CODE_IMAGE && resultCode== Activity.RESULT_OK){
//            String title = data.getStringExtra(AddListing.KEY_TITLE);
//            String price = data.getStringExtra(AddListing.KEY_PRICE);
//            String category = data.getStringExtra(AddListing.KEY_CATEGORY);
//            String description = data.getStringExtra(AddListing.KEY_DESCRIPTION);
//            String user = data.getStringExtra(AddListing.KEY_USER);
//
//            //replace this -> get current time or something?? lmk if you wanna pass id through AddListing -hw
//            long id = 0;
//
//            byte[] byteArray = data.getByteArrayExtra(AddListing.KEY_IMAGE);
//            Bitmap image = Utils.byteArrayToBitmap(byteArray);
//
//            userListings.addListing(title,price,image,category,description,user,id);
//            Log.d("HOME_TAG", String.valueOf(userListings.userListings));
//            listingAdapter.notifyDataSetChanged();
//        }
//    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getActivity(),ViewOwnListing.class);

        Bundle extras = new Bundle();

        extras.putString("TITLE",userListings.getTitle(position));
        extras.putString("PRICE",userListings.getPrice(position));
        extras.putString("CATEGORY",userListings.getCategory(position));
        extras.putString("DESCRIPTION",userListings.getDescription(position));
        extras.putLong("ID",userListings.getId(position));


        Bitmap image = userListings.getImage(position);
        byte[] byteArray = Utils.bitmapToByteArray(image);

        extras.putByteArray("IMAGE",byteArray);

        intent.putExtras(extras);

        startActivity(intent);
    }

    private void refreshRecyclerView(){
        ViewGroup container = (ViewGroup) getView().getParent();
        Context context = container.getContext();
        listingAdapter = new ListingAdapter(context, userListings, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView); //added this line for for the swipe gesture
        recyclerView.setAdapter(listingAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(gridLayoutManager);

//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listingAdapter.notifyDataSetChanged();
    }
    //swipe to delete listing method
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            String name = userListings.getTitle(viewHolder.getAdapterPosition());
            //listingAdapter.notifyDataSetChanged();

            //backup of removed item for undo purpose
            final Listing deletedItem = userListings.get(viewHolder.getAdapterPosition());
            final int position = viewHolder.getAdapterPosition();

            //removing item from recyclerview
            listingAdapter.remove(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(linearLayout, name + " Listing Removed!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listingAdapter.restore(deletedItem, position);
                    recyclerView.scrollToPosition(position);
                }
            });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
    };


}