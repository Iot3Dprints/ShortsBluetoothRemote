package com.srs.shortsremote

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.util.DisplayMetrics
import android.view.WindowManager

class ShortsAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) { }
    override fun onInterrupt() { }

    override fun onServiceConnected() {
        instance = this
        super.onServiceConnected()
    }

    override fun onDestroy() {
        if (instance === this) instance = null
        super.onDestroy()
    }

    fun swipeNext() = swipe(up = true)
    fun swipePrevious() = swipe(up = false)

    private fun swipe(up: Boolean) {
        val metrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay.getRealMetrics(metrics)
        val x = metrics.widthPixels * 0.50f
        val startY = if (up) metrics.heightPixels * 0.78f else metrics.heightPixels * 0.25f
        val endY = if (up) metrics.heightPixels * 0.22f else metrics.heightPixels * 0.78f
        val path = Path().apply { moveTo(x, startY); lineTo(x, endY) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 260))
            .build()
        dispatchGesture(gesture, null, Handler(Looper.getMainLooper()))
    }

    companion object {
        @Volatile var instance: ShortsAccessibilityService? = null
            private set
    }
}
