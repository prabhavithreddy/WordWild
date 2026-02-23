package com.readikids.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readikids.app.data.db.ProgressDao
import com.readikids.app.data.model.*
import com.readikids.app.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val prefs: PreferencesRepository,
    private val progressDao: ProgressDao
) : ViewModel() {

    val isOnboardingDone: Flow<Boolean> = prefs.isOnboardingDone
    val childName: Flow<String> = prefs.childName
    val ageGroup: Flow<AgeGroup> = prefs.ageGroup
    val totalXp: Flow<Int> = prefs.totalXp
    val streakDays: Flow<Int> = prefs.streakDays
    val badges: Flow<List<String>> = prefs.badges
    val skillStats: Flow<List<SkillStat>> = progressDao.getAllSkillStats()
    val allProgress: Flow<List<GameProgress>> = progressDao.getAllProgress()

    val startOfToday: Long
        get() {
            val cal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0)
            }
            return cal.timeInMillis
        }

    val gamesPlayedToday: Flow<Int> = progressDao.getGamesPlayedToday(startOfToday)

    // XP level calculation: every 200 XP = 1 level
    val currentLevel: Flow<Int> = totalXp.map { xp -> (xp / 200) + 1 }
    val xpToNextLevel: Flow<Int> = totalXp.map { xp -> 200 - (xp % 200) }
    val levelProgress: Flow<Float> = totalXp.map { xp -> (xp % 200) / 200f }

    fun saveProfile(name: String, avatar: String, ageGroup: AgeGroup) {
        viewModelScope.launch {
            prefs.saveChildProfile(name, avatar, ageGroup)
            prefs.markOnboardingDone()
        }
    }

    fun recordGameResult(gameId: String, score: Int, correct: Int, total: Int, skill: String) {
        viewModelScope.launch {
            if (total <= 0) return@launch // Prevent division by zero

            val accuracy = correct.toFloat() / total
            val stars = when {
                accuracy >= 0.9f -> 3
                accuracy >= 0.6f -> 2
                else -> 1
            }
            val xp = stars * 15
            progressDao.insertGameProgress(
                GameProgress(
                    gameId = gameId, score = score, starsEarned = stars,
                    xpEarned = xp, correctAnswers = correct, totalQuestions = total
                )
            )
            progressDao.updateSkillStat(skill, accuracy >= 0.6f)
            prefs.addXp(xp)
            prefs.updateStreak()
            checkAndUnlockBadges(xp)
        }
    }

    private suspend fun checkAndUnlockBadges(newXp: Int) {
        val total = prefs.totalXp.first()
        if (total >= 100) prefs.unlockBadge("xp_100")
        if (total >= 500) prefs.unlockBadge("xp_500")
        if (total >= 1000) prefs.unlockBadge("xp_1000")
    }
}
