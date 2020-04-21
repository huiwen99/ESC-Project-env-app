package com.example.env;

import android.util.Log;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class BannedUsersLoginTest {
    @Rule
    public IntentsTestRule<StartScreen> mIntentsRule = new IntentsTestRule<>(StartScreen.class);

    private final String username = "dangn511@gmail.com"; //banned user id
    private final String password = "wtf420666";

    @Test
    public void testBannedUserInputLogin() {
        pauseTestFor(6);
        onView(withId(R.id.login_email)).perform(replaceText(username));
        pauseTestFor(2);
        onView(withId(R.id.login_password)).perform(replaceText(password));
        pauseTestFor(2);
        onView(withId(R.id.login_btn)).perform(ViewActions.click());
        pauseTestFor(3);
        try {
            onView(withId(R.id.login_btn)).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            fail();
        }
    }

    private void pauseTestFor(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
