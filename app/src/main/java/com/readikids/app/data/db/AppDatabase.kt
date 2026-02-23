package com.readikids.app.data.db

import androidx.room.*
import com.readikids.app.data.model.GameProgress
import com.readikids.app.data.model.SkillStat
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    // ─ Game Progress ───────────────────────────────
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameProgress(progress: GameProgress)

    @Query("SELECT * FROM game_progress ORDER BY timestamp DESC")
    fun getAllProgress(): Flow<List<GameProgress>>

    @Query("SELECT * FROM game_progress WHERE gameId = :gameId ORDER BY timestamp DESC LIMIT 10")
    fun getProgressForGame(gameId: String): Flow<List<GameProgress>>

    @Query("SELECT SUM(xpEarned) FROM game_progress")
    fun getTotalXp(): Flow<Int?>

    @Query("SELECT COUNT(*) FROM game_progress WHERE timestamp >= :startOfDay")
    fun getGamesPlayedToday(startOfDay: Long): Flow<Int>

    // ─ Skill Stats ─────────────────────────────────
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSkillStat(stat: SkillStat)

    @Query("SELECT * FROM skill_stats")
    fun getAllSkillStats(): Flow<List<SkillStat>>

    @Query("SELECT * FROM skill_stats WHERE skillName = :skill")
    suspend fun getSkillStat(skill: String): SkillStat?

    @Transaction
    suspend fun updateSkillStat(skill: String, correct: Boolean) {
        val existing = getSkillStat(skill)
        val updated = existing?.copy(
            totalAttempts = existing.totalAttempts + 1,
            correctAttempts = if (correct) existing.correctAttempts + 1 else existing.correctAttempts,
            lastUpdated = System.currentTimeMillis()
        ) ?: SkillStat(
            skillName = skill,
            totalAttempts = 1,
            correctAttempts = if (correct) 1 else 0
        )
        upsertSkillStat(updated)
    }
}

@Database(
    entities = [GameProgress::class, SkillStat::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao

    companion object {
        const val DATABASE_NAME = "readikids_db"
    }
}
