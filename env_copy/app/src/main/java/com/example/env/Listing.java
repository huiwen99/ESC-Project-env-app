package com.example.env;

import android.graphics.Bitmap;

public class Listing {
    private String title;
    private String price;
    private Bitmap image;

    public Listing(String title, String price, Bitmap image){
        this.title = title;
        this.price = price;
        this.image = image;
    }

    public String getTitle(){
        return title;
    }
    public String getPrice(){
        return price;
    }
    public Bitmap getImage(){
        return image;
    }

}
