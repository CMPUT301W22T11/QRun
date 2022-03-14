package com.example.qrun;

import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;


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
    public void checkComment() {
        solo.assertCurrentActivity("Wrong activity", QrSummary.class);
        solo.clickOnView(solo.getView(R.id.add_comment));
        final String fieldValue = "Setting a new comment!";

        // Set a value into the text field
        solo.enterText((EditText) solo.getView(R.id.comment_text), fieldValue);
        assertTrue(solo.waitForText(fieldValue, 1, 2000));

    }
}
