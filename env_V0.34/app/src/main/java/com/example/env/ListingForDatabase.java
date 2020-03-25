package com.example.env;

import android.graphics.Bitmap;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ListingForDatabase {
    private String imgNumber;
    private String title;
    private String price;
    private String category;
    private String description;
    private String user;

    public ListingForDatabase() {
        // blank constructor needed for somme Firebase fuckery, dont delete
    }

    public ListingForDatabase(String imgNumber, String title, String price, String category, String description, String user){
        this.imgNumber = imgNumber;
        this.title = title;

        BigDecimal p = new BigDecimal(price);
        p = p.setScale(2, RoundingMode.CEILING);

        this.price = "$"+p.toString();  //Note: might want to change this to an int later for calculation and shit - Dan
        this.category = category;
        this.description = description;
        this.user = user;
    }

    public String getTitle(){
        return title;
    }
    public String getPrice(){
        return price;
    }
    public String getCategory(){
        return category;
    }
    public String getDescription(){
        return description;
    }
    public String getUser() {
        return user;
    }
    public String getImgNumber() {
        return imgNumber;
    }
}
