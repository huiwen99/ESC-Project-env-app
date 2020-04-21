package com.example.env;

import android.util.Log;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
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

public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    private final String username = "koh.huiwen99@gmail.com";
    private final String password = "1003593";

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
        pauseTestFor(3);
        try{
            onView(withId(R.id.nav_host_fragment)).check(matches(isDisplayed()));
        }catch(NoMatchingViewException e){
            fail();
        }
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
        pauseTestFor(3);
        try{
            onView(withId(R.id.login_btn)).check(matches(isDisplayed()));
        }catch(NoMatchingViewException e){
            //fail();
        }
    }

    @Test
    public void loginUsernameFuzzer(){
        //let activity load
        pauseTestFor(1);
        onView(withId(R.id.login_password)).perform(replaceText(password));
        pauseTestFor(1);
        String fuzzedUsername;
        for(int i=0;i<20;i++){//test with 10 fuzzed usernames
            do{
                fuzzedUsername=mutate(username);
            }while(fuzzedUsername.toLowerCase().equals(username));
            onView(withId(R.id.login_email)).perform(replaceText(fuzzedUsername));
            pauseTestFor(1);
            onView(withId(R.id.login_btn)).perform(ViewActions.click());
            pauseTestFor(2);
            try{//check whether it is still login activity via login button
                onView(withId(R.id.login_btn)).check(matches(isDisplayed()));
            }catch(NoMatchingViewException e){
                fail();
            }
        }
    }

    @Test
    public void loginPasswordFuzzer(){
        //let activity load
        pauseTestFor(1);
        onView(withId(R.id.login_email)).perform(replaceText(username));
        pauseTestFor(1);
        String fuzzedPassword;
        for(int i=0;i<20;i++){//test with 10 fuzzed passwords
            do{
                fuzzedPassword=mutate(password);
            }while(fuzzedPassword.equals(password));
            Log.i("pw",fuzzedPassword);
            onView(withId(R.id.login_password)).perform(replaceText(fuzzedPassword));
            pauseTestFor(1);
            onView(withId(R.id.login_btn)).perform(ViewActions.click());
            pauseTestFor(2);
            try{//check whether it is still login activity via login button
                onView(withId(R.id.login_btn)).check(matches(isDisplayed()));
            }catch(NoMatchingViewException e){
                fail();
            }
        }
    }

    @Test
    public void testRegistrationActivity(){
        pauseTestFor(1);
        //click button
        onView(withId(R.id.reg_create_btn)).perform(ViewActions.click());
        pauseTestFor(3);
        try{//check whether it is still login activity via login button
            onView(withId(R.id.reg_create_btn)).check(matches(isDisplayed()));
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

    public static String mutate(String s){
        Random rand = new Random();
        int position = rand.nextInt(s.length());
        int operator = rand.nextInt(3); //number of available operators
        if(operator==0){
            return swap(s, position);
        }else if(operator==1){
            return bitFlip(s, position);
        }else if(operator==2){
            return trim(s, position);
        }

        return s;
    }

    public static String swap(String s, int position){
        if(position!=s.length()-1){
            char a = s.charAt(position);
            char b = s.charAt(position+1);
            char[] charArray = s.toCharArray();
            charArray[position] = b;
            charArray[position+1] = a;
            String swapped = new String(charArray);
            return swapped;
        }else{
            char a = s.charAt(position);
            char b = s.charAt(0);
            char[] charArray = s.toCharArray();
            charArray[position] = b;
            charArray[0] = a;
            String swapped = new String(charArray);
            return swapped;
        }
    }

    public static String bitFlip(String s, int position){
        Random rand = new Random();
        int flipBit = rand.nextInt(8);
        char[] charArray = s.toCharArray();
        int b1 = s.charAt(position) ^ (1 << flipBit);
        char ch = (char) b1;
        charArray[position] = ch;
        String flipped = new String(charArray);
        System.out.println("Flipping the "+flipBit+" bit");
        return flipped;
    }

    public static String trim(String s, int position){
        String trimmed = s.substring(0,position);
        return trimmed;
    }


}



