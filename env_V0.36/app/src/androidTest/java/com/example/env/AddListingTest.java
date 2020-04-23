package com.example.env;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.intent.Intents.intending;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.Rule;

import java.util.Random;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Character.toUpperCase;
import static java.util.regex.Pattern.matches;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;


public class AddListingTest {
    @Rule
    public IntentsTestRule<StartScreen> mIntentsRule = new IntentsTestRule<>(StartScreen.class);

    private final String username = "koh.huiwen99@gmail.com";
    private final String password = "1003593";

    @Before
    public void stubCameraIntent(){
        Instrumentation.ActivityResult result = createImageCaptureActivityResultStub();
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(result);
        pauseTestFor(5);
        onView(withId(R.id.login_email)).perform(replaceText(username));
        onView(withId(R.id.login_password)).perform(replaceText(password));
        //click button
        onView(withId(R.id.login_btn)).perform(ViewActions.click());
        pauseTestFor(5);

    }

    @Test
    public void testAddingListing(){
        pauseTestFor(7);
        onView(withId(R.id.addListingButton)).perform(ViewActions.click());
        pauseTestFor(5);

        //inside AddListing activity

        // Click on the button that will trigger the stubbed intent.
        onView(withId(R.id.imageSelected)).perform(click());

        pauseTestFor(5);
        onView(withId(R.id.newListingTitle)).perform(replaceText("TestingTitle"));
        pauseTestFor(1);
        onView(withId(R.id.newListingPrice)).perform(scrollTo());
        onView(withId(R.id.newListingPrice)).perform(replaceText("5"));
        pauseTestFor(1);
        onView(withId(R.id.newListingCategory)).perform(click());
        pauseTestFor(1);
        onView(allOf(withId(R.id.textViewCategory), withText("Hardware"))).perform(click());
        pauseTestFor(1);
        onView(withId(R.id.newListingDescription)).perform(scrollTo());
        pauseTestFor(1);
        onView(withId(R.id.newListingDescription)).perform(replaceText("TestingDescription"));
        pauseTestFor(1);
        onView(withId(R.id.addNewListing)).perform(ViewActions.click());
        pauseTestFor(5);

    }

    @Test
    public void testBannedWords(){
        pauseTestFor(7);
        onView(withId(R.id.addListingButton)).perform(ViewActions.click());
        pauseTestFor(5);

        //inside AddListing activity

        // Click on the button that will trigger the stubbed intent.
        onView(withId(R.id.imageSelected)).perform(click());
        String bannedWord = "damn";

        pauseTestFor(3);
        onView(withId(R.id.newListingTitle)).perform(replaceText(bannedWord));
        pauseTestFor(1);
        onView(withId(R.id.newListingPrice)).perform(scrollTo());
        pauseTestFor(1);
        onView(withId(R.id.newListingPrice)).perform(replaceText("5"));
        pauseTestFor(1);
        onView(withId(R.id.newListingCategory)).perform(click());
        pauseTestFor(1);
        onView(allOf(withId(R.id.textViewCategory), withText("Hardware"))).perform(click());
        pauseTestFor(1);
        onView(withId(R.id.newListingDescription)).perform(scrollTo());
        pauseTestFor(1);
        onView(withId(R.id.newListingDescription)).perform(replaceText("TestingDescription"));
        pauseTestFor(1);
        onView(withId(R.id.addNewListing)).perform(ViewActions.click());
        pauseTestFor(2);
        onView(withId(R.id.newListingTitle)).perform(scrollTo());
        pauseTestFor(1);
        for(int i=0;i<5;i++) {
            onView(withId(R.id.newListingTitle)).perform(replaceText(fuzzCapitalize(bannedWord)));
            pauseTestFor(1);
            onView(withId(R.id.addNewListing)).perform(ViewActions.click());
            pauseTestFor(1);
        }
       try{
            onView(withId(R.id.addNewListing)).check(ViewAssertions.matches(isDisplayed()));
        }catch(NoMatchingViewException e){
            fail();
        }
    }


    private Instrumentation.ActivityResult createImageCaptureActivityResultStub() {
        // Put the drawable in a bundle.
        Bundle bundle = new Bundle();
        Uri uri = getUriToDrawable(mIntentsRule.getActivity(),R.drawable.fan);
        bundle.putParcelable(AddListing.KEY_IMAGE,uri);
//        bundle.putParcelable(AddListing.KEY_IMAGE, BitmapFactory.decodeResource(
//                mIntentsRule.getActivity().getResources(), R.drawable.fan));

        // Create the Intent that will include the bundle.
        Intent resultData = new Intent();
        resultData.putExtras(bundle);

        // Create the ActivityResult with the Intent.
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    private void pauseTestFor(long seconds) {
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }

    private static String fuzzCapitalize(String word){
        char[] charArray = word.toCharArray();
        Random rand = new Random();
        int numOfChars = rand.nextInt(charArray.length);
        for(int j = 0; j<numOfChars; j++) {
            int i = rand.nextInt(charArray.length);
            charArray[i] = toUpperCase(charArray[i]);
        }
        String newWord = new String(charArray);
        return newWord;
    }

}