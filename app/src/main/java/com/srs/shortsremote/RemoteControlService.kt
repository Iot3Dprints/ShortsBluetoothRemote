package com.srs.shortsremote

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat

class RemoteControlService : Service() {
    private lateinit var session: MediaSessionCompat

    override fun onCreate() {
        super.onCreate()
        createChannel()
        session = MediaSessionCompat(this, "ShortsBluetoothRemote").apply {
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onSkipToNext() {
                    ShortsAccessibilityService.instance?.swipeNext()
                    updateState()
                }
                override fun onSkipToPrevious() {
                    ShortsAccessibilityService.instance?.swipePrevious()
                    updateState()
                }
                // Some speakers send a play/pause event rather than next/previous.
                // We intentionally do not map play/pause to scrolling.
            })
            setSessionActivity(PendingIntent.getActivity(this@RemoteControlService, 0,
                Intent(this@RemoteControlService, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
            setMetadata(android.support.v4.media.MediaMetadataCompat.Builder()
                .putString(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TITLE, "YouTube Shorts Remote")
                .putString(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST, "Bluetooth button controller")
                .build())
            isActive = true
        }
        updateState()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) { stopSelf(); return START_NOT_STICKY }
        startForeground(NOTIFICATION_ID, buildNotification())
        session.isActive = true
        updateState()
        return START_STICKY
    }

    private fun updateState() {
        session.setPlaybackState(PlaybackStateCompat.Builder()
            .setActions(PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
            .setState(PlaybackStateCompat.STATE_PAUSED, 0, 0f)
            .build())
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Shorts remote", NotificationManager.IMPORTANCE_LOW)
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        val open = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle("Shorts Bluetooth Remote is active")
            .setContentText("Next/Previous buttons control Shorts. Bluetooth audio stays connected.")
            .setContentIntent(open)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onDestroy() {
        if (::session.isInitialized) { session.isActive = false; session.release() }
        stopForeground(true)
        super.onDestroy()
    }
    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_START = "com.srs.shortsremote.START"
        const val ACTION_STOP = "com.srs.shortsremote.STOP"
        private const val CHANNEL_ID = "shorts_remote_channel"
        private const val NOTIFICATION_ID = 29
    }
}
