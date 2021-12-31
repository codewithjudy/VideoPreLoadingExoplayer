package com.codewithjudy.videopreloadingexoplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager

class FirstActivity : AppCompatActivity() {

    private val videoUrl =
        "https://firebasestorage.googleapis.com/v0/b/testi-30703.appspot.com/o/Android%20Kotlin%20Developer%20-%20Wake%20Up%2C%20Aleks!%201.mkv?alt=media&token=251ab4ab-284c-4820-9d5c-09d656bc8739"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        schedulePreloadWork(videoUrl)

        findViewById<Button>(R.id.buttonPlayVideo).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).putExtra("VIDEO_URL", videoUrl))
        }
    }

    private fun schedulePreloadWork(videoUrl: String) {
        val workManager = WorkManager.getInstance(applicationContext)
        val videoPreloadWorker = VideoPreloadWorker.buildWorkRequest(videoUrl)
        workManager.enqueueUniqueWork(
            "VideoPreloadWorker",
            ExistingWorkPolicy.KEEP,
            videoPreloadWorker
        )
    }
}