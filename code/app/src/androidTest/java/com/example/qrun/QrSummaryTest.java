package com.example.qrun;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.ContentView;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(AndroidJUnit4.class)

public class QrSummaryTest {
    static final String userForTest = "sysys";
    @Rule
    public ActivityTestRule<QrSummary> rule =
            new ActivityTestRule<QrSummary>(QrSummary.class, true, true) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, QrSummary.class);
                    result.putExtra("userName", userForTest);
                    result.putExtra("hexString","68410665196df4ea65cdb5fcd491b845478df24657657abc35184299513a0142");
                    return result;
                }
            };
    @Test
    public void checkAdd(){
        onView(withId(R.id.add_comment)).check(matches(isDisplayed()));
        onView(withId(R.id.add_comment)).perform(ViewActions.click());
        onView(withId(R.id.comment_text)).perform(ViewActions.typeText("Comment Test"));
        onView(withText("Cancel")).perform(ViewActions.click());
        onView(withText("Comment Test")).check(doesNotExist());
        onView(withId(R.id.add_comment)).perform(ViewActions.click());
        onView(withId(R.id.comment_text)).perform(ViewActions.typeText("Comment Test"));
        onView(withText("OK")).perform(ViewActions.click());
        onView(withText("Comment Test")).check(matches(isDisplayed()));
    }
    @Test
    public void checkView(){
        onView(withId(R.id.add_comment)).perform(ViewActions.click());
        onView(withId(R.id.comment_text)).perform(ViewActions.typeText("Comment Test"));
        onView(withText("OK")).perform(ViewActions.click());
        onData(anything()).inAdapterView(withId(R.id.comment_list)).atPosition(0).
                onChildView(withId(R.id.comment_text)).check(matches(withText("Comment Test"))).perform(ViewActions.click());
        onView(withText("Comment Test")).check(matches(isDisplayed()));
    }



}
