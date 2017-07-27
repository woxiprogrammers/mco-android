package com.android.constro360;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityUITest {
    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void loginActivityUITest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.userName), isDisplayed()));
        appCompatEditText.perform(click());
        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.userName), isDisplayed()));
        appCompatEditText2.perform(replaceText("admin"), closeSoftKeyboard());
        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.userName), withText("admin"), isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());
        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.password), isDisplayed()));
        appCompatEditText4.perform(replaceText("12345"), closeSoftKeyboard());
        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.password), withText("12345"), isDisplayed()));
        appCompatEditText5.perform(pressImeActionButton());
    }
}