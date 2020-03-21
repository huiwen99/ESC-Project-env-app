package com.example.env.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.env.ListingAdapter;
import com.example.env.R;
import com.example.env.RecyclerViewItemListener;
import com.example.env.UserListings;
import com.example.env.Utils;
import com.example.env.ui.home.ViewOwnListing;

import java.util.ArrayList;

public class DashboardFragment extends Fragment implements RecyclerViewItemListener {

    RecyclerView otherListingRecyclerView;
    ListingAdapter listingAdapter;
    UserListings masterListings; //to pull from firebase, list of all existing listings

    final int REQUEST_CODE_IMAGE = 1000;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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

        Context context = container.getContext();

        otherListingRecyclerView = root.findViewById(R.id.otherListingRecyclerView);

        ArrayList<Integer> drawableId = new ArrayList<Integer>();
        drawableId.add(R.drawable.fan);
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

    }
