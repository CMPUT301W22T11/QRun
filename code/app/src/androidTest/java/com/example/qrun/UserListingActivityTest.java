package com.example.qrun;
import static androidx.test.espresso.Espresso.onView;
import static org.junit.Assert.assertFalse;
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

public class UserListingActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<UserListingActivity> rule =
            new ActivityTestRule<>(UserListingActivity.class, true, true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checksearch()throws NullPointerException {
        solo.assertCurrentActivity("Wrong Activity", UserListingActivity.class);
        solo.enterText((EditText) solo.getView(R.id.searchplayer), "Comment test");//Enter test comment
        solo.clickOnView(solo.getView(R.id.searchbut));
        assertTrue(solo.searchText("Comment test"));
    }
    @Test
    public void checkButton()throws NullPointerException{
//Asserts that the current activity is the UserListingActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", UserListingActivity.class);
        //solo.clickOnButton(solo.getString(R.id.searchbut));
        solo.clickOnView(solo.getView(R.id.camera_button));
        solo.assertCurrentActivity("Wrong Activity", ScanningActivity.class);
        solo.goBack();
        solo.clickOnView(solo.getView(R.id.playerlist));
        solo.assertCurrentActivity("Wrong Activity", UserProfileExternal.class);
        solo.goBack();
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}

