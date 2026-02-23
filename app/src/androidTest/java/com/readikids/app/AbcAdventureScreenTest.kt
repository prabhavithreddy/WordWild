package com.readikids.app

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AbcAdventureScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testPlayAbcAdventureAndCheckScore() {
        // From the Home Screen, navigate to the ABC Adventure game
        composeTestRule.onNodeWithText("ABC Adventure").performClick()

        // Verify we are on the game screen by checking for an introductory text
        composeTestRule.onNodeWithText("Tap any letter to learn it! 🎉\n0/26 discovered").assertExists()

        // Click on the letter 'A'
        composeTestRule.onNodeWithText("A").performClick()

        // Verify the score has updated from 0 to 5
        composeTestRule.onNodeWithText("⭐ 5").assertExists()

        // Click on the letter 'B'
        composeTestRule.onNodeWithText("B").performClick()

        // Verify the score has updated to 10
        composeTestRule.onNodeWithText("⭐ 10").assertExists()
    }
}