package com.codewithjudy.videopreloadingexoplayer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.codewithjudy.videopreloadingexoplayer.databinding.ActivityMainBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mHttpDataSourceFactory: HttpDataSource.Factory
    private lateinit var mDefaultDataSourceFactory: DefaultDataSourceFactory
    private lateinit var mCacheDataSourceFactory: DataSource.Factory
    private lateinit var exoPlayer: SimpleExoPlayer
    private val cache: SimpleCache = VideoApp.cache

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoUrl = intent.getStringExtra("VIDEO_URL")

        mHttpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)

        this.mDefaultDataSourceFactory = DefaultDataSourceFactory(
            applicationContext, mHttpDataSourceFactory
        )

        mCacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(mHttpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        exoPlayer = SimpleExoPlayer.Builder(applicationContext)
            .setMediaSourceFactory(DefaultMediaSourceFactory(mCacheDataSourceFactory)).build()

        val videoUri = Uri.parse(videoUrl)
        val mediaItem = MediaItem.fromUri(videoUri)
        val mediaSource =
            ProgressiveMediaSource.Factory(mCacheDataSourceFactory).createMediaSource(mediaItem)

        binding.playerView.player = exoPlayer
        exoPlayer.playWhenReady = true
        exoPlayer.seekTo(0, 0)
        exoPlayer.setMediaSource(mediaSource, true)
        exoPlayer.prepare()
    }
}