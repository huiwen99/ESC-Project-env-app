package com.example.env.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.env.FirebaseUtils;
import com.example.env.Listing;
import com.example.env.ListingAdapter;
import com.example.env.MainActivity;
import com.example.env.R;
import com.example.env.RecyclerViewItemListener;
import com.example.env.UserListings;
import com.example.env.Utils;
import com.example.env.ui.dashboard.ViewOtherListing;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements RecyclerViewItemListener{

    private NotificationsViewModel notificationsViewModel;

    RecyclerView bookmarksRecyclerView;
    ListingAdapter listingAdapter;
    UserListings bookmarkedListings;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        ((MainActivity) getActivity()).hideButton();

        Context context = container.getContext();

        bookmarksRecyclerView = root.findViewById(R.id.bookmarksRecyclerView);
        bookmarkedListings = new UserListings();

        for (Listing i : FirebaseUtils.myUserListings.userListings) {

            if (FirebaseUtils.myBookmarks.contains(i.getId())) {
                Boolean alreadyIn = false;
                for (Listing j : bookmarkedListings.userListings) {
                    if (j.getId() == i.getId()) {
                        alreadyIn = true;
                    }
                }
                if (alreadyIn.equals(false)) {
                    bookmarkedListings.addListing(i);
                }

                //refreshRecyclerView();
            }
        }


        listingAdapter = new ListingAdapter(context, bookmarkedListings, this);
        bookmarksRecyclerView.setAdapter(listingAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        bookmarksRecyclerView.setLayoutManager(gridLayoutManager);
        return root;
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getActivity(), ViewOtherListing.class);

        Bundle extras = new Bundle();

        extras.putString("TITLE", bookmarkedListings.getTitle(position));
        extras.putString("PRICE", bookmarkedListings.getPrice(position));
        extras.putString("CATEGORY",bookmarkedListings.getCategory(position));
        extras.putString("DESCRIPTION",bookmarkedListings.getDescription(position));
        extras.putString("USER",bookmarkedListings.getUser(position));


        Bitmap image = bookmarkedListings.getImage(position);
        byte[] byteArray = Utils.bitmapToByteArray(image);

        extras.putByteArray("IMAGE",byteArray);

        intent.putExtras(extras);

        startActivity(intent);
    }

    private void refreshRecyclerView(UserListings listings){
        ViewGroup container = (ViewGroup) getView().getParent();
        Context context = container.getContext();
        listingAdapter = new ListingAdapter(context, listings, this);
        bookmarksRecyclerView.setAdapter(listingAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        bookmarksRecyclerView.setLayoutManager(gridLayoutManager);
//        otherListingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listingAdapter.notifyDataSetChanged();
    }
}