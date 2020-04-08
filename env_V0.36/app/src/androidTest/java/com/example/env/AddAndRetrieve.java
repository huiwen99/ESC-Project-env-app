package com.example.env;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.common.api.HasApiKey;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;
import android.content.Context;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class AddAndRetrieve {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    // for Firebase Storage
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    static StorageReference storageRef = storage.getReferenceFromUrl("gs://envfirebaseproject.appspot.com/");

    String sampleBitmapURL = "https://firebasestorage.googleapis.com/v0/b/envfirebaseproject.appspot.com/o/1585173478912.jpg?alt=media&token=e1a1b8af-ef1d-4cbd-bdb4-88c8f209140b";

    Bitmap bm = getBitmapFromURL(sampleBitmapURL);
    byte[] bmBytes = Utils.bitmapToByteArray(bm);
    private String title = "AddAndRetrieve title";
    private String price = "100.00";
    private String category = "General";
    private String description = "AddAndRetrieve description";
    private String user = "10S3KcVBeagncf9bFDAVni8AN6l2"; // Hui Wen's gmail account

    @Test
    public void testPushListing() throws InterruptedException {
        //this test pushes a listing and checks if it's in the database
        final long timestamp = System.currentTimeMillis();
        FirebaseUtils.pushListing(timestamp, title, price, bmBytes, category, description, user);

        pauseTestFor(5);

        //collect listings from firebase
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get object and use the values to update the UI
                Log.d("DASHBOARD_TAG", "getting value");
                Object allListing = dataSnapshot.getValue();
                // ...
                HashMap allListingHashmap = ((HashMap) allListing); // cast this bitch into a hashmap
                System.out.println(allListingHashmap);

                HashMap testedObject = (HashMap) allListingHashmap.get(String.valueOf(timestamp));
                assertEquals(title, testedObject.get("title").toString());
                assertEquals(price, Listing.priceTextToNumString(testedObject.get("price").toString()));
                assertEquals(category, testedObject.get("category").toString());
                assertEquals(description, testedObject.get("description").toString());
                assertEquals(user, testedObject.get("user").toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TEST_TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("testProducts").addListenerForSingleValueEvent(postListener);

    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void pauseTestFor(long seconds) {
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
