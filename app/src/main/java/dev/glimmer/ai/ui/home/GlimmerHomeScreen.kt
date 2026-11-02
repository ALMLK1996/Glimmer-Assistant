package dev.glimmer.ai.ui.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.glimmer.character.LightBeingView
import dev.glimmer.core.model.PresenceState

private data class ChatLine(val fromUser: Boolean, val text: String)

@Composable
fun GlimmerHomeScreen(
    overlayRunning: Boolean,
    onOverlayToggle: (Boolean) -> Unit,
    onOpenSettings: () -> Unit
) {
    var chatOpen by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var persona by remember { mutableStateOf("رفيق هادئ") }
    val messages = remember {
        mutableStateListOf(
            ChatLine(false, "أهلاً، أنا Glimmer. أعيش حالياً على جهازك كرفيق تجريبي؛ المحادثة الذكية المتصلة بالإنترنت ستُضاف لاحقاً بإعداد واضح لمزوّد الذكاء الاصطناعي.")
        )
    }
    val transition = rememberInfiniteTransition(label = "ambient_background")
    val glow by transition.animateFloat(
        initialValue = 0.18f,
        targetValue = 0.38f,
        animationSpec = infiniteRepeatable(tween(4200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )

    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFF090F20), Color(0xFF111A31), Color(0xFF090F20)))
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("GLIMMER", style = MaterialTheme.typography.labelLarge, color = Color(0xFF78DDF3), fontWeight = FontWeight.Bold)
                    Text("رفيقك الرقمي", style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold)
                }
                TextButton(onClick = onOpenSettings) { Text("⚙ الإعدادات", color = Color(0xFFB8C6E4)) }
            }

            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier.size(230.dp).clip(CircleShape)
                    .background(Color(0xFF36C6E8).copy(alpha = glow * 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.size(190.dp).clip(CircleShape).background(Color(0xFF172743)), contentAlignment = Alignment.Center) {
                    LightBeingView(
                        presence = if (overlayRunning) PresenceState.VISIBLE else PresenceState.IDLE_PERFORMANCE,
                        modifier = Modifier.size(168.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            Text(if (overlayRunning) "أنا معك فوق الشاشة" else "أنا هنا عندما تحتاجني", color = Color.White, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            Text("$persona · نسخة تجريبية تعمل محلياً", color = Color(0xFFB8C6E4), style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(22.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { onOverlayToggle(!overlayRunning) }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(18.dp)) {
                    Text(if (overlayRunning) "إيقاف الرفيق" else "إظهاري فوق التطبيقات", textAlign = TextAlign.Center)
                }
                OutlinedButton(onClick = { chatOpen = true }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(18.dp)) {
                    Text("تحدث معي", color = Color.White)
                }
            }

            Spacer(Modifier.height(18.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF151F36)),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("شخصيتي", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("رفيق هادئ", "مرح", "عملي").forEach { option ->
                            val selected = persona == option
                            TextButton(onClick = { persona = option }) {
                                Text(if (selected) "● $option" else option, color = if (selected) Color(0xFF78DDF3) else Color(0xFFB8C6E4))
                            }
                        }
                    }
                    Text("الظهور العائم اختياري ويمكن إيقافه في أي وقت. لا تُرسل هذه النسخة بياناتك إلى خادم.", color = Color(0xFFB8C6E4), style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(14.dp))
            Text("المرحلة الحالية: الرفيق العائم + واجهة التفاعل المحلية", color = Color(0xFF8392B3), style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
        }
    }

    if (chatOpen) {
        AlertDialog(
            onDismissRequest = { chatOpen = false },
            title = { Text("محادثة Glimmer") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    messages.takeLast(4).forEach { line ->
                        Surface(
                            color = if (line.fromUser) Color(0xFFE5F7FF) else Color(0xFFEDEBFF),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(line.text, modifier = Modifier.padding(10.dp), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    TextField(value = message, onValueChange = { message = it }, label = { Text("اكتب رسالة") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    val text = message.trim()
                    if (text.isNotEmpty()) {
                        messages.add(ChatLine(true, text))
                        messages.add(ChatLine(false, localReply(text)))
                        message = ""
                    }
                }) { Text("إرسال") }
            },
            dismissButton = { TextButton(onClick = { chatOpen = false }) { Text("إغلاق") } }
        )
    }
}

private fun localReply(input: String): String {
    val normalized = input.lowercase()
    return when {
        "مرحبا" in normalized || "السلام" in normalized || "اهلا" in normalized -> "أهلاً بك! يسعدني أن أكون رفيقك. ما الذي ترغب بإنجازه اليوم؟"
        "من أنت" in normalized || "اسمك" in normalized -> "أنا Glimmer، رفيق رقمي تجريبي. شخصيتي وإعدادات الظهور محفوظة محلياً في هذه المرحلة."
        "مساعدة" in normalized || "ماذا تستطيع" in normalized -> "أستطيع الآن الظهور ككائن عائم، وتغيير نمط شخصيتي، ومحادثتك بردود محلية بسيطة. ربط نموذج ذكاء اصطناعي وتنفيذ الأوامر يحتاجان إعداداً وصلاحيات صريحة في مرحلة لاحقة."
        else -> "وصلتني رسالتك. هذه النسخة لا تتصل بنموذج ذكاء اصطناعي بعد؛ أضف مزوّدك لاحقاً من إعدادات واضحة، أو جرّب: مرحباً، من أنت، أو ماذا تستطيع؟"
    }
}
