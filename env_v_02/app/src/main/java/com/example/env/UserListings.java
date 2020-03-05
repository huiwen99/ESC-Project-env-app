package com.example.env;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UserListings {
    private ArrayList<Listing> userListings;

    public UserListings(){
        userListings = new ArrayList<>();
    }

    public void addListing(String title, String price, Bitmap image, String category,String description){
        Listing listing = new Listing(title, price, image, category, description);
        userListings.add(listing);
    }

    void removeListing(int position){
        userListings.remove(position);
    }

    void removeListing(Listing listing){
        userListings.remove(listing);
    }

    String getTitle(int i){
        return userListings.get(i).getTitle();
    }
    String getPrice(int i){
        return userListings.get(i).getPrice();
    }
    Bitmap getImage(int i){
        return userListings.get(i).getImage();
    }
    String getCategory(int i){
        return userListings.get(i).getCategory();
    }
    String getDescription(int i){
        return userListings.get(i).getDescription();
    }

    int getSize(){
        return userListings.size();
    }
}
