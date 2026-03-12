package com.readikids.app.ui.screens

import androidx.compose.animation.core.*
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
        Triple("phonics", "🔤 Phonics", Coral),
        Triple("vocabulary", "📝 Vocabulary", Grape),
        Triple("comprehension", "💡 Comprehension", Sky),
        Triple("spelling", "✏️ Spelling", Tangerine),
        Triple("rhyming", "🎵 Rhyming", Lime),
        Triple("sight_words", "👁️ Sight Words", Teal),
    )

    Column(modifier = Modifier.fillMaxSize().background(WarmCream)) {
        // ── Header ─────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))  // More rounded
                .background(Brush.linearGradient(listOf(Coral, Tangerine)))         // Warm gradient
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.25f))
                        .size(48.dp)         // Larger: 40→48dp
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(Modifier.width(14.dp))
                Text(
                    "$childName's Progress 📊",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp       // Larger: 20→22sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Summary Stats Cards ─────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    Triple("⭐", "$totalXp XP", "Stars Earned"),
                    Triple("🏆", "Level $currentLevel", "Reading Level"),
                    Triple("🔥", "$streakDays days", "Streak"),
                ).forEachIndexed { index, (icon, value, label) ->
                    val cardColor = listOf(CoralLight, GrapeLight, TealLight)[index]
                    val textColor = listOf(Coral, Grape, Teal)[index]
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(22.dp),   // More rounded: 16→22dp
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(icon, fontSize = 30.sp)      // Larger: 24→30sp
                            Spacer(Modifier.height(4.dp))
                            Text(value, fontWeight = FontWeight.Black, fontSize = 16.sp, color = textColor)
                            Text(label, fontSize = 11.sp, color = textColor.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // ── Level Progress Card ─────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CoralLight),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Level $currentLevel Progress", fontWeight = FontWeight.ExtraBold, color = Coral, fontSize = 16.sp)
                        Text("${(levelProgress * 100).toInt()}%", color = Coral, fontWeight = FontWeight.Black, fontSize = 16.sp)
                    }
                    Spacer(Modifier.height(10.dp))
                    XpProgressBar(progress = levelProgress, color = Coral, modifier = Modifier.fillMaxWidth(), height = 18.dp)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "${(levelProgress * 200).toInt()} / 200 XP to Level ${currentLevel + 1}",
                        fontSize = 13.sp,
                        color = Coral.copy(alpha = 0.75f),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // ── Skills Breakdown ────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(Teal.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) { Text("📊", fontSize = 18.sp) }
                Text("Reading Skills", fontWeight = FontWeight.Black, fontSize = 20.sp, color = DeepInk)
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
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

            // ── Recent Games ────────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(Tangerine.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) { Text("🎮", fontSize = 18.sp) }
                Text("Recent Games", fontWeight = FontWeight.Black, fontSize = 20.sp, color = DeepInk)
            }
            if (allProgress.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = TangerineLight)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(28.dp), contentAlignment = Alignment.Center) {
                        Text(
                            "🎯 No games yet! Start a game to see your progress here.",
                            color = Tangerine,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            } else {
                allProgress.take(10).forEach { progress ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    progress.gameId.replace("_", " ").split(" ").joinToString(" ") {
                                        it.replaceFirstChar { c -> c.uppercase() }
                                    },
                                    fontWeight = FontWeight.ExtraBold, color = DeepInk, fontSize = 15.sp
                                )
                                Text(
                                    "${progress.correctAnswers}/${progress.totalQuestions} correct",
                                    fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                StarRating(stars = progress.starsEarned)
                                Text("+${progress.xpEarned} XP", color = Tangerine, fontWeight = FontWeight.Black, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            // ── Achievements ────────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(Sunshine.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) { Text("🏅", fontSize = 18.sp) }
                Text("Achievements", fontWeight = FontWeight.Black, fontSize = 20.sp, color = DeepInk)
            }
            Achievements.all.chunked(3).forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { achievement ->
                        val unlocked = badges.contains(achievement.id)
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (unlocked) SunshineLight else Color(0xFFF5F5F5)
                            ),
                            elevation = CardDefaults.cardElevation(if (unlocked) 4.dp else 0.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(achievement.emoji, fontSize = 34.sp)   // Larger: 28→34sp
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    achievement.name,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (unlocked) Tangerine else Color.Gray,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                    // Fill empty slots if row < 3
                    repeat(3 - row.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
            }
            Spacer(Modifier.height(28.dp))
        }
    }
}
