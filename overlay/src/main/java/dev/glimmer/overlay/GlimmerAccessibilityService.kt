package dev.glimmer.overlay

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import timber.log.Timber

/** Optional, user-enabled coarse activity detection. No window text or hierarchy is retrieved. */
class GlimmerAccessibilityService : AccessibilityService() {

    private var interactionTracker: AccessibilityInteractionTracker? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo = serviceInfo.apply {
            eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED or
                AccessibilityEvent.TYPE_VIEW_LONG_CLICKED or
                AccessibilityEvent.TYPE_VIEW_SCROLLED or
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                AccessibilityEvent.TYPE_TOUCH_INTERACTION_START
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = 0
            notificationTimeout = 250
        }
        interactionTracker = AccessibilityInteractionTracker(this).also { tracker ->
            tracker.start(object : InteractionTracker.Listener {
                override fun onUserActive() {
                    val intent = Intent(this@GlimmerAccessibilityService, LightBeingOverlayService::class.java).apply {
                        action = LightBeingOverlayService.ACTION_USER_ACTIVE
                    }
                    try {
                        startService(intent)
                    } catch (exception: IllegalStateException) {
                        Timber.w(exception, "Overlay service is not available")
                    }
                }

                override fun onUserIdle() = Unit
            })
        }
        Timber.d("Glimmer accessibility service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        interactionTracker?.onAccessibilityEvent(event)
    }

    override fun onInterrupt() {
        Timber.w("Glimmer accessibility service interrupted")
    }

    override fun onDestroy() {
        interactionTracker?.stop()
        interactionTracker = null
        super.onDestroy()
    }
}
