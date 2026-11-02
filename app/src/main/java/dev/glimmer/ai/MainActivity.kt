package dev.glimmer.ai

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.glimmer.ai.ui.home.GlimmerHomeScreen
import dev.glimmer.ai.ui.onboarding.PermissionScreen
import dev.glimmer.ai.ui.theme.GlimmerTheme
import dev.glimmer.ai.util.PermissionUtils
import dev.glimmer.overlay.LightBeingOverlayService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GlimmerTheme {
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current
                var overlayGranted by remember { mutableStateOf(PermissionUtils.canDrawOverlays(context)) }
                var overlayRunning by remember { mutableStateOf(false) }

                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            overlayGranted = PermissionUtils.canDrawOverlays(context)
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!overlayGranted) {
                        PermissionScreen(
                            overlayGranted = false,
                            accessibilityEnabled = true,
                            onContinue = {
                                startActivity(
                                    Intent(
                                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:$packageName")
                                    )
                                )
                            }
                        )
                    } else {
                        GlimmerHomeScreen(
                            overlayRunning = overlayRunning,
                            onOverlayToggle = { enabled ->
                                val intent = Intent(this@MainActivity, LightBeingOverlayService::class.java).apply {
                                    action = if (enabled) {
                                        LightBeingOverlayService.ACTION_SHOW
                                    } else {
                                        LightBeingOverlayService.ACTION_STOP
                                    }
                                }
                                if (enabled) startForegroundService(intent) else startService(intent)
                                overlayRunning = enabled
                            },
                            onOpenSettings = {
                                startActivity(Intent(Settings.ACTION_SETTINGS))
                            }
                        )
                    }
                }
            }
        }
    }
}
