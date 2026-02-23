package com.readikids.app.ui.screens.games

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

private data class RhymeRound(val target: String, val options: List<String>, val correct: Set<String>)

private val RHYME_ROUNDS = listOf(
    RhymeRound("CAT", listOf("BAT", "DOG", "HAT", "SUN"), setOf("BAT", "HAT")),
    RhymeRound("CAKE", listOf("LAKE", "FISH", "SNAKE", "BIRD"), setOf("LAKE", "SNAKE")),
    RhymeRound("BALL", listOf("FALL", "TREE", "TALL", "STAR"), setOf("FALL", "TALL")),
    RhymeRound("NIGHT", listOf("LIGHT", "MOON", "BRIGHT", "SUN"), setOf("LIGHT", "BRIGHT")),
    RhymeRound("FROG", listOf("DOG", "LOG", "CAT", "HOG"), setOf("DOG", "LOG", "HOG")),
    RhymeRound("RAIN", listOf("CHAIN", "BIRD", "PLAIN", "FISH"), setOf("CHAIN", "PLAIN")),
    RhymeRound("BEAR", listOf("CHAIR", "FISH", "STARE", "MOON"), setOf("CHAIR", "STARE")),
)

@Composable
fun RhymeTimeScreen(
    onBack: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    var roundIndex by remember { mutableStateOf(0) }
    var chosen by remember { mutableStateOf(setOf<String>()) }
    var submitted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var correctRounds by remember { mutableStateOf(0) }
    var totalRounds by remember { mutableStateOf(0) }

    val round = RHYME_ROUNDS[roundIndex % RHYME_ROUNDS.size]
    val isPerfect = chosen == round.correct

    Column(modifier = Modifier.fillMaxSize().background(SkyBackground)) {
        GameTopBar("Rhyme Time", "🎵", score, Green80, onBack)

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Round indicator
            Text(
                "Round ${(roundIndex % RHYME_ROUNDS.size) + 1} of ${RHYME_ROUNDS.size}",
                color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(bottom = 16.dp)
            )

            // Target word
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = GreenLight),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Find words that rhyme with:", color = Color.Gray, fontSize = 13.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(round.target, fontWeight = FontWeight.Black, fontSize = 48.sp, color = Green80)
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("Select all rhyming words:", color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(Modifier.height(12.dp))

            // Options grid
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                round.options.chunked(2).forEach { rowOpts ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowOpts.forEach { opt ->
                            val isSel = chosen.contains(opt)
                            val isCorrect = round.correct.contains(opt)
                            val bgColor = when {
                                submitted && isCorrect -> GreenLight
                                submitted && isSel && !isCorrect -> Color(0xFFFEE2E2)
                                isSel -> Green80
                                else -> Color.White
                            }
                            val borderColor = when {
                                submitted && isCorrect -> Green80
                                submitted && isSel && !isCorrect -> Red80
                                isSel -> Green80
                                else -> Color(0xFFE5E7EB)
                            }
                            val textColor = when {
                                submitted && isCorrect -> Green80
                                submitted && isSel && !isCorrect -> Red80
                                isSel -> Color.White
                                else -> DarkBlue
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(bgColor)
                                    .border(3.dp, borderColor, RoundedCornerShape(14.dp))
                                    .clickable(enabled = !submitted) {
                                        chosen = if (isSel) chosen - opt else chosen + opt
                                    }
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (submitted) {
                                        Text(if (isCorrect) "✅" else if (isSel) "❌" else "", fontSize = 14.sp)
                                    }
                                    Text(opt, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = textColor)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            if (!submitted) {
                BouncyButton(
                    onClick = {
                        submitted = true
                        totalRounds++
                        if (isPerfect) {
                            score += 20
                            correctRounds++
                        } else {
                            score += chosen.count { it in round.correct } * 5
                        }
                    },
                    color = Green80,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Check My Answer! 🎵", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                }
            } else {
                AnimatedVisibility(visible = true, enter = slideInVertically() + fadeIn()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = if (isPerfect) GreenLight else YellowLight)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(if (isPerfect) "🎉 Perfect Rhyme!" else "💡 Correct rhymes: ${round.correct.joinToString(", ")}",
                                    color = if (isPerfect) Green80 else Orange80,
                                    fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, textAlign = TextAlign.Center)
                                if (isPerfect) StarRating(stars = 3)
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        if (roundIndex + 1 < RHYME_ROUNDS.size) {
                            BouncyButton(
                                onClick = {
                                    roundIndex++
                                    chosen = emptySet()
                                    submitted = false
                                },
                                color = Green80, modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Next Round →", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            }
                        } else {
                            // Game over
                            BouncyButton(
                                onClick = {
                                    viewModel.recordGameResult("rhyme_time", score, correctRounds, totalRounds, "rhyming")
                                    roundIndex = 0; score = 0; correctRounds = 0; totalRounds = 0
                                    chosen = emptySet(); submitted = false
                                },
                                color = Green80, modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("🎊 Finished! Play Again", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
