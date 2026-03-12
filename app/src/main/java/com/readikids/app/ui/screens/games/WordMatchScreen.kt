package com.readikids.app.ui.screens.games

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.readikids.app.ui.screens.BouncyButton
import com.readikids.app.ui.screens.GameTopBar
import com.readikids.app.ui.screens.StarRating
import com.readikids.app.ui.theme.*
import com.readikids.app.viewmodel.MainViewModel

private data class MatchPair(val word: String, val emoji: String)

private val ALL_PAIRS = listOf(
    MatchPair("CAT", "🐱"), MatchPair("DOG", "🐶"), MatchPair("SUN", "☀️"),
    MatchPair("STAR", "⭐"), MatchPair("FISH", "🐟"), MatchPair("MOON", "🌙"),
    MatchPair("CAKE", "🎂"), MatchPair("BIRD", "🐦"), MatchPair("TREE", "🌳"),
    MatchPair("BOAT", "🚤"), MatchPair("APPLE", "🍎"), MatchPair("BALL", "⚽"),
)

@Composable
fun WordMatchScreen(
    onBack: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    WordMatchContent(
        onBack = onBack,
        onGameFinished = { score, correct, total ->
            viewModel.recordGameResult("word_match", score, correct, total, "vocabulary")
        }
    )
}

@Composable
fun WordMatchContent(
    onBack: () -> Unit,
    onGameFinished: (Int, Int, Int) -> Unit
) {
    var pairs by remember { mutableStateOf(ALL_PAIRS.shuffled().take(6)) }
    var emojiOrder by remember { mutableStateOf(pairs.shuffled()) }
    var selectedWord by remember { mutableStateOf<String?>(null) }
    var matchedPairs by remember { mutableStateOf(setOf<String>()) }
    var shakingWord by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableStateOf(0) }
    var wrongAttempts by remember { mutableStateOf(0) }
    var gameComplete by remember { mutableStateOf(false) }

    LaunchedEffect(matchedPairs.size) {
        if (matchedPairs.size == pairs.size) {
            gameComplete = true
            val total = pairs.size
            val correct = total // all correct at end
            onGameFinished(score, correct, total)
        }
    }

    val stars = when {
        wrongAttempts == 0 -> 3
        wrongAttempts <= 2 -> 2
        else -> 1
    }

    Column(modifier = Modifier.fillMaxSize().background(WarmCream)) {
        GameTopBar("Word Match", "🃏", score, Coral, onBack)

        if (gameComplete) {
            // Completion view
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🎊", fontSize = 80.sp)
                Spacer(Modifier.height(12.dp))
                Text("Amazing!", fontWeight = FontWeight.Black, color = DeepInk, fontSize = 32.sp)
                Text("You matched all words!", color = Color.Gray, fontSize = 16.sp)
                Spacer(Modifier.height(16.dp))
                StarRating(stars = stars)
                Spacer(Modifier.height(12.dp))
                Text("Score: $score ⭐", fontWeight = FontWeight.Black, color = Coral, fontSize = 28.sp)
                Spacer(Modifier.height(28.dp))
                BouncyButton(
                    onClick = {
                        pairs = ALL_PAIRS.shuffled().take(6)
                        emojiOrder = pairs.shuffled()
                        selectedWord = null
                        matchedPairs = emptySet()
                        score = 0
                        wrongAttempts = 0
                        gameComplete = false
                    },
                    color = Coral
                ) {
                    Text("Play Again! 🔄", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                }
            }
        } else {
            Text(
                "Tap a word, then match it to the right picture!",
                color = DeepInk.copy(alpha = 0.8f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Words column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pairs.forEach { pair ->
                        val isMatched = matchedPairs.contains(pair.word)
                        val isSelected = selectedWord == pair.word
                        val shakeAnim by animateFloatAsState(
                            targetValue = if (shakingWord == pair.word) 1f else 0f,
                            animationSpec = if (shakingWord == pair.word)
                                keyframes { durationMillis = 400; 0f at 0; 8f at 50; -8f at 100; 8f at 150; -8f at 200; 0f at 300 }
                            else spring(),
                            label = "shake"
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(x = shakeAnim.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(
                                    when {
                                        isMatched -> LimeLight
                                        isSelected -> Coral
                                        else -> Color.White
                                    }
                                )
                                .border(
                                    4.dp,
                                    when {
                                        isMatched -> Lime
                                        isSelected -> Coral
                                        else -> Color(0xFFE5E7EB)
                                    },
                                    RoundedCornerShape(24.dp)
                                )
                                .clickable(enabled = !isMatched) {
                                    selectedWord = if (isSelected) null else pair.word
                                }
                                .padding(14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (isMatched) "✅ ${pair.word}" else pair.word,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = when {
                                    isMatched -> Lime
                                    isSelected -> Color.White
                                    else -> DeepInk
                                }
                            )
                        }
                    }
                }

                // Emojis column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    emojiOrder.forEach { pair ->
                        val isMatched = matchedPairs.contains(pair.word)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(if (isMatched) LimeLight else Color.White)
                                .border(4.dp, if (isMatched) Lime else Color(0xFFE5E7EB), RoundedCornerShape(24.dp))
                                .clickable(enabled = !isMatched && selectedWord != null) {
                                    val sel = selectedWord ?: return@clickable
                                    val target = pairs.find { it.word == sel } ?: return@clickable
                                    if (target.emoji == pair.emoji) {
                                        matchedPairs = matchedPairs + sel
                                        score += 15
                                        selectedWord = null
                                    } else {
                                        wrongAttempts++
                                        shakingWord = sel
                                    }
                                }
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(pair.emoji, fontSize = 48.sp)
                        }
                    }
                }
            }
        }
    }
}
