package com.example.env;

import android.net.Uri;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.env.AddListing;
import com.example.env.ListingAdapter;
import com.example.env.ListingForDatabase;
import com.example.env.MainActivity;
import com.example.env.R;
import com.example.env.RecyclerViewItemListener;
import com.example.env.UserListings;
import com.example.env.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class FirebaseUtils {


    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    // for Firebase Storage
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    static StorageReference storageRef = storage.getReferenceFromUrl("gs://envfirebaseproject.appspot.com/");



    public static void pushListing(long timestamp, String title, String price, byte[] imageBytes, String category, String description, String currentUser) throws InterruptedException {
        // will be the item name in our DB

        //long listingTimestamp = System.currentTimeMillis();
        //Log.d(TAG, "Entering pushListing function");

        String imageName = String.format("%d.jpg", timestamp);

        final StorageReference imageref = storageRef.child(imageName);

        UploadTask uploadTask = imageref.putBytes(imageBytes);
        System.out.println("upload task created");
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                System.out.println("Upload failed");
                System.out.println(exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                System.out.println("Successful upload");
                System.out.println(taskSnapshot.getMetadata());
            }
        });

        System.out.println("preparing for urltask");


        //supposed to get us the URL to download this image
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Uri> task) {
                                         if (task.isSuccessful()) {

                                             System.out.println("Get Uri successful");
                                             Uri downloadUri = task.getResult();

                                         } else {
                                             System.out.println("Get Uri failed");
                                         }

                                     }
                                 }

        );

        /*System.out.println("supposedly done getting URL?");

        String stringURL = imageref.getDownloadUrl().getResult().toString();

        System.out.println("declared stringurl");*/

        ListingForDatabase listing = new ListingForDatabase(title, price, String.valueOf(timestamp), category, description, currentUser);
        mDatabase.child("testProducts").child(String.valueOf(timestamp)).setValue(listing);
        //String imageHexString = new String(Hex.encodeHex(imageHex));
        //Log.d(TAG, "Converted to hex string");


    }

    public static void getImage() {

    }
}
