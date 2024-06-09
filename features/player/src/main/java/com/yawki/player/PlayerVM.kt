package com.yawki.player

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yawki.common.domain.models.PlayerState
import com.yawki.common.domain.models.song.Song
import com.yawki.common.domain.usecases.DestroyMediaControllerUseCase
import com.yawki.common.domain.usecases.GetCurrentSongPositionUseCase
import com.yawki.common.domain.usecases.PauseSongUseCase
import com.yawki.common.domain.usecases.PlaySongUseCase
import com.yawki.common.domain.usecases.ResumeSongUseCase
import com.yawki.common.domain.usecases.SeekSongToPositionUseCase
import com.yawki.common.domain.usecases.SetMediaControllerCallbackUseCase
import com.yawki.common.domain.usecases.SkipToNextSongUseCase
import com.yawki.common.domain.usecases.SkipToPreviousSongUseCase
import com.yawki.common.presentation.BaseViewModel
import com.yawki.common.presentation.PlayerUIState
import com.yawki.navigator.ComposeNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@HiltViewModel
class PlayerVM @Inject constructor(
    private val setMediaControllerCallbackUseCase: SetMediaControllerCallbackUseCase,
    private val getCurrentMusicPositionUseCase: GetCurrentSongPositionUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val pauseSongUseCase: PauseSongUseCase,
    private val resumeSongUseCase: ResumeSongUseCase,
    private val skipToNextSongUseCase: SkipToNextSongUseCase,
    private val skipToPreviousSongUseCase: SkipToPreviousSongUseCase,
    private val seekSongToPositionUseCase: SeekSongToPositionUseCase,
    private val composeNavigator: ComposeNavigator,
) : BaseViewModel<PlayerUIState, PlayerUIEvent>(PlayerUIState()) {
     val TAG = "PlayerVM-->"

    override suspend fun handleEvent(event: PlayerUIEvent) {
        val updateUI: (Song?) -> Unit = {it?.let {
            Log.d("callback", "PlayerVM => ${it.name}")
        }}
//        Log.d(TAG, "handleEvent => $event")
        when (event) {
            PlayerUIEvent.PauseSong -> pauseSongUseCase.invoke()
            PlayerUIEvent.ResumeSong -> resumeSongUseCase()
            is PlayerUIEvent.SeekSongToPosition -> seekSongToPositionUseCase(event.position)
            PlayerUIEvent.SkipToNextSong -> skipToNextSongUseCase(updateUI)
            PlayerUIEvent.SkipToPreviousSong -> skipToPreviousSongUseCase(updateUI)
            is PlayerUIEvent.PlaySong -> playSongUseCase(event.mediaItemIndex)
            PlayerUIEvent.OnBackPress -> onBackPress()
            PlayerUIEvent.OnDownload -> onDownload()
            PlayerUIEvent.BindInitialState -> bindInitialState()
        }
    }

    private fun bindInitialState() {
        setMediaControllerCallback()
    }

    private fun setMediaControllerCallback() {
        Log.d("callback", "PlayerVM => setMediaControllerCallback---")
        setMediaControllerCallbackUseCase { playerState, currentSong, currentPosition, totalDuration,
                                            isShuffleEnabled, isRepeatOneEnabled ->
            Log.d("callback", "PlayerVM => setMediaControllerCallback---inside lambda")

            updateUiState {
                copy(
                    playerState = playerState,
                    currentSong = currentSong,
                    currentPosition = currentPosition,
                    totalDuration = totalDuration,
                    isShuffleEnabled = isShuffleEnabled,
                    isRepeatOneEnabled = isRepeatOneEnabled
                )
            }

            if (playerState == PlayerState.PLAYING) {
                viewModelScope.launch {
                    while (true) {
                        delay(1.seconds)
                        updateUiState {
                            copy(
                                currentPosition = getCurrentMusicPositionUseCase()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onDownload() {
        TODO("Not yet implemented")
    }

    private fun onBackPress() {
        Log.d(TAG, "onBackPress:--- ")
        composeNavigator.navigateUp()
    }
}