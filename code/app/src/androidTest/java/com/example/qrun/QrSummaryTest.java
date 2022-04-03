package com.example.qrun;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class QrSummaryTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<QRGameListActivity> rule =
            new ActivityTestRule<>(QRGameListActivity.class, true, true);
    @Before
    public void setup() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }
    @Test
    public void checkAdd(){
        solo.assertCurrentActivity("Wrong Activity", QRGameListActivity.class);
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity",QrSummary.class);
        solo.clickOnView(solo.getView(R.id.add_comment));//Click ADD COMMENT Button
        solo.waitForDialogToOpen();
        solo.enterText((EditText) solo.getView(R.id.comment_text), "Comment test");//Enter test comment
        solo.clickOnButton("Cancel"); //Select Cancel Button
        assertFalse(solo.searchText("Comment test"));

        solo.clickOnView(solo.getView(R.id.add_comment));//Click ADD COMMENT Button
        solo.waitForDialogToOpen();
        solo.enterText((EditText) solo.getView(R.id.comment_text), "Comment test");//Enter test comment
        solo.clickOnButton("OK"); //Select Cancel Button
        assertTrue(solo.searchText("Comment test"));
//        QrSummary activity = (QrSummary) solo.getCurrentActivity();
//        final ListView comments = activity.findViewById(R.id.comment_list); // Get the listview
//        String comment = (String) comments.get; // Get item from first position
//        assertEquals("Comment test", comment);
    }
    @Test
    public void checkView(){
        solo.assertCurrentActivity("Wrong Activity", QRGameListActivity.class);
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity",QrSummary.class);
        solo.clickOnView(solo.getView(R.id.add_comment));//Click ADD COMMENT Button
        solo.enterText((EditText) solo.getView(R.id.comment_text), "Comment test");//Enter test comment
        solo.clickOnButton("OK"); //Select Cancel Button
        solo.clickInList(1,1);
        solo.waitForDialogToOpen();//Check if add comment dialog is displayed
        assertTrue("Comment is not displayed properly", solo.searchText("Comment test"));//check if comment is properly displayed
        solo.clickOnButton("Finish");//finish viewing
        solo.assertCurrentActivity("Fail to close view",QrSummary.class);//check if dialog closed
    }
    @Test
    public void deleteQr(){
        solo.assertCurrentActivity("Wrong Activity", QRGameListActivity.class);
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity",QrSummary.class);
        solo.clickOnView(solo.getView(R.id.deleteQr));
        solo.assertCurrentActivity("Wrong Activity", QRGameListActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
