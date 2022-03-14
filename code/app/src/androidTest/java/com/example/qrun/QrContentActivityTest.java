package com.example.qrun;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

public class QrContentActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule rule = new ActivityTestRule(QrContentActivity.class,true,true);
    @Before
    public void setup() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();;
    }
    @Test
    public void checkAdd(){
        solo.assertCurrentActivity("Wrong Activity", QrContentActivity.class);
        solo.clickOnView(solo.getView(R.id.add_comment_button));//Click ADD COMMENT Button
        assertTrue("Could not find the dialog!", solo.searchText("Add your comment"));//Check if add comment dialog is displayed
        solo.enterText((EditText) solo.getView(R.id.comment_text), "Comment test");//Enter test comment
        solo.clickOnButton("Cancel"); //Select Cancel Button
        assertFalse(solo.searchText("Comment test"));

        solo.clickOnView(solo.getView(R.id.add_comment_button));//Click ADD COMMENT Button
        assertTrue("Could not find the dialog!", solo.searchText("Add your comment"));//Check if add comment dialog is displayed
        solo.enterText((EditText) solo.getView(R.id.comment_text), "Comment test");//Enter test comment
        solo.clickOnButton("OK"); //Select Cancel Button
        assertTrue(solo.waitForText("Comment test", 1, 2000));
        QrContentActivity activity = (QrContentActivity) solo.getCurrentActivity();
        final ListView comments = activity.commentList; // Get the listview
        String comment = (String) comments.getItemAtPosition(0); // Get item from first position
        assertEquals("Comment test", comment);
    }
    @Test
    public void checkView(){
        solo.assertCurrentActivity("Wrong Activity", QrContentActivity.class);
        solo.clickOnView(solo.getView(R.id.add_comment_button));//Click ADD COMMENT Button
        solo.enterText((EditText) solo.getView(R.id.comment_text), "Comment test");//Enter test comment
        solo.clickOnButton("OK"); //Select Cancel Button
        assertTrue(solo.waitForText("Comment test", 1, 2000));
        solo.clickInList(0);
        assertTrue("Could not find the dialog!", solo.searchText("Viewing Comment"));//Check if add comment dialog is displayed
        assertTrue("Comment is not displayed properly", solo.searchText("Comment test"));//check if comment is properly displayed
        solo.clickOnButton("Finish");//finish viewing
        assertFalse("dialog not closed", solo.searchText("Viewing Comment"));//check if dialog closed
    }
    @Test
    public void checkDelete(){
        solo.assertCurrentActivity("Wrong Activity", QrContentActivity.class);
        solo.clickOnView(solo.getView(R.id.add_comment_button));//Click ADD COMMENT Button
        solo.enterText((EditText) solo.getView(R.id.comment_text), "Comment test");//Enter test comment
        solo.clickOnButton("OK"); //Select Cancel Button
        assertTrue(solo.waitForText("Comment test", 1, 2000));
        solo.clickLongInList(0);
        assertTrue("Could not find the dialog!", solo.searchText("Deleting Comment"));
        solo.clickOnButton("Yes");
        assertFalse("fail to delete",solo.searchText("Comment test"));

    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
