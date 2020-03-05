package com.example.env.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.env.Listing;
import com.example.env.ListingAdapter;
import com.example.env.R;
import com.example.env.UserListings;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    RecyclerView recyclerView;
    ListingAdapter listingAdapter;
    Button addListingButton;
    UserListings userListings;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        Context context = container.getContext();

        addListingButton = root.findViewById(R.id.addListingButton);
        recyclerView = root.findViewById(R.id.userListingRecyclerView);

        //adding to userListings
        ArrayList<Integer> drawableId = new ArrayList<Integer>();
        drawableId.add(R.drawable.fan);
        drawableId.add(R.drawable.heat_sink);
        userListings = new UserListings();
        for(Integer rid:drawableId){
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), rid);
            String imageName = context.getResources().getResourceEntryName(rid);
            String price = "$5";
            userListings.addListing(imageName,price,bitmap);
        }

        //initializing recyclerview
        listingAdapter = new ListingAdapter(context, userListings);
        recyclerView.setAdapter(listingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }

}