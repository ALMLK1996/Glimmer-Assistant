package dev.glimmer.ai.ui.onboarding

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PermissionScreen(
    overlayGranted: Boolean,
    accessibilityEnabled: Boolean,
    onContinue: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("دع Glimmer يعيش معك", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Text(
            "يحتاج الرفيق إلى إذن الظهور فوق التطبيقات كي تتمكن من استدعائه. لا نطلب صلاحية إمكانية الوصول لتشغيل النسخة الأولية.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Text(if (overlayGranted) "✓ إذن الظهور العائم مفعّل" else "إذن الظهور فوق التطبيقات مطلوب", style = MaterialTheme.typography.titleMedium)
        OutlinedButton(
            onClick = {
                context.startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${context.packageName}")))
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !overlayGranted
        ) { Text(if (overlayGranted) "تم منح الإذن" else "منح إذن الظهور العائم") }
        Button(onClick = onContinue, modifier = Modifier.fillMaxWidth(), enabled = overlayGranted) {
            Text("متابعة")
        }
    }
}
