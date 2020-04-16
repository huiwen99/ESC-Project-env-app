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

// Not a test as of now, just using this to add us as admins

public class AddUsersAdmin {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    // for Firebase Storage
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    static StorageReference storageRef = storage.getReferenceFromUrl("gs://envfirebaseproject.appspot.com/");

    @Test
    public void addAdmins() {
        FirebaseUtils.addUser("IAOdBNjJILgqYgoDURTzdFEqm943", "HeizerSpider", "imanheizer@gmail.com", true);
        FirebaseUtils.addUser("djhRlQzpJBhtOr4clV5cVUtmr6q2", "mingweisoo", "mingweisoo@gmail.com", true);
        FirebaseUtils.addUser("j9APkvXmuLhBEJTYzJzYUoTOjxX2", "huiwen99", "koh.huiwen99@gmail.com", true);
        FirebaseUtils.addUser("xtVvAhi11VRL6WZlcfNFkdWw3TM2", "S4tan1zing", "dangn511@gmail.com", true);
        assertEquals(1,1);
    }
}
