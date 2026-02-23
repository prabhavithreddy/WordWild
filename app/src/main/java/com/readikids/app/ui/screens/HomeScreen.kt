package com.readikids.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.readikids.app.data.model.*
import com.readikids.app.ui.theme.*
import com.readikids.app.viewmodel.MainViewModel

private data class GameCard(
    val id: String, val name: String, val desc: String,
    val emoji: String, val color: Color, val bgColor: Color, val tag: String
)

private val gameCards = listOf(
    GameCard("abc_adventure", "ABC Adventure", "Tap letters, hear sounds!", "🔤", Purple80, PurpleLight, "Phonics"),
    GameCard("word_match", "Word Match", "Match words to pictures!", "🃏", Pink80, PinkLight, "Vocabulary"),
    GameCard("rhyme_time", "Rhyme Time", "Find the rhyming words!", "🎵", Green80, GreenLight, "Rhyming"),
    GameCard("story_builder", "Story Builder", "Fill the missing word!", "📖", Blue80, BlueLight, "Reading"),
    GameCard("spell_blast", "Spell Blast", "Unscramble letters!", "✏️", Orange80, OrangeLight, "Spelling"),
    GameCard("sight_word_ninja", "Sight Word Ninja", "Tap the right words fast!", "👁️", Yellow80, YellowLight, "Sight Words"),
)

@Composable
fun HomeScreen(
    onNavigateToGame: (String) -> Unit,
    onNavigateToProgress: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val childName by viewModel.childName.collectAsState(initial = "Explorer")
    val totalXp by viewModel.totalXp.collectAsState(initial = 0)
    val streakDays by viewModel.streakDays.collectAsState(initial = 0)
    val currentLevel by viewModel.currentLevel.collectAsState(initial = 1)
    val levelProgress by viewModel.levelProgress.collectAsState(initial = 0f)
    val gamesPlayedToday by viewModel.gamesPlayedToday.collectAsState(initial = 0)
    val skillStats by viewModel.skillStats.collectAsState(initial = emptyList())
    val badges by viewModel.badges.collectAsState(initial = emptyList())
    val ageGroup by viewModel.ageGroup.collectAsState(initial = AgeGroup.TINY_STARS)

    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = -12f,
        animationSpec = infiniteRepeatable(tween(1800, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "floatY"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().background(SkyBackground),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Header
        item(span = { GridItemSpan(2) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                    .background(Brush.linearGradient(listOf(Purple80, Color(0xFF4F46E5), Blue80)))
                    .padding(24.dp)
            ) {
                Text(
                    "📚", fontSize = 160.sp,
                    modifier = Modifier.align(Alignment.TopEnd).offset(x = 20.dp, y = (-10).dp),
                    color = Color.White.copy(alpha = 0.06f)
                )

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text("Hi, $childName! 👋", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Let's Read Today!", color = Color.White, fontWeight = FontWeight.Black, fontSize = 26.sp, lineHeight = 30.sp)
                        }
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🔥", fontSize = 24.sp)
                            Text("$streakDays", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                            Text("days", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${ageGroup.icon} ${ageGroup.label} • Level $currentLevel",
                            color = Color.White.copy(alpha = 0.9f), fontWeight = FontWeight.Bold, fontSize = 13.sp
                        )
                        Text("$totalXp XP", color = Yellow80, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                    XpProgressBar(progress = levelProgress, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        // Daily Challenge
        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(Yellow80, Orange80)))
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("🌟 Daily Challenge", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        Text("Play 3 games to win bonus XP!", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                        Spacer(Modifier.height(8.dp))
                        XpProgressBar(
                            progress = (gamesPlayedToday.coerceAtMost(3)) / 3f,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth(0.75f)
                        )
                        Text("$gamesPlayedToday / 3 done", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                    }
                    Text("🏆", fontSize = 52.sp, modifier = Modifier.offset(y = floatY.dp))
                }
            }
        }

        // Games Section Title
        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(24.dp))
            Text(
                "🎮 Reading Games",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = DarkBlue,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(12.dp))
        }

        // Games Grid
        items(gameCards) { card ->
            GameCardItem(
                card = card,
                onClick = { onNavigateToGame(card.id) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // Skills Section Title
        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📊 My Skills", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = DarkBlue)
                TextButton(onClick = onNavigateToProgress) {
                    Text("View All", color = Purple80, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // Skills Card
        item(span = { GridItemSpan(2) }) {
            val skillDisplay = listOf(
                Triple("phonics", "🔤 Phonics", Purple80),
                Triple("vocabulary", "📝 Vocabulary", Pink80),
                Triple("comprehension", "💡 Comprehension", Blue80),
                Triple("spelling", "✏️ Spelling", Orange80),
            )

            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    skillDisplay.forEach { (skill, label, color) ->
                        val stat = skillStats.find { it.skillName == skill }
                        SkillProgressRow(
                            icon = label.split(" ")[0],
                            label = label.split(" ").drop(1).joinToString(" "),
                            percent = stat?.accuracyPercent ?: 0,
                            color = color
                        )
                    }
                }
            }
        }

        // Badges Section Title
        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(24.dp))
            Text("🏅 My Badges", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = DarkBlue, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(12.dp))
        }

        // Badges Horizontal Row (Nesting is fine here as it's horizontal)
        item(span = { GridItemSpan(2) }) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(Achievements.all) { achievement ->
                    val unlocked = badges.contains(achievement.id)
                    BadgeItem(achievement = achievement, unlocked = unlocked)
                }
            }
        }
    }
}

@Composable
private fun GameCardItem(card: GameCard, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(0.85f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(card.bgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(card.emoji, fontSize = 26.sp)
            }

            Column {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(card.bgColor)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(card.tag, color = card.color, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                }
                Spacer(Modifier.height(4.dp))
                Text(card.name, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = DarkBlue, lineHeight = 18.sp)
                Text(card.desc, fontSize = 11.sp, color = Color.Gray, lineHeight = 15.sp)
            }
        }
    }
}

@Composable
private fun BadgeItem(achievement: Achievement, unlocked: Boolean) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (unlocked) PurpleLight else Color(0xFFF3F4F6))
            .border(2.dp, if (unlocked) Purple80 else Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(achievement.emoji, fontSize = 28.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            achievement.name, fontSize = 9.sp, fontWeight = FontWeight.Bold,
            color = if (unlocked) Purple80 else Color.Gray,
            textAlign = TextAlign.Center, lineHeight = 12.sp
        )
    }
}
