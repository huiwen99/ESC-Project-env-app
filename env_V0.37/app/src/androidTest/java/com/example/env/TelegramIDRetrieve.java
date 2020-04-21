package com.example.env;

import androidx.test.rule.ActivityTestRule;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.Assert;
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

import org.junit.Rule;
import org.junit.Test;

public class TelegramIDRetrieve {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void TelegramIDTest() {
        FirebaseUtils.getTelegramFromUID("7uCGQIoWpvMhNVHrOM3ub11RD5Z2");
        pauseTestFor(5);
        System.out.println(FirebaseUtils.telegramID);
        assertEquals("S4tan1zing", FirebaseUtils.telegramID);

    }

    private void pauseTestFor(long seconds) {
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
