package com.example.env.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.env.AddListing;
import com.example.env.Listing;
import com.example.env.ListingAdapter;
import com.example.env.MainActivity;
import com.example.env.R;
import com.example.env.RecyclerViewItemListener;
import com.example.env.User;
import com.example.env.UserListings;
import com.example.env.Utils;
import com.example.env.ui.home.ViewOwnListing;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class DashboardFragment extends Fragment implements RecyclerViewItemListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    RecyclerView otherListingRecyclerView;
    ListingAdapter listingAdapter;
    UserListings masterListings; //to pull from firebase, list of all existing listings
    UserListings filteredList; // a copy of masterListings
    UserListings categoryList;
    Spinner categorySpinner;

    private SearchView searchView;
    private String searchText;

    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    // for Firebase Storage
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    static StorageReference storageRef = storage.getReferenceFromUrl("gs://envfirebaseproject.appspot.com/");

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //setHasOptionsMenu(true);

        ((MainActivity) getActivity()).hideButton();

        Context context = container.getContext();
        categorySpinner = root.findViewById(R.id.categorySpinner);

        otherListingRecyclerView = root.findViewById(R.id.otherListingRecyclerView);

        ArrayList<Integer> drawableId = new ArrayList<Integer>();
            drawableId.add(R.drawable.fan);
            drawableId.add(R.drawable.peltierchip);
            drawableId.add(R.drawable.threedprinter);
            drawableId.add(R.drawable.battery);
            drawableId.add(R.drawable.plywood);
            masterListings = new UserListings();
            filteredList = new UserListings();
            for(Integer rid:drawableId){
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), rid);
                String imageName = context.getResources().getResourceEntryName(rid);
                String price = "5";
                String category = "General";
                if(rid%2==0){
                    category = "Microelectronics";
                }else if(rid%2==1){
                    category = "Robotic Mechanical";
                }
                String description = "test";
                String user = "env@gmail.com";
            masterListings.addListing(imageName,price,bitmap, category, description, user);
            filteredList.addListing(imageName,price,bitmap, category, description, user);
        }

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

                            Log.d("HOME_TAG", currentUser);
                            Log.d("HOME_TAG", user);
                            Log.d("HOME_TAG", String.valueOf(currentUser.equals(user)));


                            masterListings.addListing(imageName, price, imgBitmap, category, description, user);
                            filteredList.addListing(imageName, price, imgBitmap, category, description, user);

                            Log.d("HOME_TAG", "added item");
                            //Log.d("HOME_TAG", String.valueOf(userListings.userListings));
                            refreshRecyclerView(filteredList);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("HOME_TAG", String.valueOf(exception));
                        }
                    });

                }
                refreshRecyclerView(filteredList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("HOME_TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("testProducts").addListenerForSingleValueEvent(postListener);


        listingAdapter = new ListingAdapter(context, filteredList, this);
        otherListingRecyclerView.setAdapter(listingAdapter);
        otherListingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { //display all listings if category is "General"
                    defaultListings();
                } else {
                    String category = categorySpinner.getSelectedItem().toString();
                    getCategorizedListing(category);
                    getSearchFilteredListing();
                    refreshRecyclerView(filteredList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                defaultListings();
            }
        });

        return root;
    }



    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getActivity(), ViewOtherListing.class);

        Bundle extras = new Bundle();

        extras.putString("TITLE",filteredList.getTitle(position));
        extras.putString("PRICE",filteredList.getPrice(position));
        extras.putString("CATEGORY",filteredList.getCategory(position));
        extras.putString("DESCRIPTION",filteredList.getDescription(position));
        extras.putString("USER",filteredList.getUser(position));

        Bitmap image = filteredList.getImage(position);
        byte[] byteArray = Utils.bitmapToByteArray(image);

        extras.putByteArray("IMAGE",byteArray);

        intent.putExtras(extras);

        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //show search bar
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search Listing");
        searchText = searchView.getQuery().toString();

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchText = newText;
        getCategorizedListing(categorySpinner.getSelectedItem().toString());
        getSearchFilteredListing();
        refreshRecyclerView(filteredList);

        if(newText == null || newText.trim().isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    private void defaultListings(){
        for (Listing item : masterListings.userListings) {
            filteredList.addListing(item);
        }
        refreshRecyclerView(filteredList);
    }

    private void getCategorizedListing(String category){
        categoryList = new UserListings();
        if(category.equals("General")){
            for (Listing item : masterListings.userListings) {
                categoryList.addListing(item);
            }
        }else {
            for (Listing item : masterListings.userListings) {
                if (item.getCategory().equals(category)) {
                    categoryList.addListing(item);
                }
            }
        }
    }

    private void getSearchFilteredListing(){
        filteredList = new UserListings();
        if (searchText == null || searchText.trim().isEmpty()){ //if nothing is entered in the search bar, it should show all listings of that category
            for (Listing item : categoryList.userListings){
                filteredList.addListing(item);
            }
        }else {
            for (Listing item : categoryList.userListings) {
                if (item.getTitle().toLowerCase().contains(searchText.toLowerCase()) || item.getDescription().toLowerCase().contains(searchText.toLowerCase())) { // if search bar query contains characters same as the title of the listing
                    filteredList.addListing(item);
                }
            }
        }
    }

    private void refreshRecyclerView(UserListings listings){
        ViewGroup container = (ViewGroup) getView().getParent();
        Context context = container.getContext();
        listingAdapter = new ListingAdapter(context, listings, this);
        otherListingRecyclerView.setAdapter(listingAdapter);
        otherListingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listingAdapter.notifyDataSetChanged();
    }

    }

