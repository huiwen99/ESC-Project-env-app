package com.example.env;

// THIS CLASS IS FOR TESTING PUSHING DATA TO FIREBASE ONLY
// DELETE THIS LATER

public class FakeTestData {
    public int price;
    public String name;

    public FakeTestData() {
    }

    public FakeTestData(int price, String name) {
        this.price = price;
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
