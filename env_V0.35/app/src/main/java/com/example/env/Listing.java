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
    private String user;

    public Listing(String title, String price, Bitmap image, String category,String description, String user){
        this.title = title;
        this.price = numInputToPriceText(price);
        this.image = image;
        this.category = category;
        this.description = description;
        this.user = user;
    }

    public void editListingDetails(String title, String price, Bitmap image, String category,String description){
        this.title = title;
        this.price = numInputToPriceText(price);
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
    public String getUser(){
        return user;
    }

    public static String numInputToPriceText(String rawPrice){
        BigDecimal p = new BigDecimal(rawPrice);
        p = p.setScale(2, RoundingMode.CEILING);
        String price = "$"+p.toString();  //Note: might want to change this to an int later for calculation and shit - Dan
        return price;
    }

    public static String priceTextToNumString(String priceText){ //maybe just have this so can convert to BigDecimal? -hw
        if(priceText.charAt(0)=='$') {
            priceText = priceText.substring(1); //remove '$' from price text
        }
        return priceText;
    }




}
