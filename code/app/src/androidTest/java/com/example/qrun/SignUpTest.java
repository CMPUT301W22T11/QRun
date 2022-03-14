package com.example.qrun;

import android.app.Activity;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

public class SignUpTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);




    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void checkSignup(){
        solo.clickOnButton("Sign up");
        solo.enterText((EditText) solo.getView(R.id.name), "intentTest");
        solo.enterText((EditText) solo.getView(R.id.username), "intentTest3"); //change this cause u cant have 2 duplicate usernames
        solo.enterText((EditText) solo.getView(R.id.phone), "213123");
        solo.enterText((EditText) solo.getView(R.id.email), "intentTest");
        solo.clickOnButton("OK");
        solo.assertCurrentActivity("Wrong Activity", MainScreen.class);
    }



    }




