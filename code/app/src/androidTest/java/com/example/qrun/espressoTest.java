package com.example.qrun;

import androidx.annotation.ContentView;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.*;
import androidx.test.espresso.matcher.*;

import java.util.regex.Matcher;
//import android.support.test.espresso.Espresso;


@RunWith(AndroidJUnit4.class)
public class espressoTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mMainActivityTest = new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void listGoesOverTheFold() {

    }

}
