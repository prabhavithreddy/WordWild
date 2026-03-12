package com.readikids.app.ui.screens.games

import android.speech.tts.TextToSpeech
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.readikids.app.data.model.ALPHABET
import com.readikids.app.data.model.LetterData
import com.readikids.app.ui.screens.BouncyButton
import com.readikids.app.ui.screens.GameTopBar
import com.readikids.app.ui.theme.*
import com.readikids.app.viewmodel.MainViewModel
import java.util.*

@Composable
fun AbcAdventureScreen(
    onBack: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    AbcAdventureContent(
        onBack = onBack,
        onGameFinished = { score ->
            viewModel.recordGameResult("abc_adventure", score, 26, 26, "phonics")
        }
    )
}

@Composable
fun AbcAdventureContent(
    onBack: () -> Unit,
    onGameFinished: (Int) -> Unit
) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var selectedLetter by remember { mutableStateOf<LetterData?>(null) }
    var tappedLetters by remember { mutableStateOf(setOf<Char>()) }
    var score by remember { mutableStateOf(0) }
    var animatingLetter by remember { mutableStateOf<Char?>(null) }

    // Use a Ref to keep track of initialization to avoid multiple TTS objects
    val ttsInitialized = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!ttsInitialized.value) {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                    tts?.setPitch(1.3f)
                    tts?.setSpeechRate(0.85f)
                    ttsInitialized.value = true
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(WarmCream)
    ) {
        GameTopBar(
            title = "ABC Adventure",
            emoji = "🔤",
            score = score,
            accentColor = Coral,
            onBack = onBack
        )

        // Selected letter display
        selectedLetter?.let { letter ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = CoralLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(letter.letter.toString(), fontSize = 80.sp, fontWeight = FontWeight.Black, color = Coral)
                    Column {
                        Text(letter.emoji, fontSize = 48.sp)
                        Text(
                            "${letter.letter} is for ${letter.word}",
                            fontWeight = FontWeight.Black,
                            color = DeepInk,
                            fontSize = 20.sp
                        )
                        Text("Tap again to hear it!", color = Coral, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(CoralLight)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Tap any letter to learn it! 🎉\n${tappedLetters.size}/26 discovered",
                color = DeepInk,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(8.dp))

        // Alphabet grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 12.dp),
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
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            when {
                                isLearned -> Coral
                                selectedLetter?.letter == data.letter -> CoralLight
                                else -> Color.White
                            }
                        )
                        .border(
                            3.dp,
                            if (selectedLetter?.letter == data.letter) Coral else Color(0xFFE5E7EB),
                            RoundedCornerShape(24.dp)
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
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isLearned) Color.White else DeepInk
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Progress indicator
        if (tappedLetters.size == 26) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = LimeLight)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎊", fontSize = 64.sp)
                    Text("All Letters Learned!", fontWeight = FontWeight.Black, color = Lime, fontSize = 24.sp)
                    Text("You earned $score stars!", color = DeepInk, fontWeight = FontWeight.Black, fontSize = 18.sp)
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BouncyButton(
                            onClick = {
                                onGameFinished(score)
                                tappedLetters = emptySet()
                                selectedLetter = null
                                score = 0
                            },
                            color = Lime,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Play Again 🔄", fontWeight = FontWeight.Black, color = Color.White, fontSize = 18.sp)
                        }
                        BouncyButton(
                            onClick = {
                                onGameFinished(score)
                                onBack()
                            },
                            color = Coral,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Go Home 🏠", fontWeight = FontWeight.Black, color = Color.White, fontSize = 18.sp)
                        }
                    }
                }
            }
        } else {
            // Show progress bar
            LinearProgressIndicator(
                progress = tappedLetters.size / 26f,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 24.dp).height(18.dp).clip(RoundedCornerShape(50)),
                color = Coral,
                trackColor = Color.White
            )
        }
    }
}
