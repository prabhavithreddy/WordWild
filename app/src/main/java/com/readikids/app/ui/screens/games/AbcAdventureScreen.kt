package com.readikids.app.ui.screens.games

import android.speech.tts.TextToSpeech
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.readikids.app.ui.screens.BouncyButton
import com.readikids.app.ui.screens.GameTopBar
import com.readikids.app.ui.theme.*
import com.readikids.app.viewmodel.MainViewModel
import java.util.Locale

private data class LetterData(val letter: Char, val emoji: String, val word: String)

private val ALPHABET = listOf(
    LetterData('A', "🍎", "Apple"), LetterData('B', "🐻", "Bear"), LetterData('C', "🐱", "Cat"),
    LetterData('D', "🐶", "Dog"), LetterData('E', "🐘", "Elephant"), LetterData('F', "🐟", "Fish"),
    LetterData('G', "🦒", "Giraffe"), LetterData('H', "🏠", "House"), LetterData('I', "🍦", "Ice Cream"),
    LetterData('J', "🕹️", "Jump"), LetterData('K', "🪁", "Kite"), LetterData('L', "🦁", "Lion"),
    LetterData('M', "🌙", "Moon"), LetterData('N', "🌰", "Nut"), LetterData('O', "🐙", "Octopus"),
    LetterData('P', "🐧", "Penguin"), LetterData('Q', "👑", "Queen"), LetterData('R', "🌈", "Rainbow"),
    LetterData('S', "⭐", "Star"), LetterData('T', "🐯", "Tiger"), LetterData('U', "☂️", "Umbrella"),
    LetterData('V', "🎻", "Violin"), LetterData('W', "🐋", "Whale"), LetterData('X', "🎸", "Xylophone"),
    LetterData('Y', "🧶", "Yarn"), LetterData('Z', "🦓", "Zebra"),
)

@Composable
fun AbcAdventureScreen(
    onBack: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var selectedLetter by remember { mutableStateOf<LetterData?>(null) }
    var tappedLetters by remember { mutableStateOf(setOf<Char>()) }
    var score by remember { mutableStateOf(0) }
    var animatingLetter by remember { mutableStateOf<Char?>(null) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.setPitch(1.3f)
                tts?.setSpeechRate(0.85f)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    // Record completion when all letters tapped
    LaunchedEffect(tappedLetters.size) {
        if (tappedLetters.size == 26) {
            viewModel.recordGameResult("abc_adventure", score, 26, 26, "phonics")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(SkyBackground)
    ) {
        GameTopBar(
            title = "ABC Adventure",
            emoji = "🔤",
            score = score,
            accentColor = Purple80,
            onBack = onBack
        )

        // Selected letter display
        selectedLetter?.let { letter ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PurpleLight),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(letter.letter.toString(), fontSize = 72.sp, fontWeight = FontWeight.Black, color = Purple80)
                    Column {
                        Text(letter.emoji, fontSize = 42.sp)
                        Text(
                            "${letter.letter} is for ${letter.word}",
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkBlue,
                            fontSize = 18.sp
                        )
                        Text("Tap again to hear it!", color = Purple80, fontSize = 12.sp)
                    }
                }
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(PurpleLight)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Tap any letter to learn it! 🎉\n${tappedLetters.size}/26 discovered",
                color = Purple80,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(8.dp))

        // Alphabet grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ALPHABET) { data ->
                val isLearned = tappedLetters.contains(data.letter)
                val isAnimating = animatingLetter == data.letter
                val scale by animateFloatAsState(
                    targetValue = if (isAnimating) 1.2f else 1f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "letter_scale"
                )

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .scale(scale)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            when {
                                isLearned -> Purple80
                                selectedLetter?.letter == data.letter -> PurpleLight
                                else -> Color.White
                            }
                        )
                        .border(
                            2.dp,
                            if (selectedLetter?.letter == data.letter) Purple80 else Purple80.copy(alpha = 0.2f),
                            RoundedCornerShape(14.dp)
                        )
                        .clickable {
                            selectedLetter = data
                            if (!tappedLetters.contains(data.letter)) {
                                tappedLetters = tappedLetters + data.letter
                                score += 5
                            }
                            animatingLetter = data.letter
                            tts?.speak(
                                "${data.letter}. ${data.word}",
                                TextToSpeech.QUEUE_FLUSH, null, null
                            )
                        }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        data.letter.toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isLearned) Color.White else DarkBlue
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Progress indicator
        if (tappedLetters.size == 26) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GreenLight)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎊", fontSize = 48.sp)
                    Text("All Letters Learned!", fontWeight = FontWeight.Black, color = Green80, fontSize = 22.sp)
                    Text("You earned $score stars!", color = DarkBlue, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    BouncyButton(
                        onClick = {
                            tappedLetters = emptySet()
                            selectedLetter = null
                            score = 0
                        },
                        color = Green80
                    ) {
                        Text("Play Again 🔄", fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }
        }
    }
}
