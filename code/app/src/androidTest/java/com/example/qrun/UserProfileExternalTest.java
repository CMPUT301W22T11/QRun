package com.example.qrun;
import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class UserProfileExternalTest {
    private Solo solo;
    @Rule
    public ActivityTestRule rule =
            new ActivityTestRule(UserProfileExternal.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkButton() throws NullPointerException{
        solo.assertCurrentActivity("Wrong Activity", UserProfileExternal.class);
        solo.clickInList(R.id.qrlist);
        solo.assertCurrentActivity("Wrong Activity", QrSummary.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
