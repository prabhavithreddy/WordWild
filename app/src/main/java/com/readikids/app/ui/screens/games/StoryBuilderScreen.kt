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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.readikids.app.ui.screens.BouncyButton
import com.readikids.app.ui.screens.GameTopBar
import com.readikids.app.ui.screens.StarRating
import com.readikids.app.ui.theme.*
import com.readikids.app.viewmodel.MainViewModel

private data class StoryQuestion(
    val before: String,
    val after: String,
    val options: List<String>,
    val answer: String,
    val emoji: String
)

private val STORY_QUESTIONS = listOf(
    StoryQuestion("The little", "jumped over the moon.", listOf("cat", "car", "cup"), "cat", "🐱"),
    StoryQuestion("She saw a big", "in the sky.", listOf("star", "stone", "stick"), "star", "⭐"),
    StoryQuestion("He ate a sweet", "for breakfast.", listOf("cake", "cave", "camp"), "cake", "🎂"),
    StoryQuestion("The fluffy", "played in the garden.", listOf("rabbit", "river", "ribbon"), "rabbit", "🐰"),
    StoryQuestion("She read her favourite", "before bed.", listOf("book", "brick", "bench"), "book", "📚"),
    StoryQuestion("The big yellow", "shone brightly.", listOf("sun", "sand", "sock"), "sun", "☀️"),
    StoryQuestion("The baby", "splashed in the water.", listOf("duck", "desk", "door"), "duck", "🦆"),
    StoryQuestion("They built a big", "out of snow.", listOf("snowman", "snack", "stamp"), "snowman", "⛄"),
)

@Composable
fun StoryBuilderScreen(
    onBack: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    var questionIndex by remember { mutableStateOf(0) }
    var chosen by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableStateOf(0) }
    var correctCount by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }

    val q = STORY_QUESTIONS[questionIndex]
    val isCorrect = chosen == q.answer

    Column(modifier = Modifier.fillMaxSize().background(WarmCream)) {
        GameTopBar("Story Builder", "📖", score, Sky, onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Question counter
            Text(
                "Story ${questionIndex + 1} of ${STORY_QUESTIONS.size}",
                color = DeepInk.copy(alpha = 0.6f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))

            // Story card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = SkyLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(q.emoji, fontSize = 72.sp)
                    Spacer(Modifier.height(16.dp))

                    // Story text with blank
                    val blankText = chosen ?: "_____"
                    val blankColor = when {
                        chosen == null -> Sky
                        isCorrect -> Lime
                        else -> Tangerine   // Tangerine provides much better text contrast than Sunshine
                    }
                    val storyText = buildAnnotatedString {
                        withStyle(SpanStyle(color = DeepInk, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)) {
                            append("${q.before} ")
                        }
                        withStyle(SpanStyle(
                            color = blankColor,
                            fontWeight = FontWeight.Black,
                            fontSize = 24.sp,
                            background = blankColor.copy(alpha = 0.15f)
                        )) {
                            append(blankText)
                        }
                        withStyle(SpanStyle(color = DeepInk, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)) {
                            append(" ${q.after}")
                        }
                    }
                    Text(storyText, textAlign = TextAlign.Center, lineHeight = 36.sp)
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(
                "Choose the right word to complete the story!",
                color = DeepInk.copy(alpha = 0.8f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))

            // Options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                q.options.forEach { opt ->
                    val isSel = chosen == opt
                    val isAns = opt == q.answer
                    val bgColor = when {
                        showResult && isAns -> LimeLight
                        showResult && isSel && !isAns -> SunshineLight   // Warm, not red
                        isSel -> Sky
                        else -> Color.White
                    }
                    val borderColor = when {
                        showResult && isAns -> Lime
                        showResult && isSel && !isAns -> Sunshine          // Warm, not red
                        isSel -> Sky
                        else -> Color(0xFFE5E7EB)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(bgColor)
                            .border(4.dp, borderColor, RoundedCornerShape(24.dp))
                            .clickable(enabled = !showResult) {
                                chosen = opt
                                showResult = true
                                if (opt == q.answer) {
                                    score += 20
                                    correctCount++
                                }
                            }
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            opt,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = when {
                                showResult && isAns -> Lime
                                showResult && isSel && !isAns -> Tangerine  // Warm, not red
                                isSel -> Color.White
                                else -> DeepInk
                            }
                        )
                    }
                }
            }

            // Result feedback
            AnimatedVisibility(
                visible = showResult,
                enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isCorrect) LimeLight else SunshineLight)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                if (isCorrect) "🎉 Great choice!" else "💡 The answer was \"${q.answer}\"",
                                color = if (isCorrect) Lime else Tangerine,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                            if (isCorrect) StarRating(stars = 3, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                    Spacer(Modifier.height(12.dp))

                    if (questionIndex + 1 < STORY_QUESTIONS.size) {
                        BouncyButton(
                            onClick = { questionIndex++; chosen = null; showResult = false },
                            color = Sky, modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Next Story →", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
                        }
                    } else {
                        BouncyButton(
                            onClick = {
                                viewModel.recordGameResult("story_builder", score, correctCount, STORY_QUESTIONS.size, "comprehension")
                                questionIndex = 0; chosen = null; showResult = false; score = 0; correctCount = 0
                            },
                            color = Sky, modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("🎊 Finished! Play Again", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}
