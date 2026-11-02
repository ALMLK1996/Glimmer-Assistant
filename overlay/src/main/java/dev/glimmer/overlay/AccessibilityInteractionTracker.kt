package dev.glimmer.overlay

import android.accessibilityservice.AccessibilityService
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import timber.log.Timber

/** Reports coarse interaction signals only; it never reads window contents or typed text. */
class AccessibilityInteractionTracker(
    private val service: AccessibilityService
) : InteractionTracker {

    private var listener: InteractionTracker.Listener? = null
    private var lastReportedAt = 0L

    override fun start(listener: InteractionTracker.Listener) {
        this.listener = listener
        lastReportedAt = 0L
        Timber.d("Interaction tracker started")
    }

    override fun stop() {
        listener = null
        Timber.d("Interaction tracker stopped")
    }

    fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || listener == null) return

        // Ignore Glimmer's own overlay and avoid high-volume content/text-change events.
        if (event.packageName?.toString() == service.packageName) return
        val isRelevant = when (event.eventType) {
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_START,
            AccessibilityEvent.TYPE_VIEW_CLICKED,
            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED,
            AccessibilityEvent.TYPE_VIEW_SCROLLED,
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> true
            else -> false
        }
        if (!isRelevant) return

        val now = SystemClock.elapsedRealtime()
        if (now - lastReportedAt < MIN_REPORT_INTERVAL_MS) return
        lastReportedAt = now
        listener?.onUserActive()
    }

    private companion object {
        const val MIN_REPORT_INTERVAL_MS = 500L
    }
}
