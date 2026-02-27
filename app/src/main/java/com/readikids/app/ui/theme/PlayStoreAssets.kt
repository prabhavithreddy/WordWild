package com.readikids.app.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * GOOGLE PLAY FEATURE GRAPHIC (1024 x 500)
 * Fixed layout to prevent squashing and vertical text.
 */
@Preview(showBackground = true, widthDp = 1024, heightDp = 500)
@Composable
fun FeatureGraphicPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF6366F1), Color(0xFF312E81))
                )
            )
    ) {
        // --- Ambient Effects ---
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.1f), Color.Transparent),
                    center = Offset(0f, 0f),
                    radius = 800f
                ),
                radius = 800f,
                center = Offset(0f, 0f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1.2f)) {
                // Branded Logo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .shadow(15.dp, RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🦁", fontSize = 56.sp)
                    }
                    Spacer(Modifier.width(24.dp))
                    Text(
                        text = "WordWild",
                        maxLines = 1,
                        softWrap = false,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 84.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = NunitoFamily
                        )
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "The Wildest Learning Adventure!",
                    style = TextStyle(
                        color = Yellow80,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = NunitoFamily
                    )
                )

                Spacer(Modifier.height(32.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    listOf("🦁 Phonics", "🐘 Vocab", "📖 Stories").forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .border(1.5.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(tag, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            }

            // Mascot
            Box(
                modifier = Modifier.weight(0.8f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "🐼", 
                    fontSize = 280.sp,
                    modifier = Modifier.shadow(20.dp, CircleShape)
                )
            }
        }
    }
}

/**
 * GOOGLE PLAY APP ICON (512 x 512)
 */
@Preview(showBackground = true, widthDp = 512, heightDp = 512)
@Composable
fun AppIconPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFF4338CA)))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .size(400.dp)
                .shadow(30.dp, RoundedCornerShape(95.dp))
                .clip(RoundedCornerShape(95.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🦁", fontSize = 200.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                text = "WordWild",
                maxLines = 1,
                softWrap = false,
                style = TextStyle(
                    color = Color(0xFF4338CA),
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = NunitoFamily
                )
            )
        }
    }
}
