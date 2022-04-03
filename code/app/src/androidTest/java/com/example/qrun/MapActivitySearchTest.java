package com.example.qrun;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MapActivitySearchTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MapsActivity> rule =
            new ActivityTestRule<>(MapsActivity.class, true, true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }
    @Test
    public void checkSearch(){
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
        solo.enterText((EditText) solo.getView(R.id.location_text), "University of Alberta");//Enter test location
        solo.clickOnButton("Search");
        assertTrue(solo.waitForText("Stay for the view or go for the treasure hunt?", 1, 5000));
    }
    @Test
    public void checkSure(){
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
        solo.enterText((EditText) solo.getView(R.id.location_text), "University of Alberta");//Enter test location
        solo.clickOnButton("Search");
        solo.waitForDialogToOpen(5000);
        solo.clickOnButton("Sure");
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);//Pass if user stay in current activity
    }
//    @Test
//    public void checkGo(){
//        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
//        solo.enterText((EditText) solo.getView(R.id.location_text), "University of Alberta");//Enter test location
//        solo.clickOnButton("Search");
//        solo.waitForDialogToOpen(5000);
//        solo.clickOnButton("GO!");
//        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);//Pass if user get into next activity
//    }
    @Test
    public void checkChoose(){
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
        solo.enterText((EditText) solo.getView(R.id.location_text), "University of Alberta");//Enter test location
        solo.clickOnButton("Search");
        solo.waitForDialogToOpen(5000);
        solo.clickOnButton("GO!");
        solo.assertCurrentActivity("Wrong Activity", SearchActivity.class);
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity",MapsActivity.class);
    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
