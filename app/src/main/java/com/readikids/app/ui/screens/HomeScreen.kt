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
import androidx.compose.ui.draw.shadow
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

// Updated game cards to use warm new palette
private val gameCards = listOf(
    GameCard("abc_adventure", "ABC Adventure", "Tap letters, hear sounds!", "🔤", Coral, CoralLight, "Phonics"),
    GameCard("word_match", "Word Match", "Match words to pictures!", "🃏", Grape, GrapeLight, "Vocabulary"),
    GameCard("rhyme_time", "Rhyme Time", "Find the rhyming words!", "🎵", Lime, LimeLight, "Rhyming"),
    GameCard("story_builder", "Story Builder", "Fill the missing word!", "📖", Sky, SkyLight, "Reading"),
    GameCard("spell_blast", "Spell Blast", "Unscramble letters!", "✏️", Tangerine, TangerineLight, "Spelling"),
    GameCard("sight_word_ninja", "Sight Word Ninja", "Tap the right words fast!", "👁️", Teal, TealLight, "Sight Words"),
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

    HomeScreenContent(
        childName = childName,
        totalXp = totalXp,
        streakDays = streakDays,
        currentLevel = currentLevel,
        levelProgress = levelProgress,
        gamesPlayedToday = gamesPlayedToday,
        skillStats = skillStats,
        badges = badges,
        ageGroup = ageGroup,
        onNavigateToGame = onNavigateToGame,
        onNavigateToProgress = onNavigateToProgress
    )
}

@Composable
fun HomeScreenContent(
    childName: String,
    totalXp: Int,
    streakDays: Int,
    currentLevel: Int,
    levelProgress: Float,
    gamesPlayedToday: Int,
    skillStats: List<SkillStat>,
    badges: List<String>,
    ageGroup: AgeGroup,
    onNavigateToGame: (String) -> Unit,
    onNavigateToProgress: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    // Gentle bounce for the trophy / decorative elements
    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = -14f,
        animationSpec = infiniteRepeatable(tween(2000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "floatY"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // ── Hero Header ────────────────────────────────────────────────
        item(span = { GridItemSpan(2) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Coral, Color(0xFFFF8C61), Tangerine)  // Warm coral → orange gradient
                        )
                    )
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 28.dp)
            ) {
                // Large decorative emoji watermark
                Text(
                    "📚", fontSize = 180.sp,
                    modifier = Modifier.align(Alignment.TopEnd).offset(x = 24.dp, y = (-16).dp),
                    color = Color.White.copy(alpha = 0.07f)
                )

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                "Hey, $childName! 🌈",
                                color = Color.White.copy(alpha = 0.92f),
                                fontSize = 16.sp,        // Increased from 14sp
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                "Let's Read Today!",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 30.sp,        // Increased from 26sp
                                lineHeight = 36.sp
                            )
                        }
                        // Streak badge — bubbly and round
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))   // More rounded
                                .background(Color.White.copy(alpha = 0.25f))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🔥", fontSize = 28.sp)           // Bigger: 24→28sp
                            Text("$streakDays", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
                            Text("days", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                            color = Color.White.copy(alpha = 0.92f),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                        // XP badge with sunshine highlight
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Sunshine)
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text("⭐ $totalXp XP", color = DeepInk, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    XpProgressBar(progress = levelProgress, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        // ── Daily Challenge ────────────────────────────────────────────
        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(8.dp, RoundedCornerShape(28.dp))    // Soft shadow
                    .clip(RoundedCornerShape(28.dp))             // More rounded
                    .background(Brush.linearGradient(listOf(Sunshine, Tangerine)))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "🌟 Daily Challenge",
                            color = DeepInk,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp        // Increased from 16sp
                        )
                        Text(
                            "Play 3 games to win bonus XP!",
                            color = DeepInk.copy(alpha = 0.75f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(10.dp))
                        XpProgressBar(
                            progress = (gamesPlayedToday.coerceAtMost(3)) / 3f,
                            color = Coral,
                            modifier = Modifier.fillMaxWidth(0.80f),
                            height = 16.dp
                        )
                        Text(
                            "$gamesPlayedToday / 3 done",
                            color = DeepInk.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                    Text("🏆", fontSize = 64.sp, modifier = Modifier.offset(y = floatY.dp))  // Bigger: 52→64sp
                }
            }
        }

        // ── Featured Game – Today's Pick ───────────────────────────────
        item(span = { GridItemSpan(2) }) {
            val featured = gameCards.first()
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(10.dp, RoundedCornerShape(28.dp))
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(featured.color, featured.color.copy(alpha = 0.75f))
                        )
                    )
                    .clickable { onNavigateToGame(featured.id) }
                    .padding(22.dp)
            ) {
                // Background decorative large emoji
                Text(
                    featured.emoji, fontSize = 140.sp,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = 28.dp, y = 8.dp)
                        .rotate(-10f),
                    color = Color.White.copy(alpha = 0.10f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // Large icon box — more rounded, bigger
                    Box(
                        modifier = Modifier
                            .size(88.dp)                   // Increased from 80dp
                            .clip(RoundedCornerShape(26.dp))
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(featured.emoji, fontSize = 50.sp)   // Increased from 44sp
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color.White.copy(alpha = 0.30f))
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text("✨ Today's Pick", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(featured.name, color = Color.White, fontWeight = FontWeight.Black, fontSize = 22.sp, lineHeight = 28.sp)
                        Text(featured.desc, color = Color.White.copy(alpha = 0.90f), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        // Play Now — big, prominent, pill-shaped CTA
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color.White)
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                "▶  Play Now",
                                color = featured.color,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // ── Games Section Title ────────────────────────────────────────
        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(28.dp))
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Coral.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎮", fontSize = 18.sp)
                }
                Text(
                    "All Games",
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,              // Increased from 20sp
                    color = DeepInk
                )
            }
            Spacer(Modifier.height(14.dp))
        }

        // ── Games Grid ─────────────────────────────────────────────────
        items(gameCards) { card ->
            GameCardItem(
                card = card,
                onClick = { onNavigateToGame(card.id) },
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            )
        }

        // ── Skills Section ─────────────────────────────────────────────
        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(28.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Teal.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📊", fontSize = 18.sp)
                    }
                    Text("My Skills", fontWeight = FontWeight.Black, fontSize = 22.sp, color = DeepInk)
                }
                TextButton(onClick = onNavigateToProgress) {
                    Text("View All →", color = Coral, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // ── Skills Card ────────────────────────────────────────────────
        item(span = { GridItemSpan(2) }) {
            val skillDisplay = listOf(
                Triple("phonics", "🔤 Phonics", Coral),
                Triple("vocabulary", "📝 Vocabulary", Grape),
                Triple("comprehension", "💡 Comprehension", Sky),
                Triple("spelling", "✏️ Spelling", Tangerine),
            )

            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(28.dp),    // More rounded
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
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

        // ── Badges Section ─────────────────────────────────────────────
        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(28.dp))
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Sunshine.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏅", fontSize = 18.sp)
                }
                Text("My Badges", fontWeight = FontWeight.Black, fontSize = 22.sp, color = DeepInk)
            }
            Spacer(Modifier.height(14.dp))
        }

        // ── Badges Row ─────────────────────────────────────────────────
        item(span = { GridItemSpan(2) }) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)   // Increased spacing
            ) {
                items(Achievements.all) { achievement ->
                    val unlocked = badges.contains(achievement.id)
                    BadgeItem(achievement = achievement, unlocked = unlocked)
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun GameCardItem(card: GameCard, onClick: () -> Unit, modifier: Modifier = Modifier) {
    var pressed by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .aspectRatio(0.82f)
            .clickable {
                pressed = true
                onClick()
            },
        shape = RoundedCornerShape(28.dp),    // More rounded: 22→28dp
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp, pressedElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(18.dp),  // More padding: 16→18dp
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon box — larger, more rounded
            Box(
                modifier = Modifier
                    .size(80.dp)                    // Increased from 72dp
                    .clip(RoundedCornerShape(22.dp))
                    .background(card.bgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(card.emoji, fontSize = 44.sp)  // Increased from 38sp
            }

            Column {
                // Tag pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(card.bgColor)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(card.tag, color = card.color, fontSize = 11.sp, fontWeight = FontWeight.Black)
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    card.name,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,           // Increased from 15sp
                    color = DeepInk,
                    lineHeight = 22.sp
                )
                Text(
                    card.desc,
                    fontSize = 13.sp,           // Increased from 12sp
                    color = Color.Gray,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun BadgeItem(achievement: Achievement, unlocked: Boolean) {
    Column(
        modifier = Modifier
            .width(96.dp)                   // Increased from 80dp
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (unlocked)
                    Brush.verticalGradient(listOf(SunshineLight, CoralLight))  // Warm gradient when unlocked
                else
                    Brush.verticalGradient(listOf(Color(0xFFF5F5F5), Color(0xFFEEEEEE)))
            )
            .border(
                width = if (unlocked) 3.dp else 1.dp,
                color = if (unlocked) Sunshine else Color(0xFFDDDDDD),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(achievement.emoji, fontSize = 36.sp)   // Increased from 28sp
        Spacer(Modifier.height(5.dp))
        Text(
            achievement.name,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (unlocked) Tangerine else Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}
