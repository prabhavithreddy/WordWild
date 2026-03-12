package com.readikids.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.readikids.app.data.model.AgeGroup
import com.readikids.app.ui.theme.*
import com.readikids.app.viewmodel.MainViewModel

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    var step by remember { mutableStateOf(0) }
    var childName by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf("🦁") }
    var selectedAge by remember { mutableStateOf(AgeGroup.TINY_STARS) }

    val avatars = listOf("🦁","🐼","🦊","🐸","🐧","🦄","🐬","🐶","🐱","🐨","🐯","🦋")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                // Warm coral → orange → golden gradient — welcoming and energetic for kids
                Brush.verticalGradient(listOf(Coral, Color(0xFFFF8C61), Tangerine))
            )
    ) {
        // Decorative floating stars/circles in background
        Box(
            modifier = Modifier
                .size(180.dp)
                .offset(x = (-50).dp, y = (-50).dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.06f))
        )
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 40.dp, y = 40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.06f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            // Progress dots — larger and clearer
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(3) { i ->
                    Box(
                        modifier = Modifier
                            .size(if (i == step) 32.dp else 14.dp, 14.dp)  // Bigger: 24/10 → 32/14
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (i <= step) Color.White
                                else Color.White.copy(alpha = 0.35f)
                            )
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            AnimatedContent(targetState = step, label = "step") { s ->
                when (s) {
                    0 -> WelcomeStep()
                    1 -> NameStep(
                        name = childName,
                        avatar = selectedAvatar,
                        avatars = avatars,
                        onNameChange = { childName = it },
                        onAvatarSelect = { selectedAvatar = it }
                    )
                    else -> AgeStep(
                        selected = selectedAge,
                        onSelect = { selectedAge = it }
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // Big, chunky CTA button
            BouncyButton(
                onClick = {
                    if (step < 2) step++
                    else {
                        viewModel.saveProfile(childName.ifBlank { "Explorer" }, selectedAvatar, selectedAge)
                        onComplete()
                    }
                },
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (step < 2) "Next →" else "Start Exploring! 🚀",
                    color = Coral,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp            // Increased from 18sp
                )
            }
        }
    }
}

@Composable
fun WelcomeStep() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("🦁", fontSize = 120.sp)    // Larger: 100→120sp
        Spacer(Modifier.height(16.dp))
        Text(
            "WordWild",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 48.sp              // Larger: 42→48sp
        )
        Text(
            "Explore the wild world of reading!",
            color = Color.White.copy(alpha = 0.90f),
            fontSize = 18.sp,             // Larger: 17→18sp
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp)
        )
        Spacer(Modifier.height(32.dp))
        listOf(
            "🎮 6 wild reading games",
            "⭐ Earn XP & level up",
            "🔥 Daily streaks & badges",
            "📊 Track your adventure"
        ).forEach { feature ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(18.dp))   // More rounded: 12→18dp
                    .background(Color.White.copy(alpha = 0.20f))
                    .padding(horizontal = 16.dp, vertical = 14.dp),  // More padding
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    feature,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp    // Larger: 15→16sp
                )
            }
        }
    }
}

@Composable
fun NameStep(
    name: String, avatar: String, avatars: List<String>,
    onNameChange: (String) -> Unit, onAvatarSelect: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(avatar, fontSize = 96.sp)    // Larger: 80→96sp
        Spacer(Modifier.height(8.dp))
        Text(
            "Who's exploring today?",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 26.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))
        // Name input
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            placeholder = {
                Text(
                    "Explorer's name",
                    color = Color.White.copy(alpha = 0.55f),
                    fontWeight = FontWeight.Bold
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White.copy(alpha = 0.55f),
            ),
            textStyle = LocalTextStyle.current.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            ),
            shape = RoundedCornerShape(20.dp),  // More rounded: 16→20dp
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(24.dp))
        Text(
            "Pick your avatar! 👇",
            color = Color.White.copy(alpha = 0.90f),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 17.sp
        )
        Spacer(Modifier.height(14.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                avatars.chunked(6).forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        row.forEach { av ->
                            Box(
                                modifier = Modifier
                                    .size(64.dp)               // Larger tap target: 52→64dp
                                    .clip(CircleShape)
                                    .background(
                                        if (av == avatar)
                                            Brush.radialGradient(listOf(Color.White, Sunshine))
                                        else
                                            Brush.radialGradient(listOf(Color.White.copy(alpha = 0.25f), Color.White.copy(alpha = 0.15f)))
                                    )
                                    .shadow(if (av == avatar) 6.dp else 0.dp, CircleShape)
                                    .clickable { onAvatarSelect(av) }
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(av, fontSize = 34.sp)   // Larger: 28→34sp
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun AgeStep(selected: AgeGroup, onSelect: (AgeGroup) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("How old are you?", color = Color.White, fontWeight = FontWeight.Black, fontSize = 28.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(
            "We'll personalize your wild adventure!",
            color = Color.White.copy(alpha = 0.85f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Spacer(Modifier.height(24.dp))
        AgeGroup.values().forEach { ag ->
            val isSelected = ag == selected
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(22.dp))   // More rounded: 16→22dp
                    .background(
                        if (isSelected)
                            Brush.horizontalGradient(listOf(Color.White, SunshineLight))
                        else
                            Brush.horizontalGradient(listOf(Color.White.copy(alpha = 0.20f), Color.White.copy(alpha = 0.12f)))
                    )
                    .clickable { onSelect(ag) }
                    .padding(18.dp),                   // More padding: 16→18dp
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(ag.icon, fontSize = 36.sp)   // Larger: 30→36sp
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            ag.label,
                            fontWeight = FontWeight.Black,
                            color = if (isSelected) DeepInk else Color.White,
                            fontSize = 18.sp
                        )
                        Text(
                            ag.ageRange,
                            color = if (isSelected) DeepInk.copy(alpha = 0.65f) else Color.White.copy(alpha = 0.75f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (isSelected) Text("✅", fontSize = 24.sp)
                }
            }
        }
    }
}
