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
import com.readikids.app.ui.theme.*
import com.readikids.app.viewmodel.MainViewModel

private data class SpellWord(val word: String, val emoji: String, val hint: String)

private val SPELL_WORDS = listOf(
    SpellWord("CAT", "🐱", "A furry pet that meows"),
    SpellWord("DOG", "🐶", "Man's best friend"),
    SpellWord("SUN", "☀️", "It shines in the sky"),
    SpellWord("STAR", "⭐", "Twinkles at night"),
    SpellWord("FISH", "🐟", "Swims in water"),
    SpellWord("CAKE", "🎂", "A sweet birthday treat"),
    SpellWord("BIRD", "🐦", "It has feathers and wings"),
    SpellWord("FROG", "🐸", "It can jump and croak"),
    SpellWord("MOON", "🌙", "It glows at night"),
    SpellWord("RAIN", "🌧️", "Water that falls from clouds"),
)

@Composable
fun SpellBlastScreen(
    onBack: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    var wordIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var correctCount by remember { mutableStateOf(0) }
    var placed by remember { mutableStateOf(listOf<Char?>()) }
    var availableLetters by remember { mutableStateOf(listOf<IndexedValue<Char>>()) }
    var showResult by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    val spellWord = SPELL_WORDS[wordIndex]

    // Init/reset for each new word
    LaunchedEffect(wordIndex) {
        placed = List(spellWord.word.length) { null }
        val shuffled = spellWord.word.toList()
            .plus(('A'..'Z').filter { it !in spellWord.word }.shuffled().take(3))
            .shuffled()
        availableLetters = shuffled.mapIndexed { i, c -> IndexedValue(i, c) }
        showResult = false
        isCorrect = false
    }

    Column(modifier = Modifier.fillMaxSize().background(SkyBackground)) {
        GameTopBar("Spell Blast", "✏️", score, Orange80, onBack)

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Word ${wordIndex + 1} of ${SPELL_WORDS.size}", color = Color.Gray, fontSize = 13.sp)
            Spacer(Modifier.height(16.dp))

            // Word card with emoji and hint
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = OrangeLight),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(spellWord.emoji, fontSize = 64.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(spellWord.hint, color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp, textAlign = TextAlign.Center)
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("Arrange the letters to spell the word!", color = Color.Gray, fontSize = 13.sp)
            Spacer(Modifier.height(16.dp))

            // Placed letters row (answer blanks)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                placed.forEachIndexed { slotIdx, letter ->
                    val bgColor = when {
                        showResult && isCorrect -> GreenLight
                        showResult && !isCorrect -> Color(0xFFFEE2E2)
                        letter != null -> OrangeLight
                        else -> Color.White
                    }
                    val borderColor = when {
                        showResult && isCorrect -> Green80
                        showResult && !isCorrect -> Red80
                        letter != null -> Orange80
                        else -> Color(0xFFE5E7EB)
                    }

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(bgColor)
                            .border(3.dp, borderColor, RoundedCornerShape(12.dp))
                            .clickable(enabled = !showResult && letter != null) {
                                // Remove from placed, return to available
                                val removedLetter = placed[slotIdx]!!
                                placed = placed.toMutableList().also { it[slotIdx] = null }
                                availableLetters = availableLetters + IndexedValue(
                                    (availableLetters.maxOfOrNull { it.index } ?: 0) + 1, removedLetter
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            letter?.toString() ?: "",
                            fontWeight = FontWeight.Black,
                            fontSize = 24.sp,
                            color = when {
                                showResult && isCorrect -> Green80
                                showResult && !isCorrect -> Red80
                                else -> Orange80
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Available letters
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                availableLetters.forEach { (idx, letter) ->
                    val scale by animateFloatAsState(
                        targetValue = 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "letter_scale_$idx"
                    )
                    Box(
                        modifier = Modifier
                            .scale(scale)
                            .size(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Orange80)
                            .clickable(enabled = !showResult) {
                                val firstBlank = placed.indexOfFirst { it == null }
                                if (firstBlank >= 0) {
                                    placed = placed.toMutableList().also { it[firstBlank] = letter }
                                    availableLetters = availableLetters.filter { it.index != idx }

                                    // Auto-check when all placed
                                    val newPlaced = placed.toMutableList().also { it[firstBlank] = letter }
                                    if (newPlaced.none { it == null }) {
                                        val attempt = newPlaced.joinToString("")
                                        isCorrect = attempt == spellWord.word
                                        showResult = true
                                        if (isCorrect) {
                                            score += 25
                                            correctCount++
                                        }
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(letter.toString(), fontWeight = FontWeight.Black, fontSize = 24.sp, color = Color.White)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Result
            if (showResult) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isCorrect) GreenLight else YellowLight)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            if (isCorrect) "🎉 Correct! Well done!" else "💡 The word was: ${spellWord.word}",
                            color = if (isCorrect) Green80 else Orange80,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (wordIndex + 1 < SPELL_WORDS.size) {
                    BouncyButton(
                        onClick = { wordIndex++ },
                        color = Orange80, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Next Word →", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }
                } else {
                    BouncyButton(
                        onClick = {
                            viewModel.recordGameResult("spell_blast", score, correctCount, SPELL_WORDS.size, "spelling")
                            wordIndex = 0; score = 0; correctCount = 0
                        },
                        color = Orange80, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🎊 Finished! Play Again", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }
                }
            } else {
                // Clear button
                OutlinedButton(
                    onClick = {
                        val wordLetters = spellWord.word.toList()
                            .plus(('A'..'Z').filter { it !in spellWord.word }.shuffled().take(3))
                            .shuffled()
                        availableLetters = wordLetters.mapIndexed { i, c -> IndexedValue(i, c) }
                        placed = List(spellWord.word.length) { null }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(2.dp, Orange80)
                ) {
                    Text("🔄 Reset Letters", color = Orange80, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
