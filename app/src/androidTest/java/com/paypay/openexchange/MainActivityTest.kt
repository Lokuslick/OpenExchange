package com.paypay.openexchange

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.paypay.openexchange.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.hamcrest.Matchers.anything
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java, false, true)


    @Test
    fun displaySpinnerWithData() {
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.rvExchangeRates))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.currencySpinner))
            .check(matches(Matchers.not(ViewMatchers.isDisplayed())))
        val expected = "AED"
        // When
        onView(withId(R.id.currencySpinner))
            .perform(click())
        onData(anything())
            .atPosition(0)
            .check(matches(withText(expected)))
    }
}