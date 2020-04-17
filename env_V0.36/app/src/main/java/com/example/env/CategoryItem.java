package com.example.env;

public class CategoryItem {
    private String mCategoryName;
    private int mCategoryImage;

    public CategoryItem(String categoryName, int categoryImage) {
        mCategoryName = categoryName;
        mCategoryImage = categoryImage;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public int getCategoryImage() {
        return mCategoryImage;
    }
}
