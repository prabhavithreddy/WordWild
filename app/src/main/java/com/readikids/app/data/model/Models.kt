package com.readikids.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// ─── User / Child Profile ────────────────────────────────────────────
data class ChildProfile(
    val id: String = "",
    val name: String = "",
    val avatarEmoji: String = "🦁",
    val ageGroup: AgeGroup = AgeGroup.TINY_STARS,
    val totalXp: Int = 0,
    val currentLevel: Int = 1,
    val streakDays: Int = 0,
    val lastPlayedDate: Long = 0L,
    val badges: List<String> = emptyList()
)

enum class AgeGroup(val label: String, val ageRange: String, val icon: String, val minAge: Int, val maxAge: Int) {
    TINY_STARS("Tiny Stars", "3–4 yrs", "⭐", 3, 4),
    MOON_RIDERS("Moon Riders", "4–5 yrs", "🌙", 4, 5),
    SUN_CHASERS("Sun Chasers", "5–6 yrs", "☀️", 5, 6),
    STAR_GAZERS("Star Gazers", "6–7 yrs", "🌟", 6, 7),
    SKY_HEROES("Sky Heroes", "7–8 yrs", "🚀", 7, 8);
}

// ─── Game Progress ───────────────────────────────────────────────────
@Entity(tableName = "game_progress")
data class GameProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val gameId: String,
    val score: Int,
    val starsEarned: Int,   // 1, 2, or 3
    val xpEarned: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val correctAnswers: Int,
    val totalQuestions: Int
)

// ─── Skill Stats ─────────────────────────────────────────────────────
@Entity(tableName = "skill_stats")
data class SkillStat(
    @PrimaryKey
    val skillName: String,  // "phonics", "vocabulary", "comprehension", "spelling", "rhyming", "sight_words"
    val totalAttempts: Int = 0,
    val correctAttempts: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    val accuracyPercent: Int
        get() = if (totalAttempts == 0) 0 else ((correctAttempts.toFloat() / totalAttempts) * 100).toInt()
}

// ─── Game Definitions ────────────────────────────────────────────────
data class GameInfo(
    val id: String,
    val name: String,
    val description: String,
    val emoji: String,
    val skill: String,
    val minAgeGroup: AgeGroup,
    val xpReward: Int = 30
)

object Games {
    val all = listOf(
        GameInfo("abc_adventure", "ABC Adventure", "Tap letters, hear sounds!", "🔤", "phonics", AgeGroup.TINY_STARS),
        GameInfo("word_match", "Word Match", "Match words to pictures!", "🃏", "vocabulary", AgeGroup.TINY_STARS),
        GameInfo("rhyme_time", "Rhyme Time", "Find words that rhyme!", "🎵", "rhyming", AgeGroup.MOON_RIDERS),
        GameInfo("story_builder", "Story Builder", "Fill in the missing word!", "📖", "comprehension", AgeGroup.SUN_CHASERS),
        GameInfo("spell_blast", "Spell Blast", "Unscramble the letters!", "✏️", "spelling", AgeGroup.MOON_RIDERS),
        GameInfo("sight_word_ninja", "Sight Word Ninja", "Tap the correct word fast!", "👁️", "sight_words", AgeGroup.STAR_GAZERS),
    )
}

// ─── Achievements ────────────────────────────────────────────────────
data class Achievement(
    val id: String,
    val name: String,
    val emoji: String,
    val description: String,
    val isUnlocked: Boolean = false
)

object Achievements {
    val all = listOf(
        Achievement("first_game", "First Steps", "🎉", "Complete your first game"),
        Achievement("alphabet_pro", "Alphabet Pro", "🔤", "Learn all 26 letters"),
        Achievement("rhyme_king", "Rhyme King", "🎵", "Get 3 perfect rhyme rounds"),
        Achievement("story_teller", "Story Teller", "📖", "Complete 5 story builder games"),
        Achievement("streak_3", "3-Day Streak", "🔥", "Play 3 days in a row"),
        Achievement("streak_7", "Week Warrior", "⚡", "Play 7 days in a row"),
        Achievement("streak_30", "Monthly Hero", "🏆", "Play 30 days in a row"),
        Achievement("speed_reader", "Speed Reader", "💨", "Score 100% in Sight Word Ninja"),
        Achievement("perfect_speller", "Perfect Speller", "✏️", "Complete Spell Blast without mistakes"),
        Achievement("xp_100", "Rising Star", "⭐", "Earn 100 XP"),
        Achievement("xp_500", "Super Reader", "🌟", "Earn 500 XP"),
        Achievement("xp_1000", "Reading Champion", "🏅", "Earn 1000 XP"),
    )
}
