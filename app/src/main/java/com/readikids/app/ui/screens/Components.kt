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
@Composable
fun BouncyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Purple80,
    contentPadding: PaddingValues = PaddingValues(horizontal = 28.dp, vertical = 14.dp),
    content: @Composable RowScope.() -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Button(
        onClick = onClick,
        modifier = modifier.scale(scale),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(50),
        contentPadding = contentPadding,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 2.dp)
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
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.15f))
                .size(44.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = accentColor)
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(emoji, fontSize = 22.sp)
            Text(title, style = MaterialTheme.typography.titleMedium, color = DarkBlue)
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(accentColor.copy(alpha = 0.15f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("⭐ $score", color = accentColor, fontWeight = FontWeight.ExtraBold)
        }
    }
}

// ─── Star Rating ─────────────────────────────────────────────────────
@Composable
fun StarRating(stars: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(3) { i ->
            Text(if (i < stars) "⭐" else "☆", fontSize = 24.sp)
        }
    }
}

// ─── XP Progress Bar ─────────────────────────────────────────────────
@Composable
fun XpProgressBar(
    progress: Float,
    color: Color = Yellow80,
    modifier: Modifier = Modifier,
    height: Dp = 12.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800, easing = EaseOutCubic),
        label = "xp_progress"
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.35f))
            .height(height)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .clip(RoundedCornerShape(50))
                .background(color)
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
            Text("$icon $label", fontWeight = FontWeight.Bold, color = DarkBlue)
            Text("$percent%", color = color, fontWeight = FontWeight.ExtraBold)
        }
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFE5E7EB))
        ) {
            val animPct by animateFloatAsState(
                targetValue = percent / 100f,
                animationSpec = tween(800, easing = EaseOutCubic),
                label = "skill_bar"
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(animPct)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(color)
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
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "card_scale"
    )
    Card(
        modifier = modifier
            .scale(scale)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

// ─── Result Overlay ───────────────────────────────────────────────────
@Composable
fun ResultOverlay(
    isCorrect: Boolean,
    message: String,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = true,
        enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(if (isCorrect) GreenLight else Color(0xFFFEE2E2))
                .border(3.dp, if (isCorrect) Green80 else Red80, RoundedCornerShape(20.dp))
                .padding(20.dp)
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (isCorrect) "🎉" else "💡", fontSize = 48.sp)
                Text(
                    message,
                    color = if (isCorrect) Green80 else Red80,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Text("Tap to continue", color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}
