package com.example.env;

import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    private String username = "koh.huiwen99@gmail.com";
    private String password = "1003593";

    @Test
    public void testValidUserInputLogin(){
        //let activity load
        pauseTestFor(3);
        //input text
        onView(withId(R.id.login_email)).perform(replaceText(username));
        pauseTestFor(1);
        onView(withId(R.id.login_password)).perform(replaceText(password));
        pauseTestFor(1);
        //click button
        onView(withId(R.id.login_btn)).perform(ViewActions.click());
        pauseTestFor(10);
    }
    @Test
    public void testInvalidUserInputLogin(){
        //let activity load
        pauseTestFor(3);
        //input text
        onView(withId(R.id.login_email)).perform(replaceText(username));
        pauseTestFor(1);
        onView(withId(R.id.login_password)).perform(replaceText("wrongPassword"));
        pauseTestFor(1);
        //click button
        onView(withId(R.id.login_btn)).perform(ViewActions.click());
        pauseTestFor(10);
    }

    @Test
    public void testRegistrationActivity(){
        pauseTestFor(1);
        //click button
        onView(withId(R.id.login_to_create)).perform(ViewActions.click());
        pauseTestFor(10);
    }


    private void pauseTestFor(long seconds) {
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}



