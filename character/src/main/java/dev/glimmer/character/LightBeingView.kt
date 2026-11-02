package dev.glimmer.character

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import dev.glimmer.core.model.PresenceState
import kotlin.math.sin

@Composable
fun LightBeingView(presence: PresenceState, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "light_being")
    val idle = presence == PresenceState.IDLE_PERFORMANCE
    val pulse by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (idle) 7000 else 3400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (idle) 30000 else 16000),
            repeatMode = RepeatMode.Restart
        ),
        label = "drift"
    )
    val targetAlpha = when (presence) {
        PresenceState.HIDDEN, PresenceState.FADING -> 0f
        PresenceState.MATERIALIZING -> 0.7f
        PresenceState.VISIBLE, PresenceState.IDLE_PERFORMANCE -> 1f
    }
    val alpha by animateFloatAsState(targetValue, tween(350, easing = FastOutSlowInEasing), label = "alpha")
    val energy = if (idle) 0.9f else 1f

    Canvas(modifier.graphicsLayer { this.alpha = alpha }) {
        if (alpha <= 0.01f) return@Canvas
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension * 0.27f * pulse * energy
        drawCircle(
            brush = Brush.radialGradient(listOf(Color(0x3322D3EE), Color(0x226366F1), Color.Transparent), center, radius * 2.35f),
            radius = radius * 2.35f,
            center = center
        )
        drawCircle(
            brush = Brush.radialGradient(listOf(Color(0xAA22D3EE), Color(0x6600B8D4), Color.Transparent), center, radius * 1.5f),
            radius = radius * 1.5f,
            center = center
        )
        drawCircle(
            brush = Brush.radialGradient(listOf(Color.White, Color(0xFFE0F2FE), Color(0xCC22D3EE), Color.Transparent), center, radius),
            radius = radius,
            center = center
        )
        val angle = Math.toRadians(drift.toDouble())
        val nucleus = Offset(center.x + sin(angle).toFloat() * 4f, center.y + sin(angle * 0.7).toFloat() * 2.5f)
        drawCircle(Color.White.copy(alpha = 0.92f), radius * 0.26f, nucleus)
    }
}
