package com.example.env.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import com.example.env.LoginActivity;
import com.example.env.MainActivity;
import com.example.env.R;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

public class HomeFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

//    @Test
//    public void testAddListing(){
//        pauseTestFor(3);
//        onView(withId(R.id.addListingButton)).perform(ViewActions.click());
//        pauseTestFor(3);
//
//        //inside AddListing activity
//
//        pauseTestFor(3);
//        onView(withId(R.id.newListingTitle)).perform(replaceText("TestingTitle"));
//        pauseTestFor(1);
//        onView(withId(R.id.newListingPrice)).perform(replaceText("5"));
//        pauseTestFor(1);
//        onView(withId(R.id.newListingDescription)).perform(replaceText("TestingDescription"));
//        pauseTestFor(1);
//        onView(withId(R.id.addNewListing)).perform(ViewActions.click());
//        pauseTestFor(5);
//    }

    @Test
    public void testViewAndEditListing(){
        pauseTestFor(3);
        onView(withId(R.id.userListingRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        pauseTestFor(1);
        onView(withId(R.id.editListingButton)).perform(click());
        pauseTestFor(1);
        onView(withId(R.id.editTitle)).perform(replaceText("editedTitle"));
        pauseTestFor(1);
        onView(withId(R.id.editPrice)).perform(replaceText("1"));
        pauseTestFor(1);

        onView(withId(R.id.editCateogry)).perform(click());
        pauseTestFor(1);
        onData(is("Microelectronics")).perform(click());
        pauseTestFor(1);

        onView(withId(R.id.editDescription)).perform(replaceText("editedDescription"));
        pauseTestFor(1);
        onView(withId(R.id.editListing)).perform(click());
        pauseTestFor(3);
    }

    private void pauseTestFor(long seconds) {
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    private void savePickedImage() {
//        Bitmap bm = BitmapFactory.decodeResource(mActivityTestRule.getActivity().getResources(), R.drawable.fan);
//        assertTrue(bm != null);
//        File dir = mActivityTestRule.getActivity().getExternalCacheDir();
//        File file = new File(dir.getPath(), "pickImageResult.jpeg");
//        System.out.println(file.getAbsolutePath());
//        FileOutputStream outStream = null;
//        try {
//            outStream = new FileOutputStream(file);
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
//            outStream.flush();
//            outStream.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }



}
