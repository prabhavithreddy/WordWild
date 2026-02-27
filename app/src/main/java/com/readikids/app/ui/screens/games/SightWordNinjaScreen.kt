package com.readikids.app.ui.screens.games

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.readikids.app.ui.screens.BouncyButton
import com.readikids.app.ui.screens.GameTopBar
import com.readikids.app.ui.theme.*
import com.readikids.app.viewmodel.MainViewModel
import kotlinx.coroutines.delay

private val SIGHT_WORD_SETS = listOf(
    listOf("the", "a", "is", "in", "it", "of", "to", "and"),
    listOf("he", "she", "we", "me", "my", "be", "by", "do"),
    listOf("on", "up", "at", "as", "go", "so", "no", "or"),
    listOf("all", "are", "but", "can", "for", "had", "has", "her"),
    listOf("his", "hot", "how", "if", "let", "may", "not", "now"),
)

@Composable
fun SightWordNinjaScreen(
    onBack: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    SightWordNinjaContent(
        onBack = onBack,
        onGameFinished = { score, correct, round ->
            viewModel.recordGameResult("sight_word_ninja", score, correct, round, "sight_words")
        }
    )
}

@Composable
fun SightWordNinjaContent(
    onBack: () -> Unit,
    onGameFinished: (Int, Int, Int) -> Unit
) {
    var gameState by remember { mutableStateOf<GameState>(GameState.Ready) }
    var targetWord by remember { mutableStateOf("the") }
    var displayedWords by remember { mutableStateOf(listOf<String>()) }
    var score by remember { mutableStateOf(0) }
    var lives by remember { mutableStateOf(3) }
    var round by remember { mutableStateOf(0) }
    var correctCount by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(3) }
    var flashWord by remember { mutableStateOf<Pair<String, Boolean>?>(null) } // word, isCorrect

    val wordSet = SIGHT_WORD_SETS[round % SIGHT_WORD_SETS.size]

    fun nextRound() {
        val target = wordSet.random()
        targetWord = target
        // Show 4-6 words, some are correct some are distractors
        val correctOnes = listOf(target, if (Math.random() > 0.5) target else wordSet.random())
        val distractors = wordSet.filter { it != target }.shuffled().take(3)
        displayedWords = (correctOnes + distractors).shuffled().take(6)
        timeLeft = 3
        flashWord = null
        gameState = GameState.Playing
    }

    LaunchedEffect(gameState) {
        if (gameState == GameState.Playing) {
            while (timeLeft > 0 && gameState == GameState.Playing) {
                delay(1000)
                timeLeft--
            }
            if (gameState == GameState.Playing) {
                lives--
                if (lives <= 0) {
                    onGameFinished(score, correctCount, round + 1)
                    gameState = GameState.GameOver
                } else {
                    nextRound()
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(SkyBackground)) {
        GameTopBar("Sight Word Ninja", "👁️", score, Yellow80, onBack)

        when (gameState) {
            GameState.Ready -> ReadyView(onStart = { nextRound(); round = 0; score = 0; lives = 3; correctCount = 0 })
            GameState.GameOver -> GameOverView(score = score, correctCount = correctCount, round = round,
                onRestart = { nextRound(); round = 0; score = 0; lives = 3; correctCount = 0 })
            GameState.Playing -> PlayingView(
                targetWord = targetWord,
                displayedWords = displayedWords,
                timeLeft = timeLeft,
                score = score,
                lives = lives,
                flashWord = flashWord,
                onWordTap = { tapped ->
                    if (tapped == targetWord) {
                        score += 15
                        correctCount++
                        flashWord = Pair(tapped, true)
                        round++
                        gameState = GameState.Ready
                        nextRound()
                    } else {
                        lives--
                        flashWord = Pair(tapped, false)
                        if (lives <= 0) {
                            onGameFinished(score, correctCount, round + 1)
                            gameState = GameState.GameOver
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ReadyView(onStart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("👁️", fontSize = 80.sp)
        Spacer(Modifier.height(16.dp))
        Text("Sight Word Ninja", fontWeight = FontWeight.Black, fontSize = 28.sp, color = DarkBlue)
        Text("Tap the target word before time runs out!", color = Color.Gray, fontSize = 15.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 8.dp))
        Spacer(Modifier.height(32.dp))
        BouncyButton(onClick = onStart, color = Yellow80, modifier = Modifier.fillMaxWidth()) {
            Text("Start Game! ⚡", color = DarkBlue, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
        }
    }
}

@Composable
private fun GameOverView(score: Int, correctCount: Int, round: Int, onRestart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("💀", fontSize = 72.sp)
        Spacer(Modifier.height(12.dp))
        Text("Game Over!", fontWeight = FontWeight.Black, fontSize = 32.sp, color = DarkBlue)
        Text("You reached round $round", color = Color.Gray, fontSize = 15.sp)
        Spacer(Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = YellowLight)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Final Score: $score ⭐", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = Yellow80)
                Text("Correct words: $correctCount", color = DarkBlue, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(24.dp))
        BouncyButton(onClick = onRestart, color = Yellow80, modifier = Modifier.fillMaxWidth()) {
            Text("Play Again! 🔄", color = DarkBlue, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
        }
    }
}

@Composable
private fun PlayingView(
    targetWord: String,
    displayedWords: List<String>,
    timeLeft: Int,
    score: Int,
    lives: Int,
    flashWord: Pair<String, Boolean>?,
    onWordTap: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Lives & timer
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(3) { i -> Text(if (i < lives) "❤️" else "🖤", fontSize = 22.sp) }
            }
            // Timer circle
            val timerColor = when (timeLeft) { 3 -> Green80; 2 -> Yellow80; else -> Red80 }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(50))
                    .background(timerColor),
                contentAlignment = Alignment.Center
            ) {
                Text("$timeLeft", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
            }
        }

        Spacer(Modifier.height(20.dp))

        // Target word card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = YellowLight),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Find this word:", color = Color.Gray, fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    targetWord,
                    fontWeight = FontWeight.Black,
                    fontSize = 48.sp,
                    color = DarkBlue
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Tap it below! 👇", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(12.dp))

        // Word grid
        displayedWords.chunked(2).forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { word ->
                    val isFlashed = flashWord?.first == word
                    val flashCorrect = flashWord?.second ?: false
                    val scale by animateFloatAsState(
                        targetValue = if (isFlashed) 1.15f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "word_scale"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .scale(scale)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                when {
                                    isFlashed && flashCorrect -> GreenLight
                                    isFlashed && !flashCorrect -> Color(0xFFFEE2E2)
                                    else -> Color.White
                                }
                            )
                            .border(
                                3.dp,
                                when {
                                    isFlashed && flashCorrect -> Green80
                                    isFlashed && !flashCorrect -> Red80
                                    else -> Color(0xFFE5E7EB)
                                },
                                RoundedCornerShape(14.dp)
                            )
                            .clickable { onWordTap(word) }
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            word, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp,
                            color = when {
                                isFlashed && flashCorrect -> Green80
                                isFlashed && !flashCorrect -> Red80
                                else -> DarkBlue
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}

private sealed class GameState {
    object Ready : GameState()
    object Playing : GameState()
    object GameOver : GameState()
}
