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

public class RegisterActivityTest {
    @Rule
    public ActivityTestRule<RegisterActivity> mActivityTestRule = new ActivityTestRule<>(RegisterActivity.class);

    private String user = "Testing display name";
    private String user_fail = "Testing invalid account";
    private String email = "abcdefg@gmail.com";
    private String password = "1000000";

    @Test
    public void testValidRegistration(){
        //let activity load
        pauseTestFor(3);
        //input text
        onView(withId(R.id.reg_telegram_user)).perform(replaceText(user));
        pauseTestFor(1);
        onView(withId(R.id.reg_email)).perform(replaceText(email));
        pauseTestFor(1);
        onView(withId(R.id.reg_password)).perform(replaceText(password));
        pauseTestFor(1);
        //click button
        onView(withId(R.id.reg_create_btn)).perform(ViewActions.click());
        pauseTestFor(10);
    }

    @Test
    public void testInvalidRegistration(){
        //let activity load
        pauseTestFor(3);
        //input text
        onView(withId(R.id.reg_telegram_user)).perform(replaceText(user_fail));
        pauseTestFor(1);
        onView(withId(R.id.reg_email)).perform(replaceText("invalid_email"));
        pauseTestFor(1);
        onView(withId(R.id.reg_password)).perform(replaceText("wrong password"));
        pauseTestFor(1);
        //click button
        onView(withId(R.id.reg_create_btn)).perform(ViewActions.click());
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
