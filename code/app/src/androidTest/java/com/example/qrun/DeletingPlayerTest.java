package com.example.qrun;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class DeletingPlayerTest {
    private Solo solo;

    @Rule
    public ActivityTestRule rule =
            new ActivityTestRule(DeletingPlayer.class, true, true);
    @Before
    public void setUpAgain() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void check()throws NullPointerException {
        solo.assertCurrentActivity("Wrong Activity", DeletingPlayer.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
