package com.readikids.app

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OnboardingScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testOnboardingFlow_initial() {
        // 1. Verify the initial screen is shown
        composeTestRule.onNodeWithText("WordWild").assertExists()
        composeTestRule.onNodeWithText("Explore the wild world of reading!").assertExists()

        // 2. Find and click the 'Next' button
        val nextButton = composeTestRule.onNodeWithText("Next →")
        nextButton.assertExists()
        nextButton.performClick()

        // 3. Verify the second step is shown
        composeTestRule.onNodeWithText("Who's exploring today?").assertExists()
    }
}
