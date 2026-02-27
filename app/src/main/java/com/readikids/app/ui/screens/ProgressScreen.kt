package com.readikids.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.readikids.app.data.model.*
import com.readikids.app.ui.theme.*
import com.readikids.app.viewmodel.MainViewModel

@Composable
fun ProgressScreen(
    onBack: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val childName by viewModel.childName.collectAsState(initial = "Explorer")
    val totalXp by viewModel.totalXp.collectAsState(initial = 0)
    val streakDays by viewModel.streakDays.collectAsState(initial = 0)
    val currentLevel by viewModel.currentLevel.collectAsState(initial = 1)
    val levelProgress by viewModel.levelProgress.collectAsState(initial = 0f)
    val skillStats by viewModel.skillStats.collectAsState(initial = emptyList())
    val allProgress by viewModel.allProgress.collectAsState(initial = emptyList())
    val badges by viewModel.badges.collectAsState(initial = emptyList())

    ProgressScreenContent(
        childName = childName,
        totalXp = totalXp,
        streakDays = streakDays,
        currentLevel = currentLevel,
        levelProgress = levelProgress,
        skillStats = skillStats,
        allProgress = allProgress,
        badges = badges,
        onBack = onBack
    )
}

@Composable
fun ProgressScreenContent(
    childName: String,
    totalXp: Int,
    streakDays: Int,
    currentLevel: Int,
    levelProgress: Float,
    skillStats: List<SkillStat>,
    allProgress: List<GameProgress>,
    badges: List<String>,
    onBack: () -> Unit
) {
    val skillDisplay = listOf(
        Triple("phonics", "🔤 Phonics", Purple80),
        Triple("vocabulary", "📝 Vocabulary", Pink80),
        Triple("comprehension", "💡 Comprehension", Blue80),
        Triple("spelling", "✏️ Spelling", Orange80),
        Triple("rhyming", "🎵 Rhyming", Green80),
        Triple("sight_words", "👁️ Sight Words", Yellow80),
    )

    Column(modifier = Modifier.fillMaxSize().background(SkyBackground)) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(Brush.linearGradient(listOf(Purple80, Blue80)))
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.2f)).size(40.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(Modifier.width(12.dp))
                Text("$childName's Progress 📊", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary cards
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    Triple("⭐", "$totalXp XP", "Total Stars"),
                    Triple("🏆", "Level $currentLevel", "Reading Level"),
                    Triple("🔥", "$streakDays days", "Streak"),
                ).forEach { (icon, value, label) ->
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(icon, fontSize = 24.sp)
                            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = DarkBlue)
                            Text(label, fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }

            // XP Progress
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PurpleLight),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Level $currentLevel Progress", fontWeight = FontWeight.Bold, color = Purple80)
                        Text("${(levelProgress * 100).toInt()}%", color = Purple80, fontWeight = FontWeight.ExtraBold)
                    }
                    Spacer(Modifier.height(8.dp))
                    XpProgressBar(progress = levelProgress, color = Purple80, modifier = Modifier.fillMaxWidth(), height = 14.dp)
                    Spacer(Modifier.height(4.dp))
                    Text("${(levelProgress * 200).toInt()} / 200 XP to Level ${currentLevel + 1}", fontSize = 12.sp, color = Purple80.copy(alpha = 0.7f))
                }
            }

            // Skills
            Text("📊 Reading Skills Breakdown", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = DarkBlue)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    skillDisplay.forEach { (skillKey, label, color) ->
                        val stat = skillStats.find { it.skillName == skillKey }
                        SkillProgressRow(
                            icon = label.split(" ")[0],
                            label = label.split(" ").drop(1).joinToString(" "),
                            percent = stat?.accuracyPercent ?: 0,
                            color = color
                        )
                    }
                }
            }

            // Games played
            Text("🎮 Recent Games", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = DarkBlue)
            if (allProgress.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No games played yet! Start a game to see your progress here.", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            } else {
                allProgress.take(10).forEach { progress ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(progress.gameId.replace("_", " ").split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } },
                                    fontWeight = FontWeight.Bold, color = DarkBlue)
                                Text("${progress.correctAnswers}/${progress.totalQuestions} correct", fontSize = 12.sp, color = Color.Gray)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                StarRating(stars = progress.starsEarned)
                                Text("+${progress.xpEarned} XP", color = Yellow80, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Achievements
            Text("🏅 Achievements", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = DarkBlue)
            Achievements.all.chunked(3).forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { achievement ->
                        val unlocked = badges.contains(achievement.id)
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = if (unlocked) PurpleLight else Color(0xFFF9FAFB)),
                            elevation = CardDefaults.cardElevation(if (unlocked) 3.dp else 0.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(achievement.emoji, fontSize = 28.sp, modifier = Modifier.let { if (!unlocked) it else it })
                                Text(
                                    achievement.name,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (unlocked) Purple80 else Color.Gray,
                                    lineHeight = 12.sp
                                )
                            }
                        }
                    }
                    // Fill empty if row < 3
                    repeat(3 - row.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
