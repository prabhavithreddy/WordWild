package com.readikids.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.readikids.app.ui.theme.*

// ─── Bouncy Button ───────────────────────────────────────────────────
// Stronger bounce effect — kids love satisfying tactile feedback
@Composable
fun BouncyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Coral,
    contentPadding: PaddingValues = PaddingValues(horizontal = 32.dp, vertical = 18.dp),
    content: @Composable RowScope.() -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,   // More bouncy than before
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    Button(
        onClick = onClick,
        modifier = modifier.scale(scale),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(50),
        contentPadding = contentPadding,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp, pressedElevation = 2.dp)
    ) {
        content()
    }
}

// ─── Game Top Bar ────────────────────────────────────────────────────
@Composable
fun GameTopBar(
    title: String,
    emoji: String,
    score: Int,
    accentColor: Color,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Large back button — kids need big tap targets
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(accentColor.copy(alpha = 0.15f))
                .size(52.dp)               // Increased from 44dp
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = accentColor)
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(emoji, fontSize = 26.sp)  // Increased from 22sp
            Text(title, style = MaterialTheme.typography.titleMedium, color = DeepInk)
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(accentColor.copy(alpha = 0.15f))
                .padding(horizontal = 18.dp, vertical = 10.dp)  // More padding
        ) {
            Text("⭐ $score", color = accentColor, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
        }
    }
}

// ─── Star Rating ─────────────────────────────────────────────────────
@Composable
fun StarRating(stars: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(3) { i ->
            Text(if (i < stars) "⭐" else "☆", fontSize = 28.sp)  // Increased from 24sp
        }
    }
}

// ─── XP Progress Bar ─────────────────────────────────────────────────
// Chunkier bar = more satisfying feedback for kids
@Composable
fun XpProgressBar(
    progress: Float,
    color: Color = Sunshine,
    modifier: Modifier = Modifier,
    height: Dp = 18.dp                 // Increased from 12dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 900, easing = EaseOutCubic),
        label = "xp_progress"
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.40f))
            .height(height)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .clip(RoundedCornerShape(50))
                .background(
                    Brush.horizontalGradient(listOf(color, color.copy(alpha = 0.8f)))
                )
        )
        // Shimmer highlight on top for a candy-like look
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(height / 2.5f)
                .align(Alignment.TopStart)
                .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                .background(Color.White.copy(alpha = 0.30f))
        )
    }
}

// ─── Skill Progress Row ───────────────────────────────────────────────
@Composable
fun SkillProgressRow(
    icon: String,
    label: String,
    percent: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "$icon $label",
                fontWeight = FontWeight.ExtraBold,
                color = DeepInk,
                fontSize = 15.sp        // Slightly larger
            )
            Text(
                "$percent%",
                color = color,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp
            )
        }
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)          // Increased from 10dp
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFEEEEEE))
        ) {
            val animPct by animateFloatAsState(
                targetValue = percent / 100f,
                animationSpec = tween(900, easing = EaseOutCubic),
                label = "skill_bar"
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(animPct)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(Brush.horizontalGradient(listOf(color, color.copy(alpha = 0.7f))))
            )
        }
    }
}

// ─── Bouncy Animated Card ─────────────────────────────────────────────
@Composable
fun AnimatedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
        label = "card_scale"
    )
    Card(
        modifier = modifier
            .scale(scale)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            ),
        shape = RoundedCornerShape(28.dp),   // More rounded — bubbly and kid-friendly
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp), content = content)
    }
}

// ─── Result Overlay ───────────────────────────────────────────────────
// Bigger, bolder, more celebratory for kids
@Composable
fun ResultOverlay(
    isCorrect: Boolean,
    message: String,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = true,
        enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)) + fadeIn()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))   // More rounded
                .background(if (isCorrect) LimeLight else Color(0xFFFFECEC))
                .border(4.dp, if (isCorrect) Lime else Strawberry, RoundedCornerShape(28.dp))
                .padding(28.dp)                    // More padding
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (isCorrect) "🎉" else "💡", fontSize = 72.sp)   // Larger: 48→72sp
                Spacer(Modifier.height(8.dp))
                Text(
                    message,
                    color = if (isCorrect) Lime else Strawberry,
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp,              // Increased from 20sp
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "Tap to continue! 👇",
                    color = DeepInk.copy(alpha = 0.6f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
