package com.example.env.ui.home;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import com.example.env.LoginActivity;
import com.example.env.MainActivity;
import com.example.env.R;
import com.example.env.StartScreen;

import org.hamcrest.Matcher;
import org.junit.Before;
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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

public class HomeFragmentTest {
    @Rule
    public ActivityTestRule<StartScreen> mActivityTestRule = new ActivityTestRule<>(StartScreen.class);

    private final String username = "koh.huiwen99@gmail.com";
    private final String password = "1003593";
    
    @Test
    public void testViewAndEditListing(){
        pauseTestFor(5);
        onView(withId(R.id.login_email)).perform(replaceText(username));
        onView(withId(R.id.login_password)).perform(replaceText(password));
        //click button
        onView(withId(R.id.login_btn)).perform(ViewActions.click());
        pauseTestFor(5);
        onView(withId(R.id.userListingRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        pauseTestFor(1);
        onView(withId(R.id.editListingButton)).perform(click());
        pauseTestFor(1);
        onView(withId(R.id.editTitle)).perform(replaceText("editedTitle"));
        pauseTestFor(1);
        onView(withId(R.id.editPrice)).perform(replaceText("1"));
        pauseTestFor(1);

        onView(withId(R.id.editCategory)).perform(click());
        pauseTestFor(1);
        onView(allOf(withId(R.id.textViewCategory), withText("Hardware"))).perform(click());
        pauseTestFor(1);

        onView(withId(R.id.editDescription)).perform(replaceText("editedDescription"));
        pauseTestFor(1);
        onView(withId(R.id.editListing)).perform(click());
        pauseTestFor(5);

        onView(withId(R.id.listingTitle)).check(matches(withText("editedTitle")));
        onView(withId(R.id.listingPrice)).check(matches(withText("$1.00")));
        onView(withId(R.id.listingCategory)).check(matches(withText("Hardware")));
        onView(withId(R.id.listingDescription)).check(matches(withText("editedDescription")));

    }


    @Test
    public void logOutTest(){
        pauseTestFor(5);
        onView(withId(R.id.login_email)).perform(replaceText(username));
        onView(withId(R.id.login_password)).perform(replaceText(password));
        //click button
        onView(withId(R.id.login_btn)).perform(ViewActions.click());
        pauseTestFor(5);
        onView(withId(R.id.sign_out_button)).perform(click());
        pauseTestFor(2);
        try{
            onView(withId(R.id.login_btn)).check(matches(isDisplayed()));
        }catch(NoMatchingViewException e){
            fail();
        }
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
