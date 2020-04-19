package com.example.env;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class CategoryAdapter extends ArrayAdapter<CategoryItem> {

    public CategoryAdapter(Context context, ArrayList<CategoryItem> countryList) {
        super(context, 0, countryList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.category_spinner, parent, false
            );
        }

        ImageView imageViewCategoryIcon = convertView.findViewById(R.id.imageViewCategoryIcon);
        TextView textViewCategory = convertView.findViewById(R.id.textViewCategory);

        CategoryItem currentItem = getItem(position);

        if (currentItem != null) {
            imageViewCategoryIcon.setImageResource(currentItem.getCategoryImage());
            textViewCategory.setText(currentItem.getCategoryName());
        }

        return convertView;
    }

}