package com.example.qrun;

import android.app.Activity;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LogoutTest {
    private Solo solo;
    FirebaseFirestore db;


    @Rule
    public ActivityTestRule rule=new ActivityTestRule(MainActivity.class,true,true);

    @Before
    public void setUpAgain() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
//        solo.clickOnButton("Sign up");
//        solo.enterText((EditText) solo.getView(R.id.name), "intentTestYWP");
//        solo.enterText((EditText) solo.getView(R.id.username), "intentTestYWP"); //change this cause u cant have 2 duplicate usernames
//        solo.enterText((EditText) solo.getView(R.id.phone), "213123");
//        solo.enterText((EditText) solo.getView(R.id.email), "intentTest");
//        solo.clickOnButton("OK");
    }
    @Test
    public void CheckLogout(){
        solo.clickOnMenuItem("Logout");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }
//    @After
//    public void After(){
//        UserStorage userStorage=new UserStorage(db);
//        db.collection("User").document("intentTestYWP").delete();
//    }
}
