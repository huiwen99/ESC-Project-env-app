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

import java.util.ArrayList;

public class DashboardFragment extends Fragment implements RecyclerViewItemListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    RecyclerView otherListingRecyclerView;
    ListingAdapter listingAdapter;
    UserListings masterListings; //to pull from firebase, list of all existing listings
    UserListings filteredList; // a copy of masterListings
    UserListings generalList;
    UserListings roboticMechanicalList;
    UserListings microElectronicsList;
    String[] categories = {"All", "General", "Robotic Mechanical", "Microelectronics"};
    Spinner categorySpinner;

    private SearchView search;

    final int REQUEST_CODE_IMAGE = 1000;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //setHasOptionsMenu(true);

        ((MainActivity) getActivity()).hideButton();


        Context context = container.getContext();
        categorySpinner = root.findViewById(R.id.categorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1, categories));

        categorySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup container = (ViewGroup) getView().getParent();
                Context context = container.getContext();
                if (position == 0) {
                    listingAdapter = new ListingAdapter(context, masterListings, this);
                    otherListingRecyclerView.setAdapter(listingAdapter);
                    otherListingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                } else if (position == 1) {
                    getGeneralCategory();
                } else if (position == 2) {
                    getRoboticMechanicalCategory();
                } else {
                    getMicroelectronicsCategory();
                }
                listingAdapter.notifyDataSetChanged();
            }
        });


        otherListingRecyclerView = root.findViewById(R.id.otherListingRecyclerView);

        ArrayList<Integer> drawableId = new ArrayList<Integer>();
            drawableId.add(R.drawable.fan);
            drawableId.add(R.drawable.peltierchip);
            drawableId.add(R.drawable.threedprinter);
            drawableId.add(R.drawable.battery);
            drawableId.add(R.drawable.plywood);
            masterListings = new UserListings();
            for(Integer rid:drawableId){
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), rid);
                String imageName = context.getResources().getResourceEntryName(rid);
                String price = "5";
                String category = "General";
                String description = "test";
                String user = "env@gmail.com";
            masterListings.addListing(imageName,price,bitmap, category, description, user);

        }
        listingAdapter = new ListingAdapter(context, masterListings, this);
        otherListingRecyclerView.setAdapter(listingAdapter);
        otherListingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


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
//            byte[] byteArray = data.getByteArrayExtra(AddListing.KEY_IMAGE);
//            Bitmap image = Utils.byteArrayToBitmap(byteArray);
//
//            userListings.addListing(title,price,image,category,description,user);
//            listingAdapter.notifyDataSetChanged();
//        }
//    }


    @Override
    public void onItemClicked(int position) {
        Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ViewOtherListing.class);

        Bundle extras = new Bundle();

        extras.putString("TITLE",masterListings.getTitle(position));
        extras.putString("PRICE",masterListings.getPrice(position));
        extras.putString("CATEGORY",masterListings.getCategory(position));
        extras.putString("DESCRIPTION",masterListings.getDescription(position));
        extras.putString("USER",masterListings.getUser(position));

        Bitmap image = masterListings.getImage(position);
        byte[] byteArray = Utils.bitmapToByteArray(image);

        extras.putByteArray("IMAGE",byteArray);

        intent.putExtras(extras);

        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search Listing");

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
        ViewGroup container = (ViewGroup) getView().getParent();
        Context context = container.getContext();
        if (newText == null || newText.trim().isEmpty()){ //if nothing is entered in the search bar, it should show all listingsx
            //show recycler view of masterListings
            listingAdapter = new ListingAdapter(context, masterListings, this);
            otherListingRecyclerView.setAdapter(listingAdapter);
            otherListingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return false;
        }
        filteredList = new UserListings(); //create a new array List called filteredList so that we can add listings(items) inside when it matches the search and then show it
        for (Listing item : masterListings.userListings) { //for every listing in masterListings
            if(item.getTitle().toLowerCase().contains(newText.toLowerCase())||item.getDescription().toLowerCase().contains(newText.toLowerCase())) { // if search bar query contains characters same as the title of the listing
               filteredList.addListing(item); //add that listing(item) to filteredList
            }
        }
        //show recycler view of filteredList
        listingAdapter = new ListingAdapter(context, filteredList, this);
        otherListingRecyclerView.setAdapter(listingAdapter);
        otherListingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return true;
    }
    private void getGeneralCategory() {
        Log.d("mw","General category: ");
        ViewGroup container = (ViewGroup) getView().getParent();
        Context context = container.getContext();
        generalList = new UserListings();
        for (Listing item : masterListings.userListings) {
            if (item.getCategory().equals("General")) {
                generalList.addListing(item);
                listingAdapter = new ListingAdapter(context, generalList, this);
                otherListingRecyclerView.setAdapter(listingAdapter);
                otherListingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }


    }

    private void getRoboticMechanicalCategory() {
        Log.d("mw","Robotic mechanical category: ");
        ViewGroup container = (ViewGroup) getView().getParent();
        Context context = container.getContext();
        roboticMechanicalList = new UserListings();
        for (Listing item : masterListings.userListings) {
            if (item.getCategory().equals("Robotic Mechanical")) {
                roboticMechanicalList.addListing(item);
                listingAdapter = new ListingAdapter(context, roboticMechanicalList,this);
                otherListingRecyclerView.setAdapter(listingAdapter);
                otherListingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }


    }
    private void getMicroelectronicsCategory() {
        Log.d("mw","Microelectronics category: ");
        ViewGroup container = (ViewGroup) getView().getParent();
        Context context = container.getContext();
        microElectronicsList = new UserListings();
        for (Listing item : masterListings.userListings) {
            if (item.getCategory().equals("Microelectronics")) {
                microElectronicsList.addListing(item);
                listingAdapter = new ListingAdapter(context, microElectronicsList, this);
                otherListingRecyclerView.setAdapter(listingAdapter);
                otherListingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }


    }

    }

