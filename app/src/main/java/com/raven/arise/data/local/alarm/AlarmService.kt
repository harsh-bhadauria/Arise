package com.raven.arise.data.local.alarm

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.raven.arise.MainActivity
import com.raven.arise.R

class AlarmService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        val label = intent?.getStringExtra("label") ?: "Alarm!"

        when (action) {
            ACTION_PAUSE -> {
                Log.d("AlarmService", "Received PAUSE action")
                pauseAlarm()
                return START_NOT_STICKY
            }

            ACTION_RESUME -> {
                Log.d("AlarmService", "Received RESUME action")
                resumeAlarm()
                return START_NOT_STICKY
            }

            else -> {
                Log.d("AlarmService", "Alarm started or restarted with label: $label")
                playAlarmSound()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    startVibration()
                }
                startForeground(1, buildNotification(label))
                return START_REDELIVER_INTENT
            }
        }
    }


    private fun buildNotification(label: String): Notification {
        val tapIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("label", label)
            putExtra("initialScreen", "alarmRing")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val contentPendingIntent = PendingIntent.getActivity(
            this,
            0,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val deleteIntent = Intent(this, AlarmService::class.java).apply {
            putExtra("label", label)
        }

        val deletePendingIntent = PendingIntent.getService(
            this,
            1,
            deleteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, "alarm_channel")
            .setContentTitle("⏰ Alarm Ringing")
            .setContentText("Tap to dismiss")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(contentPendingIntent)
            .setDeleteIntent(deletePendingIntent)
            .setOngoing(true)
            .setAutoCancel(false)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()
    }

    private fun playAlarmSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
            val afd = resources.openRawResourceFd(R.raw.mysterious_place) ?: return

            mediaPlayer?.apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                isLooping = true
                prepare()
                start()
            }

            afd.close()
        } else {
            mediaPlayer?.start()
        }

        Log.d("AlarmService", "MediaPlayer started")
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun startVibration() {
        val vibratorManager = getSystemService(VibratorManager::class.java)
        vibrator = vibratorManager?.defaultVibrator
        val vibrationPattern = longArrayOf(0, 500, 500)
        val effect = VibrationEffect.createWaveform(vibrationPattern, 0)
        vibrator?.vibrate(effect)
    }

    override fun onDestroy() {
        Log.d("AlarmService", "Service destroyed, MediaPlayer stopped")
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        vibrator?.cancel()

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_PAUSE = "com.raven.arise.action.PAUSE"
        const val ACTION_RESUME = "com.raven.arise.action.RESUME"
    }

    private fun pauseAlarm() {
        mediaPlayer?.pause()
        vibrator?.cancel()
    }

    private fun resumeAlarm() {
        mediaPlayer?.start()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            startVibration()
        }
    }

}