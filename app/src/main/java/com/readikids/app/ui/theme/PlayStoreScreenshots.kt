package com.readikids.app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.readikids.app.data.model.*
import com.readikids.app.ui.screens.*
import com.readikids.app.ui.screens.games.*

/**
 * MARKETING SCREENSHOT WRAPPER
 * Provides a realistic device frame and beautiful background.
 */
@Composable
fun MarketingScreenshot(
    caption: String,
    useDarkDeviceBackground: Boolean = false,
    aspectRatio: Float = 9f / 19.5f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Purple80, Color(0xFF1E1B4B)))
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 20.dp, start = 30.dp, end = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = caption,
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                fontFamily = NunitoFamily,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // Simulated Device Frame
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio)
                    .border(10.dp, Color(0xFF111827), RoundedCornerShape(40.dp))
                    .padding(10.dp)
                    .clip(RoundedCornerShape(30.dp)),
                color = if (useDarkDeviceBackground) Color(0xFF4F46E5) else SkyBackground
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    WordWildTheme {
                        content()
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────
// PHONE SCREENSHOTS (Pixel 7)
// ─────────────────────────────────────────────────────────────────────

@Preview(device = Devices.PIXEL_7, widthDp = 412, heightDp = 892, name = "Phone 1")
@Composable
fun Screenshot1_Phone() = MarketingScreenshot(
    caption = "Start Your Wild\nAdventure!",
    useDarkDeviceBackground = true
) { WelcomeStep() }

@Preview(device = Devices.PIXEL_7, widthDp = 412, heightDp = 892, name = "Phone 2")
@Composable
fun Screenshot2_Phone() = MarketingScreenshot("Pick Your\nLearning Journey") {
    HomeScreenContent(
        childName = "Alex", totalXp = 450, streakDays = 5, currentLevel = 3,
        levelProgress = 0.25f, gamesPlayedToday = 1, skillStats = emptyList(),
        badges = listOf("xp_100"), ageGroup = AgeGroup.MOON_RIDERS,
        onNavigateToGame = {}, onNavigateToProgress = {}
    )
}

@Preview(device = Devices.PIXEL_7, widthDp = 412, heightDp = 892, name = "Phone 3")
@Composable
fun Screenshot3_Phone() = MarketingScreenshot("Master Every Letter\nand Sound") {
    AbcAdventureContent(onBack = {}, onGameFinished = {})
}

// ─────────────────────────────────────────────────────────────────────
// 7-INCH TABLET SCREENSHOTS (Nexus 7)
// ─────────────────────────────────────────────────────────────────────

@Preview(device = Devices.NEXUS_7, widthDp = 600, heightDp = 960, name = "7-inch 1")
@Composable
fun Screenshot1_Tablet7() = MarketingScreenshot(
    caption = "Start Your Wild\nAdventure!",
    useDarkDeviceBackground = true,
    aspectRatio = 600f / 960f
) { WelcomeStep() }

@Preview(device = Devices.NEXUS_7, widthDp = 600, heightDp = 960, name = "7-inch 2")
@Composable
fun Screenshot2_Tablet7() = MarketingScreenshot(
    caption = "Pick Your\nLearning Journey",
    aspectRatio = 600f / 960f
) {
    HomeScreenContent(
        childName = "Alex", totalXp = 450, streakDays = 5, currentLevel = 3,
        levelProgress = 0.25f, gamesPlayedToday = 1, skillStats = emptyList(),
        badges = listOf("xp_100"), ageGroup = AgeGroup.MOON_RIDERS,
        onNavigateToGame = {}, onNavigateToProgress = {}
    )
}

@Preview(device = Devices.NEXUS_7, widthDp = 600, heightDp = 960, name = "7-inch 3")
@Composable
fun Screenshot3_Tablet7() = MarketingScreenshot(
    caption = "Master Every Letter\nand Sound",
    aspectRatio = 600f / 960f
) {
    AbcAdventureContent(onBack = {}, onGameFinished = {})
}

@Preview(device = Devices.NEXUS_7, widthDp = 600, heightDp = 960, name = "7-inch 4")
@Composable
fun Screenshot4_Tablet7() = MarketingScreenshot(
    caption = "Track Your\nReading Progress",
    aspectRatio = 600f / 960f
) {
    ProgressScreenContent(
        childName = "Alex", totalXp = 450, streakDays = 5, currentLevel = 3,
        levelProgress = 0.25f, skillStats = emptyList(), allProgress = emptyList(),
        badges = listOf("xp_100"), onBack = {}
    )
}

@Preview(device = Devices.NEXUS_7, widthDp = 600, heightDp = 960, name = "7-inch 5")
@Composable
fun Screenshot5_Tablet7() = MarketingScreenshot(
    caption = "Fun Games to\nBuild Vocabulary",
    aspectRatio = 600f / 960f
) {
    WordMatchContent(onBack = {}, onGameFinished = { _, _, _ -> })
}

@Preview(device = Devices.NEXUS_7, widthDp = 600, heightDp = 960, name = "7-inch 6")
@Composable
fun Screenshot6_Tablet7() = MarketingScreenshot(
    caption = "Become a\nSight Word Ninja!",
    aspectRatio = 600f / 960f
) {
    SightWordNinjaContent(onBack = {}, onGameFinished = { _, _, _ -> })
}

// ─────────────────────────────────────────────────────────────────────
// 10-INCH TABLET SCREENSHOTS (Pixel C)
// ─────────────────────────────────────────────────────────────────────

@Preview(device = Devices.PIXEL_C, widthDp = 900, heightDp = 1280, name = "10-inch 1")
@Composable
fun Screenshot1_Tablet10() = MarketingScreenshot(
    caption = "Start Your Wild\nAdventure!",
    useDarkDeviceBackground = true,
    aspectRatio = 900f / 1280f
) { WelcomeStep() }

@Preview(device = Devices.PIXEL_C, widthDp = 900, heightDp = 1280, name = "10-inch 2")
@Composable
fun Screenshot2_Tablet10() = MarketingScreenshot(
    caption = "Pick Your\nLearning Journey",
    aspectRatio = 900f / 1280f
) {
    HomeScreenContent(
        childName = "Alex", totalXp = 450, streakDays = 5, currentLevel = 3,
        levelProgress = 0.25f, gamesPlayedToday = 1, skillStats = emptyList(),
        badges = listOf("xp_100"), ageGroup = AgeGroup.MOON_RIDERS,
        onNavigateToGame = {}, onNavigateToProgress = {}
    )
}

@Preview(device = Devices.PIXEL_C, widthDp = 900, heightDp = 1280, name = "10-inch 3")
@Composable
fun Screenshot3_Tablet10() = MarketingScreenshot(
    caption = "Master Every Letter\nand Sound",
    aspectRatio = 900f / 1280f
) {
    AbcAdventureContent(onBack = {}, onGameFinished = {})
}

@Preview(device = Devices.PIXEL_C, widthDp = 900, heightDp = 1280, name = "10-inch 4")
@Composable
fun Screenshot4_Tablet10() = MarketingScreenshot(
    caption = "Track Your\nReading Progress",
    aspectRatio = 900f / 1280f
) {
    ProgressScreenContent(
        childName = "Alex", totalXp = 450, streakDays = 5, currentLevel = 3,
        levelProgress = 0.25f, skillStats = emptyList(), allProgress = emptyList(),
        badges = listOf("xp_100"), onBack = {}
    )
}

@Preview(device = Devices.PIXEL_C, widthDp = 900, heightDp = 1280, name = "10-inch 5")
@Composable
fun Screenshot5_Tablet10() = MarketingScreenshot(
    caption = "Fun Games to\nBuild Vocabulary",
    aspectRatio = 900f / 1280f
) {
    WordMatchContent(onBack = {}, onGameFinished = { _, _, _ -> })
}

@Preview(device = Devices.PIXEL_C, widthDp = 900, heightDp = 1280, name = "10-inch 6")
@Composable
fun Screenshot6_Tablet10() = MarketingScreenshot(
    caption = "Become a\nSight Word Ninja!",
    aspectRatio = 900f / 1280f
) {
    SightWordNinjaContent(onBack = {}, onGameFinished = { _, _, _ -> })
}
