package com.yawki.common.data.service

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class YawKiPlayerService : MediaSessionService() {
    val TAG= "-PlayerService-"
    private var mediaSession: MediaSession? = null

    @Inject
    lateinit var exoPlayer: ExoPlayer

    override fun onCreate() {
        super.onCreate()
Log.d(TAG, "creating YawKiPlayerService------")
        mediaSession = MediaSession.Builder(this, exoPlayer)
            .setCallback(MediaSessionCallback())
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    override fun onDestroy() {
        mediaSession?.run {
            exoPlayer.release()
            release()
            mediaSession = null
        }

        super.onDestroy()
    }

    private inner class MediaSessionCallback : MediaSession.Callback {
        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>
        ): ListenableFuture<MutableList<MediaItem>> {
            try {
                val updatedMediaItems = mediaItems.map {
                    Log.d(TAG, "onAddMediaItems ----map=${it.mediaId}")
                    it.buildUpon().setUri(it.mediaId).build()
                }.toMutableList()
                // to deelte
                updatedMediaItems.map {
                    Log.d(TAG, "onAddMediaItems ----updatedMediaItems=${it.mediaId}")
                }

                return Futures.immediateFuture(updatedMediaItems)
            } catch (e: Exception) {
                Log.d(TAG, "onAddMediaItems ----exception=${e.message}")
                e.printStackTrace()
            }
            return Futures.immediateCancelledFuture()
        }
    }
}