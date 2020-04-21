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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.env.AddListing;
import com.example.env.CategoryAdapter;
import com.example.env.CategoryItem;
import com.example.env.FirebaseUtils;
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
import com.google.firebase.auth.FirebaseUser;
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

    //declarations for implementing custom spinner
    private ArrayList<CategoryItem> mCategoryItem;
    private CategoryAdapter mAdapter;

    RecyclerView otherListingRecyclerView;
    ListingAdapter listingAdapter;
    UserListings masterListings; //to pull from firebase, list of all existing listings
    UserListings filteredList; // a copy of masterListings
    UserListings categoryList;
    Spinner categorySpinner;
    CategoryItem search;


    private SearchView searchView;
    private String searchText;

    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    // for Firebase Storage
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    static StorageReference storageRef = storage.getReferenceFromUrl("gs://envfirebaseproject.appspot.com/");

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        //setHasOptionsMenu(true);

        //to show custom spinner when activity is created
        initList();
        Context context = container.getContext();

        categorySpinner = root.findViewById(R.id.categorySpinner);
        //categorySpinner.setOnItemSelectedListener();
        mAdapter = new CategoryAdapter(getContext(), mCategoryItem);
        categorySpinner.setAdapter(mAdapter);


        ((MainActivity) getActivity()).hideButton();



        otherListingRecyclerView = root.findViewById(R.id.otherListingRecyclerView);

//        ArrayList<Integer> drawableId = new ArrayList<Integer>();
//            drawableId.add(R.drawable.fan);
//            drawableId.add(R.drawable.peltierchip);
//            drawableId.add(R.drawable.threedprinter);
//            drawableId.add(R.drawable.battery);
//            drawableId.add(R.drawable.plywood);
            masterListings = new UserListings();
            filteredList = new UserListings();
            //TODO: please remove hardcoded listings
//            for(Integer rid:drawableId){
//                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), rid);
//                String imageName = context.getResources().getResourceEntryName(rid);
//                String price = "5";
//                String category = "General";
//                if(rid%2==0){
//                    category = "Microelectronics";
//                }else if(rid%2==1){
//                    category = "Robotic Mechanical";
//                }
//                String description = "test";
//                String user = "env@gmail.com";
//            masterListings.addListing(imageName,price,bitmap, category, description, user);
//            filteredList.addListing(imageName,price,bitmap, category, description, user);
//        }

        //collect listings from firebase
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get object and use the values to update the UI
                Log.d("DASHBOARD_TAG", "getting value");
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
                            String email = itemHashmap.get("email").toString();
                            String telegramID = itemHashmap.get("telegramID").toString();

                            //to be replaced
                            long listingID = Long.parseLong(itemHashmap.get("imgNumber").toString());



                            masterListings.addListing(imageName, price, imgBitmap, category, description, user, listingID,
                                    email, telegramID);
                            filteredList.addListing(imageName, price, imgBitmap, category, description, user, listingID,
                                    email, telegramID);

                            Log.d("DASHBOARD_TAG", "added item");
                            //Log.d("HOME_TAG", String.valueOf(userListings.userListings));
                            refreshRecyclerView(filteredList);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("DASHBOARD_TAG", String.valueOf(exception));
                        }
                    });

                }
                refreshRecyclerView(filteredList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("DASHBOARD_TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("testProducts").addListenerForSingleValueEvent(postListener);


        listingAdapter = new ListingAdapter(context, filteredList, this);
        otherListingRecyclerView.setAdapter(listingAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        otherListingRecyclerView.setLayoutManager(gridLayoutManager);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryItem clickedItem = (CategoryItem) parent.getItemAtPosition(position);
                String clickedCategoryName = clickedItem.getCategoryName();
                Toast.makeText(getContext(), clickedCategoryName + " selected", Toast.LENGTH_SHORT).show();
                if (position == 0) { //display all listings if category is "General"
                    defaultListings();
                } else {
                    Log.d("mw","value of categorySpinner.getSelectedItem().toString();" + categorySpinner.getSelectedItem().toString());
                    Log.d("mw","value of clickedCategoryName" + clickedCategoryName);
                    Log.d("mw", "value of categorySpinner.getSelectedItem()" + categorySpinner.getSelectedItem());
                    String category = clickedCategoryName;
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
        extras.putLong("ID",filteredList.getId(position));
        extras.putString("EMAIL",filteredList.getEmail(position));
        extras.putString("TELEGRAMID",filteredList.getTelegramID(position));
        Log.d("TELE_ID", filteredList.getTelegramID(position));
        //FirebaseUtils.getTelegramFromUID(filteredList.getUser(position));

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
        String selectedCategoryItem = mCategoryItem.get(categorySpinner.getSelectedItemPosition()).getCategoryName();
        Log.d("mw","value of mCategoryItem.get(categorySpinner.getSelectedItemPosition()).getCategoryName();" + mCategoryItem.get(categorySpinner.getSelectedItemPosition()).getCategoryName());
        searchText = newText;
        getCategorizedListing(selectedCategoryItem);
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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        otherListingRecyclerView.setLayoutManager(gridLayoutManager);
//        otherListingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listingAdapter.notifyDataSetChanged();
    }

    private void initList() { //method to populate array list for custom spinner
        mCategoryItem = new ArrayList<>();
        mCategoryItem.add(new CategoryItem("General", R.drawable.general));
        mCategoryItem.add(new CategoryItem("Acrylic", R.drawable.acrylic));
        mCategoryItem.add(new CategoryItem("Arts and Crafts", R.drawable.artsandcrafts));
        mCategoryItem.add(new CategoryItem("Adhesives", R.drawable.adhesives));
        mCategoryItem.add(new CategoryItem("Cables and Wires", R.drawable.cablesandwires));
        mCategoryItem.add(new CategoryItem("Electronics", R.drawable.electronics));
        mCategoryItem.add(new CategoryItem("Events", R.drawable.events));
        mCategoryItem.add(new CategoryItem("Hardware", R.drawable.hardware));
        mCategoryItem.add(new CategoryItem("Lighting", R.drawable.lighting));
        mCategoryItem.add(new CategoryItem("Microelectronics", R.drawable.microelectronics));
        mCategoryItem.add(new CategoryItem("Robotic Mechanical", R.drawable.roboticmechanical));
        mCategoryItem.add(new CategoryItem("Sealants and Tapes", R.drawable.sealantsandtape));
        mCategoryItem.add(new CategoryItem("Software", R.drawable.software));
        mCategoryItem.add(new CategoryItem("Tools", R.drawable.tools));
        mCategoryItem.add(new CategoryItem("Wood", R.drawable.wood));


    }

//    public void getSelectedItem(View v){
//        CategoryItem categoryItem = (CategoryItem) categorySpinner.getSelectedItem();
//    }
    }

