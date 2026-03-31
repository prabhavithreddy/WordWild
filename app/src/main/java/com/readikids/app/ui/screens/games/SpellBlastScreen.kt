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
    SpellBlastContent(
        onBack = onBack,
        onGameFinished = { score, correctCount ->
            viewModel.recordGameResult("spell_blast", score, correctCount, SPELL_WORDS.size, "spelling")
        }
    )
}

@Composable
fun SpellBlastContent(
    onBack: () -> Unit,
    onGameFinished: (Int, Int) -> Unit
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

    Column(modifier = Modifier.fillMaxSize().background(WarmCream)) {
        GameTopBar("Spell Blast", "✏️", score, Tangerine, onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Word ${wordIndex + 1} of ${SPELL_WORDS.size}",
                color = DeepInk.copy(alpha = 0.8f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(Modifier.height(16.dp))

            // Word card with emoji and hint
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = TangerineLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(spellWord.emoji, fontSize = 96.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        spellWord.hint,
                        color = DeepInk,
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        lineHeight = 28.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
            Text(
                "Arrange the letters to spell the word!",
                color = DeepInk.copy(alpha = 0.8f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))

            // Placed letters row (answer blanks)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                placed.forEachIndexed { slotIdx, letter ->
                    val bgColor = when {
                        showResult && isCorrect -> LimeLight
                        showResult && !isCorrect -> SunshineLight   // Warm yellow, not red
                        letter != null -> TangerineLight
                        else -> Color.White
                    }
                    val borderColor = when {
                        showResult && isCorrect -> Lime
                        showResult && !isCorrect -> Sunshine          // Warm, not red
                        letter != null -> Tangerine
                        else -> Color(0xFFE5E7EB)
                    }

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(bgColor)
                            .border(4.dp, borderColor, RoundedCornerShape(20.dp))
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
                            fontSize = 28.sp,
                            color = when {
                                showResult && isCorrect -> Lime
                                showResult && !isCorrect -> Tangerine  // Warm, not red
                                else -> Tangerine
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
                            .size(60.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Tangerine)
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
                        Text(letter.toString(), fontWeight = FontWeight.Black, fontSize = 28.sp, color = Color.White)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Result
            if (showResult) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isCorrect) LimeLight else SunshineLight)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            if (isCorrect) "🎉 Correct! Well done!" else "💡 The word was: ${spellWord.word}",
                            color = if (isCorrect) Lime else Tangerine,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (wordIndex + 1 < SPELL_WORDS.size) {
                    BouncyButton(
                        onClick = { wordIndex++ },
                        color = Tangerine, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Next Word →", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
                    }
                } else {
                    BouncyButton(
                        onClick = {
                            onGameFinished(score, correctCount)
                            wordIndex = 0; score = 0; correctCount = 0
                        },
                        color = Tangerine, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🎊 Finished! Play Again", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
                    }
                }
            } else {
                // Clear button
                BouncyButton(
                    onClick = {
                        val wordLetters = spellWord.word.toList()
                            .plus(('A'..'Z').filter { it !in spellWord.word }.shuffled().take(3))
                            .shuffled()
                        availableLetters = wordLetters.mapIndexed { i, c -> IndexedValue(i, c) }
                        placed = List(spellWord.word.length) { null }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    color = Tangerine.copy(alpha = 0.8f)
                ) {
                    Text("🔄 Reset Letters", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                }
            }
        }
    }
}
