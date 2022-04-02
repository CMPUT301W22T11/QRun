package com.example.qrun;

import static org.junit.Assert.assertTrue;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MapActivityTest1 {
    private Solo solo;
    @Rule
    public ActivityTestRule rule =
            new ActivityTestRule(MapsActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkDialogue() throws NullPointerException{
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
        solo.clickOnScreen(540, 960);
        assertTrue(solo.waitForDialogToOpen());
    }

    //condition, tested only on Pixel 2 API 30
    //Robotium has no way to click on marker so marker was centred and click on middle of screen was used
    @Test
    public void checkShowQR() throws NullPointerException{
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
        solo.clickOnScreen(540, 960);
        assertTrue(solo.waitForDialogToOpen());
        solo.clickOnButton("Show QR");
        solo.assertCurrentActivity("Wrong Activity", QrSummary.class);
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
