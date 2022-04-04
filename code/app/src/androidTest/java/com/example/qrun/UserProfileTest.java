package com.example.qrun;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class UserProfileTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainScreen> rule =
            new ActivityTestRule<>(MainScreen.class, true, true);
    @Before
    public void setup() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();;
    }
    @Test
    public void seeUser(){
        solo.assertCurrentActivity("Wrong Activity",MainScreen.class);
        solo.clickOnMenuItem("Profile");
        solo.assertCurrentActivity("Fail to show profile",PlayerProfile.class);
    }
}
