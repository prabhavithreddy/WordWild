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
                Brush.verticalGradient(listOf(Purple80, Color(0xFF4F46E5), Blue80))
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            // Progress dots
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) { i ->
                    Box(
                        modifier = Modifier
                            .size(if (i == step) 24.dp else 10.dp, 10.dp)
                            .clip(RoundedCornerShape(50))
                            .background(if (i <= step) Yellow80 else Color.White.copy(alpha = 0.3f))
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

            BouncyButton(
                onClick = {
                    if (step < 2) step++
                    else {
                        viewModel.saveProfile(childName.ifBlank { "Explorer" }, selectedAvatar, selectedAge)
                        onComplete()
                    }
                },
                color = Yellow80,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (step < 2) "Next →" else "Start Exploring! 🚀",
                    color = DarkBlue,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun WelcomeStep() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("🦁", fontSize = 100.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            "WordWild",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 42.sp
        )
        Text(
            "Explore the wild world of reading!",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
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
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(feature, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
        Text(avatar, fontSize = 80.sp)
        Spacer(Modifier.height(8.dp))
        Text("Who's exploring today?", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            placeholder = { Text("Explorer's name", color = Color.White.copy(alpha = 0.5f)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Yellow80,
                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(24.dp))
        Text("Pick your avatar!", color = Color.White.copy(alpha = 0.85f), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                avatars.chunked(6).forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        row.forEach { av ->
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(if (av == avatar) Yellow80 else Color.White.copy(alpha = 0.2f))
                                    .clickable { onAvatarSelect(av) }
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(av, fontSize = 28.sp)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun AgeStep(selected: AgeGroup, onSelect: (AgeGroup) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("How old are you?", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 26.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text("We'll personalize your wild adventure!", color = Color.White.copy(alpha = 0.8f), textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        AgeGroup.values().forEach { ag ->
            val isSelected = ag == selected
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) Yellow80 else Color.White.copy(alpha = 0.15f))
                    .clickable { onSelect(ag) }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(ag.icon, fontSize = 30.sp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(ag.label, fontWeight = FontWeight.ExtraBold, color = if (isSelected) DarkBlue else Color.White, fontSize = 17.sp)
                        Text(ag.ageRange, color = if (isSelected) DarkBlue.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                    }
                    if (isSelected) Text("✅", fontSize = 22.sp)
                }
            }
        }
    }
}
