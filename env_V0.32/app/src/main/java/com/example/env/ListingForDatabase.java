package com.example.env;

import android.graphics.Bitmap;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ListingForDatabase {
    private String title;
    private String price;
    private String imageHex;
    private String category;
    private String description;

    public ListingForDatabase(String title, String price, String imageHex, String category,String description){
        this.title = title;

        BigDecimal p = new BigDecimal(price);
        p = p.setScale(2, RoundingMode.CEILING);

        this.price = "$"+p.toString();  //Note: might want to change this to an int later for calculation and shit - Dan
        this.imageHex = imageHex;
        this.category = category;
        this.description = description;
    }

    public String getTitle(){
        return title;
    }
    public String getPrice(){
        return price;
    }
    public String getImageHex(){
        return imageHex;
    }
    public String getCategory(){
        return category;
    }
    public String getDescription(){
        return description;
    }


}
