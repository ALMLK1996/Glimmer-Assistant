package dev.glimmer.overlay

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import dev.glimmer.ai.MainActivity
import dev.glimmer.core.model.PresenceState
import dev.glimmer.overlay.ui.LightBeingOverlayRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class LightBeingOverlayService : Service(), LifecycleOwner, SavedStateRegistryOwner {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main.immediate + job)
    private lateinit var presenceController: PresenceController
    private var windowManager: WindowManager? = null
    private var overlayView: ComposeView? = null
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry
    private val channelId = "glimmer_overlay"

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        presenceController = PresenceController(scope)
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())
        lifecycleRegistry.currentState = Lifecycle.State.STARTED

        scope.launch {
            presenceController.state.collectLatest { state ->
                when (state.presence) {
                    PresenceState.VISIBLE, PresenceState.MATERIALIZING, PresenceState.IDLE_PERFORMANCE, PresenceState.FADING ->
                        ensureOverlayVisible()
                    PresenceState.HIDDEN -> removeOverlay()
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SHOW -> presenceController.onUserInteraction()
            ACTION_HIDE, ACTION_USER_ACTIVE -> presenceController.onUserBrowsing()
            ACTION_STOP -> {
                presenceController.forceHide()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelfResult(startId)
            }
        }
        return START_NOT_STICKY
    }

    private fun ensureOverlayVisible() {
        if (overlayView != null) return
        if (!Settings.canDrawOverlays(this)) {
            Timber.w("Overlay permission was revoked; hiding the companion")
            presenceController.forceHide()
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            return
        }
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 28
            y = 160
        }
        val view = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@LightBeingOverlayService)
            setViewTreeSavedStateRegistryOwner(this@LightBeingOverlayService)
            setContent {
                val state by presenceController.state.collectAsState()
                LightBeingOverlayRoot(
                    presence = state.presence,
                    onDismissRequest = { presenceController.onUserBrowsing() }
                )
            }
        }
        try {
            windowManager?.addView(view, params)
            overlayView = view
            lifecycleRegistry.currentState = Lifecycle.State.RESUMED
        } catch (exception: SecurityException) {
            Timber.e(exception, "Overlay permission is unavailable")
            presenceController.forceHide()
        } catch (exception: WindowManager.BadTokenException) {
            Timber.e(exception, "System rejected the overlay window")
            presenceController.forceHide()
        }
    }

    private fun removeOverlay() {
        overlayView?.let { view ->
            try {
                windowManager?.removeView(view)
            } catch (exception: IllegalArgumentException) {
                Timber.w(exception, "Overlay was already detached")
            } finally {
                overlayView = null
            }
        }
        if (lifecycleRegistry.currentState != Lifecycle.State.DESTROYED) {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(channelId, "Glimmer companion", NotificationManager.IMPORTANCE_LOW)
            .apply {
                description = "Shown while the user-enabled companion overlay is active"
                setShowBadge(false)
            }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        val stopIntent = Intent(this, LightBeingOverlayService::class.java).setAction(ACTION_STOP)
        val stopPendingIntent = PendingIntent.getService(
            this,
            NOTIFICATION_ID,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val openIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID + 1,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Glimmer is ready")
            .setContentText("Your floating companion is active")
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setContentIntent(openIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopPendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    override fun onDestroy() {
        presenceController.forceHide()
        removeOverlay()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_SHOW = "dev.glimmer.overlay.SHOW"
        const val ACTION_HIDE = "dev.glimmer.overlay.HIDE"
        const val ACTION_USER_ACTIVE = "dev.glimmer.overlay.USER_ACTIVE"
        const val ACTION_STOP = "dev.glimmer.overlay.STOP"
        private const val NOTIFICATION_ID = 1001
    }
}
