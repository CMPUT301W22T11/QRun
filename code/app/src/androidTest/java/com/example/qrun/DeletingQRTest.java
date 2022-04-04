package com.example.qrun;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.matcher.RootMatchers.*;
import androidx.test.espresso.action.ViewActions;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DeletingQRTest {
    static final String userNameForTest = "testDelete2";
    @Rule
    public ActivityTestRule<UserListingActivity> rule =
            new ActivityTestRule<UserListingActivity>(UserListingActivity.class, true, true) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, UserListingActivity.class);
                    result.putExtra("userName", "sysys");
                    return result;
                }
            };
    @Test
    public void testSearch() {
        onView(withId(R.id.searchplayer)).check(matches(isDisplayed()));
        onView(withId(R.id.searchbut)).check(matches(isDisplayed()));
        onView(withId(R.id.searchplayer)).perform(ViewActions.clearText(),ViewActions.typeText(userNameForTest));
        onView(withId(R.id.searchbut)).perform(ViewActions.click());
        onData(anything()).inAdapterView(withId(R.id.playerlist)).atPosition(0).
                onChildView(withId(R.id.usernameContent)).check(matches(withText(userNameForTest))).perform(ViewActions.click());
        onView(withId(R.id.userNameTV)).check(matches(isDisplayed()));
    }
    @Test
    public void testDeleteQRof2() throws Exception{
        testSearch();
        onData(anything()).inAdapterView(withId(R.id.qrlist)).atPosition(0).perform(ViewActions.click());
        onView(withId(R.id.points)).check(matches(isDisplayed()));
        onView(withId(R.id.delete_admin_qr)).check(matches(isDisplayed())).perform(ViewActions.click());
        Thread.sleep(5000);
        onView(withId(R.id.userNameTV)).check(matches(isDisplayed()));
    }
}
