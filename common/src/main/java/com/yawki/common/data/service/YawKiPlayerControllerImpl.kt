package com.yawki.common.data.service

import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.yawki.common.R
import com.yawki.common.domain.models.PlayerState
import com.yawki.common.domain.models.song.Song
import com.yawki.common.domain.models.song.toSong
import com.yawki.common.domain.service.YawKiPlayerController
import java.io.ByteArrayOutputStream

const val TAG = "YawKiPlayerCtrImpl-->"

class YawKiPlayerControllerImpl(val context: Context) : YawKiPlayerController {

    private var mediaControllerFuture: ListenableFuture<MediaController>
    private val mediaController: MediaController?
        get() = if (mediaControllerFuture.isDone) mediaControllerFuture.get() else null

    override var mediaControllerCallback: (
        (
        playerState: PlayerState,
        currentMusic: Song?,
        currentPosition: Long,
        totalDuration: Long,
        isShuffleEnabled: Boolean,
        isRepeatOneEnabled: Boolean
    ) -> Unit
    )? = null

    init {
        val sessionToken =
            SessionToken(context, ComponentName(context, YawKiPlayerService::class.java))
        mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        mediaControllerFuture.addListener({
            Log.d("callback", "YawKiPlayerControllerImpl => addListener---")
            controllerListener()
        }, MoreExecutors.directExecutor())
    }

    private fun controllerListener() {
        Log.d("callback", "YawKiPlayerControllerImpl => controllerListener---")

        mediaController?.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                Log.d("callback", "YawKiPlayerControllerImpl => Player.Listener---$events")

                with(player) {
                    mediaControllerCallback?.invoke(
                        playbackState.toPlayerState(isPlaying),
                        currentMediaItem?.toSong(),
                        currentPosition.coerceAtLeast(0L),
                        duration.coerceAtLeast(0L),
                        shuffleModeEnabled,
                        repeatMode == Player.REPEAT_MODE_ALL
                    )
                }
            }
        })
    }

    private fun Int.toPlayerState(isPlaying: Boolean) =
        when (this) {
            Player.STATE_IDLE -> PlayerState.STOPPED
            Player.STATE_ENDED -> PlayerState.STOPPED
            else -> if (isPlaying) PlayerState.PLAYING else PlayerState.PAUSED
        }

    override fun addMediaItems(songs: List<Song>) {
        val mediaItems = songs.map {
            Log.d("TAG", "-----it.songUrl=>${it.fileUrl}")
            MediaItem.Builder()
                .setMediaId(it.fileUrl)
                .setUri(it.fileUrl)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(it.name)
                        .setSubtitle(it.monk)
                        .setArtist(it.monk)
                        .apply {
                            if (it.artworkUri.isNotEmpty())
                                setArtworkUri(Uri.parse(it.artworkUri))
                            else {
                                setArtworkData(
                                    getDefaultArtWork(),
                                    MediaMetadata.PICTURE_TYPE_FRONT_COVER
                                )
                            }
                        }
                        .build()
                )
                .build()
        }
        Log.d(TAG, "addMediaItems-->$mediaItems")
        Log.d(TAG, "addMediaItems last-->${mediaItems.last().mediaId}")
        Log.d(TAG, "addMediaItems last mediaMetadata-->${mediaItems.last().mediaMetadata}")

        mediaController?.setMediaItems(mediaItems)

        Log.d(TAG, "addMediaItems count-->${mediaController?.mediaItemCount}")

    }

    private fun getDefaultArtWork(): ByteArray {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.little_monk)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
        return outputStream.toByteArray()
    }

    override fun play(mediaItemIndex: Int) {
        Log.d(TAG, "play-->$mediaItemIndex")

        try {
            mediaController?.apply {
                seekToDefaultPosition(mediaItemIndex)
                playWhenReady = true
                prepare()
            }
        } catch (e: Exception) {
            Log.d(TAG, "play error-->$e")

            e.printStackTrace()
        }
    }

    override fun resume() {
        mediaController?.play()
    }

    override fun pause() {
        mediaController?.pause()
    }

    override fun getCurrentPosition(): Long = mediaController?.currentPosition ?: 0L

    override fun getCurrentSong(): Song? = mediaController?.currentMediaItem?.toSong()

    override fun seekTo(position: Long) {
        mediaController?.seekTo(position)
    }

    override fun destroy() {
        MediaController.releaseFuture(mediaControllerFuture)
        mediaControllerCallback = null
        Log.d(TAG, "destroy player---->")
    }

    override fun skipToNextSong() {
        mediaController?.seekToNext()
    }

    override fun skipToPreviousSong() {
        mediaController?.seekToPrevious()
    }

}