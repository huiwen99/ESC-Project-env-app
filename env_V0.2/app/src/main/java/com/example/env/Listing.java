package com.example.env;

import android.graphics.Bitmap;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Listing {
    private String title;
    private String price;
    private Bitmap image;
    private String category;
    private String description;

    public Listing(String title, String price, Bitmap image, String category,String description){
        this.title = title;

        BigDecimal p = new BigDecimal(price);
        p = p.setScale(2, RoundingMode.CEILING);

        this.price = "$"+p.toString();
        this.image = image;
        this.category = category;
        this.description = description;
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
    public String getCategory(){
        return category;
    }
    public String getDescription(){
        return description;
    }


}
