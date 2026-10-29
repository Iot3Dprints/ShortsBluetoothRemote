package com.srs.shortsremote

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 28, 28, 28)
            setBackgroundColor(android.graphics.Color.rgb(23, 21, 35))
        }
        fun text(value: String, size: Float, color: Int = android.graphics.Color.WHITE): TextView =
            TextView(this).apply {
                text = value; textSize = size; setTextColor(color); gravity = Gravity.CENTER
                setPadding(0, 10, 0, 10)
            }
        root.addView(text("SHORTS REMOTE", 27f))
        root.addView(text("Use your Bluetooth speaker's next/previous buttons to scroll YouTube Shorts.", 16f, 0xFFDDD9EA.toInt()))
        status = text("Accessibility: checking…", 15f, 0xFFFFD166.toInt())
        root.addView(status)
        val enable = Button(this).apply {
            text = "1. Enable accessibility"
            setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        }
        root.addView(enable, matchWidth())
        val start = Button(this).apply {
            text = "2. Start Bluetooth remote"
            setOnClickListener {
                if (!isAccessibilityEnabled()) {
                    status.text = "Please enable Shorts Bluetooth Remote in Accessibility settings first."
                } else {
                    startService(Intent(this@MainActivity, RemoteControlService::class.java).setAction(RemoteControlService.ACTION_START))
                    status.text = "Remote started. Open YouTube Shorts and test Next/Previous."
                }
            }
        }
        root.addView(start, matchWidth())
        val stop = Button(this).apply {
            text = "Stop remote"
            setOnClickListener {
                stopService(Intent(this@MainActivity, RemoteControlService::class.java))
                status.text = "Remote stopped."
            }
        }
        root.addView(stop, matchWidth())
        root.addView(text("Audio stays on the Bluetooth speaker. This app only handles media buttons and swipe gestures.", 13f, 0xFFBDB8CC.toInt()))
        setContentView(root)
    }

    override fun onResume() {
        super.onResume()
        if (::status.isInitialized) status.text = if (isAccessibilityEnabled()) "Accessibility: enabled ✓" else "Accessibility: not enabled yet"
    }

    private fun matchWidth() = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { setMargins(0, 6, 0, 6) }

    private fun isAccessibilityEnabled(): Boolean {
        val manager = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        val expected = ComponentName(this, ShortsAccessibilityService::class.java)
        return manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            .any { it.resolveInfo.serviceInfo.packageName == expected.packageName && it.resolveInfo.serviceInfo.name == expected.className }
    }
}
