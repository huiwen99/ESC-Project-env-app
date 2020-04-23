package com.example.env.ui.dashboard;

import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.example.env.LoginActivity;
import com.example.env.MainActivity;
import com.example.env.R;
import com.example.env.StartScreen;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

public class DashboardFragmentTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    private final String username = "koh.huiwen99@gmail.com";
    private final String password = "1003593";

    @Test
    public void testNavigationBrowse(){
        pauseTestFor(5);
        onView(withId(R.id.login_email)).perform(replaceText(username));
        onView(withId(R.id.login_password)).perform(replaceText(password));
        //click button
        onView(withId(R.id.login_btn)).perform(ViewActions.click());
        pauseTestFor(5);
        onView(withContentDescription("Browse")).perform(click());
        pauseTestFor(5);
        try{
            onView(withId(R.id.categorySpinner)).check(matches(isDisplayed()));
        }catch(NoMatchingViewException e){
            fail();
        }
    }

    @Test
    public void testSearchCategory(){
        pauseTestFor(5);
        onView(withId(R.id.login_email)).perform(replaceText(username));
        onView(withId(R.id.login_password)).perform(replaceText(password));
        //click button
        onView(withId(R.id.login_btn)).perform(ViewActions.click());
        pauseTestFor(5);
        onView(withContentDescription("Browse")).perform(click());
        pauseTestFor(5);

        onView(withId(R.id.categorySpinner)).perform(click());
        pauseTestFor(1);
        onView(allOf(withId(R.id.textViewCategory), withText("Hardware"))).perform(click());
        pauseTestFor(1);
        try{
            onView(withId(R.id.otherListingRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            pauseTestFor(1);
            try{
                onView(withId(R.id.chatButton)).check(matches(isDisplayed()));
                onView(withId(R.id.otherListingCategory)).check(matches(withText("Hardware")));
            }catch(NoMatchingViewException e){
                fail();
            }
        }catch(IndexOutOfBoundsException e){
            Log.i("testSearch", "Category does not have items");
        }
    }

    @Test
    public void testSearchTitleAndCategory(){
        pauseTestFor(5);
        onView(withId(R.id.login_email)).perform(replaceText(username));
        onView(withId(R.id.login_password)).perform(replaceText(password));
        //click button
        onView(withId(R.id.login_btn)).perform(ViewActions.click());
        pauseTestFor(5);
        onView(withContentDescription("Browse")).perform(click());
        pauseTestFor(5);

        onView(withId(R.id.categorySpinner)).perform(click());
        pauseTestFor(1);
        onView(allOf(withId(R.id.textViewCategory), withText("Hardware"))).perform(click());
        pauseTestFor(1);

        onView(withId(R.id.action_search)).perform(click());
        pauseTestFor(1);
        String stringToSearch = "another"; //can be changed to suit our existing listings
        onView(isAssignableFrom(EditText.class)).perform(typeText(stringToSearch), pressKey(KeyEvent.KEYCODE_ENTER));
        closeSoftKeyboard();
        pauseTestFor(1);
        try{
            onView(withId(R.id.otherListingRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            pauseTestFor(1);
            try{
                onView(withId(R.id.chatButton)).check(matches(isDisplayed()));
                onView(withId(R.id.otherListingCategory)).check(matches(withText("Hardware")));
                onView(withText(stringToSearch));

            }catch(NoMatchingViewException e){
                fail();
            }
        }catch(PerformException e){
            Log.i("testSearch", "Item with these filters does not exist");
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