package com.yawki.common.data.service

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.yawki.common.data.datasource.local.database.dao.SongDao
import com.yawki.common.data.mapper.SongDomainModelMapper
import com.yawki.common.data.mapper.SongEntityMapper
import com.yawki.common.domain.models.PlayerState
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Mp3.Song
import com.yawki.common.domain.models.song.toSong
import com.yawki.common.domain.service.YawKiPlayerController
import com.yawki.common.utils.getDefaultArtWork
import kotlinx.coroutines.coroutineScope

const val TAG = "YawKiPlayerCtrImpl-->"

class YawKiPlayerControllerImpl(private val context: Context,
    private val songDao: SongDao,
    private val songEntityMapper: SongEntityMapper
) : YawKiPlayerController {

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

    override fun addMediaItems(songs: List<Song>, monk: Monk) {
        val mediaItems = songs.map {
            MediaItem.Builder()
                .setMediaId(it.fileUrl) // this must be the uri of the file
                .setUri(it.fileUrl)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTrackNumber(it.id)
                        .setTitle(it.name)
                        .setSubtitle(monk.name)
                        .setArtist(monk.name)
                        .apply {
                            if (it.artworkUri.isNotEmpty())
                                setArtworkUri(Uri.parse(it.artworkUri))
                            else {
                                setArtworkData(
                                    getDefaultArtWork(context),
                                    MediaMetadata.PICTURE_TYPE_FRONT_COVER
                                )
                            }
                        }
                        .build()
                )
                .build()
        }
        mediaController?.setMediaItems(mediaItems)
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

    override suspend fun getCurrentSong(): Song {
        return coroutineScope{
            // since the index from media playlist can not be match with the list index, we need to get the song from db
            val currentPlayingSong = mediaController?.currentMediaItem
            val songId = currentPlayingSong?.mediaMetadata?.trackNumber // set id to trackNumber since there is not place to tag the song id
//            val song = mediaController?.currentMediaItem?.toSong()
            val dbSong = songDao.getSong(songId ?: 0)
//            val fullInfoSong = song?.copy(isPlaying = dbSong.isPlaying, isFavorite = dbSong.isFavorite, currentPosition = dbSong.currentPosition)
            return@coroutineScope songEntityMapper.mapToDomain(dbSong)
        }
    }

    override fun seekTo(position: Long) {
        mediaController?.seekTo(position)
    }

    override fun stop() {
        mediaController?.stop()
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