package com.example.env;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UserListings {
    public ArrayList<Listing> userListings;

    public UserListings(){
        userListings = new ArrayList<>();
    }

    public void addListing(String title, String price, Bitmap image, String category,String description, String user){
        Listing listing = new Listing(title, price, image, category, description, user);
        userListings.add(listing);
    }
    public void addListing(Listing listing){
        userListings.add(listing);
    }

    public void editListing(int position, String title, String price, Bitmap image, String category,String description){
        Listing listingToChange = userListings.get(position);
        listingToChange.editListingDetails(title, price, image, category, description);
    }

    void removeListing(int position){
        userListings.remove(position);
    }

    void removeListing(Listing listing){
        userListings.remove(listing);
    }

    public String getTitle(int i){
        return userListings.get(i).getTitle();
    }
    public String getPrice(int i){
        return userListings.get(i).getPrice();
    }
    public Bitmap getImage(int i){
        return userListings.get(i).getImage();
    }
    public String getCategory(int i){
        return userListings.get(i).getCategory();
    }
    public String getDescription(int i){
        return userListings.get(i).getDescription();
    }
    int getSize(){
        return userListings.size();
    }
    public String getUser(int i){
        return userListings.get(i).getUser();
    }
    public void addListing(int position, Listing item) {
        userListings.add(position, item);
    }

    public Listing get(int adapterPosition) {
        Listing listing = userListings.get(adapterPosition);
        return listing;
    }
}
