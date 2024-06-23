package com.yawki.player

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yawki.common.domain.models.PlayerState
import com.yawki.common.domain.models.song.Song
import com.yawki.common.domain.usecases.GetCurrentSongPositionUseCase
import com.yawki.common.domain.usecases.PauseSongUseCase
import com.yawki.common.domain.usecases.PlaySongUseCase
import com.yawki.common.domain.usecases.ResumeSongUseCase
import com.yawki.common.domain.usecases.SeekSongToPositionUseCase
import com.yawki.common.domain.usecases.SetMediaControllerCallbackUseCase
import com.yawki.common.domain.usecases.SkipToNextSongUseCase
import com.yawki.common.domain.usecases.SkipToPreviousSongUseCase
import com.yawki.common.presentation.BaseViewModel
import com.yawki.common.presentation.EventBus
import com.yawki.common.presentation.PlayerUIEvent
import com.yawki.common.presentation.PlayerUIState
import com.yawki.navigator.ComposeNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@HiltViewModel
class PlayerVM @Inject constructor(
    private val composeNavigator: ComposeNavigator,
    private val eventBus: EventBus,
) : BaseViewModel<PlayerUIState, PlayerUIEvent>(PlayerUIState()) {
    val TAG = "PlayerVM-->"

    fun triggerPlayerEvent(event: PlayerUIEvent) {
        Log.d("TAG", "triggerPlayerEvent: $event")
        viewModelScope.launch {
            eventBus.emit(event)
        }
    }
    override suspend fun handleEvent(event: PlayerUIEvent) {
        val updateUI: (Song?) -> Unit = {
            it?.let {
                Log.d("callback", "PlayerVM => ${it.name}")
            }
        }
//        Log.d(TAG, "handleEvent => $event")
        when (event) {
            PlayerUIEvent.OnBackPress -> onBackPress()
            PlayerUIEvent.OnDownload -> onDownload()
//            PlayerUIEvent.BindInitialState -> bindInitialState()
//            PlayerUIEvent.OpenFullScreenPlayer -> openFullScreenPlayer()
        }
    }

//    private fun openFullScreenPlayer() {
//        TODO("Not yet implemented")
//    }

//    private fun bindInitialState() {
//        setMediaControllerCallback()
//    }
//
//    private fun setMediaControllerCallback() {
//        Log.d("callback", "PlayerVM => setMediaControllerCallback---")
//        setMediaControllerCallbackUseCase { playerState, currentSong, currentPosition, totalDuration,
//                                            isShuffleEnabled, isRepeatOneEnabled ->
////            Log.d("callback", "PlayerVM => setMediaControllerCallback---inside lambda")
//            viewModelScope.launch {
//                updateUiState {
//                    copy(
//                        playerState = playerState,
//                        currentSong = currentSong,
//                        currentPosition = currentPosition,
//                        totalDuration = totalDuration,
//                        isShuffleEnabled = isShuffleEnabled,
//                        isRepeatOneEnabled = isRepeatOneEnabled
//                    )
//                }
//
//                if (playerState == PlayerState.PLAYING) {
//                    while (true) {
//                        delay(1.seconds)
//                        updateUiState {
//                            copy(
//                                currentPosition = getCurrentMusicPositionUseCase()
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }

    private fun onDownload() {
        TODO("Not yet implemented")
    }

    private fun onBackPress() {
        Log.d(TAG, "onBackPress:--- ")
        composeNavigator.navigateUp()
    }
}