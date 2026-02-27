package com.readikids.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.readikids.app.ui.screens.*
import com.readikids.app.ui.screens.games.*
import com.readikids.app.viewmodel.MainViewModel

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object AbcAdventure : Screen("abc_adventure")
    object WordMatch : Screen("word_match")
    object RhymeTime : Screen("rhyme_time")
    object StoryBuilder : Screen("story_builder")
    object SpellBlast : Screen("spell_blast")
    object SightWordNinja : Screen("sight_word_ninja")
    object Progress : Screen("progress")
}

@Composable
fun WordWildNavGraph() {
    val navController: NavHostController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()
    val isOnboardingDone by mainViewModel.isOnboardingDone.collectAsState(initial = null)

    if (isOnboardingDone == null) return // Wait for data

    NavHost(
        navController = navController,
        startDestination = if (isOnboardingDone == true) Screen.Home.route else Screen.Onboarding.route
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToGame = { gameId ->
                    navController.navigate(gameId)
                },
                onNavigateToProgress = {
                    navController.navigate(Screen.Progress.route)
                }
            )
        }

        composable(Screen.AbcAdventure.route) {
            AbcAdventureScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.WordMatch.route) {
            WordMatchScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.RhymeTime.route) {
            RhymeTimeScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.StoryBuilder.route) {
            StoryBuilderScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.SpellBlast.route) {
            SpellBlastScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.SightWordNinja.route) {
            SightWordNinjaScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Progress.route) {
            ProgressScreen(onBack = { navController.popBackStack() })
        }
    }
}
