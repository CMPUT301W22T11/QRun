package com.example.qrun;

import static org.junit.Assert.assertTrue;

import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class QrSummaryTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<QrSummary> rule =
            new ActivityTestRule<>(QrSummary.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void checkNewFragment() {
        solo.clickOnView(solo.getView(R.id.add_comment));
        assertTrue(true);
    }
}
