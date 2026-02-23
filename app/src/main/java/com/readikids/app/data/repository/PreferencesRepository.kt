package com.readikids.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.readikids.app.data.model.AgeGroup
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "readikids_prefs")

@Singleton
class PreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val CHILD_NAME = stringPreferencesKey("child_name")
        val CHILD_AVATAR = stringPreferencesKey("child_avatar")
        val AGE_GROUP = stringPreferencesKey("age_group")
        val TOTAL_XP = intPreferencesKey("total_xp")
        val STREAK_DAYS = intPreferencesKey("streak_days")
        val LAST_PLAYED = longPreferencesKey("last_played")
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val BADGES = stringPreferencesKey("badges") // comma-separated
    }

    val childName: Flow<String> = context.dataStore.data.catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.CHILD_NAME] ?: "" }

    val ageGroup: Flow<AgeGroup> = context.dataStore.data.catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs -> AgeGroup.values().find { it.name == prefs[Keys.AGE_GROUP] } ?: AgeGroup.TINY_STARS }

    val totalXp: Flow<Int> = context.dataStore.data.catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.TOTAL_XP] ?: 0 }

    val streakDays: Flow<Int> = context.dataStore.data.catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.STREAK_DAYS] ?: 0 }

    val isOnboardingDone: Flow<Boolean> = context.dataStore.data.catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.ONBOARDING_DONE] ?: false }

    val badges: Flow<List<String>> = context.dataStore.data.catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.BADGES]?.split(",")?.filter { s -> s.isNotBlank() } ?: emptyList() }

    suspend fun saveChildProfile(name: String, avatar: String, ageGroup: AgeGroup) {
        context.dataStore.edit { prefs ->
            prefs[Keys.CHILD_NAME] = name
            prefs[Keys.CHILD_AVATAR] = avatar
            prefs[Keys.AGE_GROUP] = ageGroup.name
        }
    }

    suspend fun addXp(amount: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.TOTAL_XP] ?: 0
            prefs[Keys.TOTAL_XP] = current + amount
        }
    }

    suspend fun markOnboardingDone() {
        context.dataStore.edit { it[Keys.ONBOARDING_DONE] = true }
    }

    suspend fun updateStreak() {
        context.dataStore.edit { prefs ->
            val lastPlayed = prefs[Keys.LAST_PLAYED] ?: 0L
            val now = System.currentTimeMillis()
            val oneDayMs = 86_400_000L
            val streak = prefs[Keys.STREAK_DAYS] ?: 0
            prefs[Keys.LAST_PLAYED] = now
            prefs[Keys.STREAK_DAYS] = when {
                lastPlayed == 0L -> 1
                now - lastPlayed < oneDayMs -> streak       // same day
                now - lastPlayed < oneDayMs * 2 -> streak + 1  // next day
                else -> 1   // streak broken
            }
        }
    }

    suspend fun unlockBadge(badgeId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.BADGES]?.split(",")?.filter { it.isNotBlank() }?.toMutableList() ?: mutableListOf()
            if (!current.contains(badgeId)) {
                current.add(badgeId)
                prefs[Keys.BADGES] = current.joinToString(",")
            }
        }
    }
}
