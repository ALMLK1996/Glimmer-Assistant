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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.glimmer.ai.remote.OpenAiCompatibleChatClient
import dev.glimmer.character.LightBeingView
import dev.glimmer.core.model.PresenceState
import kotlinx.coroutines.launch

private data class ChatLine(val fromUser: Boolean, val text: String)

@Composable
fun GlimmerHomeScreen(
    overlayRunning: Boolean,
    onOverlayToggle: (Boolean) -> Unit,
    onOpenSettings: () -> Unit
) {
    var chatOpen by remember { mutableStateOf(false) }
    var providerSettingsOpen by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }
    var endpoint by remember { mutableStateOf("https://api.openai.com/v1/chat/completions") }
    var model by remember { mutableStateOf("gpt-4o-mini") }
    var persona by remember { mutableStateOf("رفيق هادئ") }
    var sending by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("المحادثة تستخدم مزوّدك مباشرة؛ المفتاح يبقى في الذاكرة ولا يُحفظ.") }
    val scope = rememberCoroutineScope()
    val chatClient = remember { OpenAiCompatibleChatClient() }
    val messages = remember {
        mutableStateListOf(ChatLine(false, "أهلاً، أنا Glimmer. اربط مزوّد ذكاء اصطناعي من زر الإعدادات لبدء محادثة حقيقية."))
    }
    val transition = rememberInfiniteTransition(label = "ambient_background")
    val glow by transition.animateFloat(
        initialValue = 0.18f,
        targetValue = 0.38f,
        animationSpec = infiniteRepeatable(tween(4200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )

    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF090F20), Color(0xFF111A31), Color(0xFF090F20))))) {
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
            Text("$persona · رفيقك الذكي", color = Color(0xFFB8C6E4), style = MaterialTheme.typography.bodyMedium)

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
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF151F36)), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("شخصيتي", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("رفيق هادئ", "مرح", "عملي").forEach { option ->
                            TextButton(onClick = { persona = option }) {
                                Text(if (persona == option) "● $option" else option, color = if (persona == option) Color(0xFF78DDF3) else Color(0xFFB8C6E4))
                            }
                        }
                    }
                    Text("الذكاء اختياري. رسائلك تُرسل إلى المزوّد الذي تختاره عند الإرسال فقط.", color = Color(0xFFB8C6E4), style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(14.dp))
            Text("الرفيق العائم · محادثة AI اختيارية", color = Color(0xFF8392B3), style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
        }
    }

    if (providerSettingsOpen) {
        AlertDialog(
            onDismissRequest = { providerSettingsOpen = false },
            title = { Text("إعداد مزوّد الذكاء") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("يدعم واجهات Chat Completions المتوافقة. استخدم HTTPS. مفتاحك لا يُحفظ ويُمسح عند إغلاق التطبيق.", style = MaterialTheme.typography.bodySmall)
                    TextField(value = endpoint, onValueChange = { endpoint = it }, label = { Text("رابط API الكامل") }, singleLine = true)
                    TextField(value = model, onValueChange = { model = it }, label = { Text("اسم النموذج") }, singleLine = true)
                    TextField(value = apiKey, onValueChange = { apiKey = it }, label = { Text("API key") }, visualTransformation = PasswordVisualTransformation(), singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    providerSettingsOpen = false
                    status = "تم إعداد المزوّد على هذا الجهاز مؤقتاً."
                }, enabled = endpoint.startsWith("https://", ignoreCase = true) && model.isNotBlank() && apiKey.isNotBlank()) { Text("حفظ مؤقتاً") }
            },
            dismissButton = { TextButton(onClick = { providerSettingsOpen = false }) { Text("إلغاء") } }
        )
    }

    if (chatOpen) {
        AlertDialog(
            onDismissRequest = { if (!sending) chatOpen = false },
            title = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("محادثة Glimmer")
                    TextButton(onClick = { providerSettingsOpen = true }) { Text("المزوّد") }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    messages.takeLast(6).forEach { line ->
                        Surface(color = if (line.fromUser) Color(0xFFE5F7FF) else Color(0xFFEDEBFF), shape = RoundedCornerShape(14.dp)) {
                            Text(line.text, modifier = Modifier.fillMaxWidth().padding(10.dp), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    if (sending) Text("Glimmer يفكر…", color = MaterialTheme.colorScheme.primary)
                    Text(status, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("اكتب رسالة") },
                        singleLine = true,
                        enabled = !sending,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = !sending && message.isNotBlank() && apiKey.isNotBlank(),
                    onClick = {
                        val text = message.trim()
                        message = ""
                        messages.add(ChatLine(true, text))
                        status = "يتم إرسال رسالتك إلى المزوّد المحدد."
                        sending = true
                        scope.launch {
                            try {
                                val turns = messages.map { OpenAiCompatibleChatClient.Turn(it.fromUser, it.text) }
                                val reply = chatClient.reply(endpoint.trim(), apiKey.trim(), model.trim(), turns)
                                messages.add(ChatLine(false, reply))
                                status = "وصل الرد."
                            } catch (exception: Exception) {
                                status = exception.message ?: "تعذر الحصول على رد."
                            } finally {
                                sending = false
                            }
                        }
                    }
                ) { Text("إرسال") }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = { providerSettingsOpen = true }) { Text("إعداد AI") }
                    TextButton(onClick = { if (!sending) chatOpen = false }, enabled = !sending) { Text("إغلاق") }
                }
            }
        )
    }
}
