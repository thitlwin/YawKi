package com.yawki.common.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yawki.common.domain.models.PlayerState
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.service.YawKiPlayerController
import com.yawki.common.domain.usecases.CloseThePlayerUseCase
import com.yawki.common.domain.usecases.GetCurrentSongPositionUseCase
import com.yawki.common.domain.usecases.PauseSongUseCase
import com.yawki.common.domain.usecases.PlaySongUseCase
import com.yawki.common.domain.usecases.ResumeSongUseCase
import com.yawki.common.domain.usecases.SeekSongToPositionUseCase
import com.yawki.common.domain.usecases.SetMediaControllerCallbackUseCase
import com.yawki.common.domain.usecases.SkipToNextSongUseCase
import com.yawki.common.domain.usecases.SkipToPreviousSongUseCase
import com.yawki.navigator.ComposeNavigator
import com.yawki.navigator.YawKiScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

//const val TAG = "SharedVM-->"
@HiltViewModel
class SharedViewModel @Inject constructor(
    private val setMediaControllerCallbackUseCase: SetMediaControllerCallbackUseCase,
    private val getCurrentMusicPositionUseCase: GetCurrentSongPositionUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val pauseSongUseCase: PauseSongUseCase,
    private val resumeSongUseCase: ResumeSongUseCase,
    private val skipToNextSongUseCase: SkipToNextSongUseCase,
    private val skipToPreviousSongUseCase: SkipToPreviousSongUseCase,
    private val seekSongToPositionUseCase: SeekSongToPositionUseCase,
    private val closeThePlayerUseCase: CloseThePlayerUseCase,
    private val composeNavigator: ComposeNavigator,
    private val yawKiPlayerController: YawKiPlayerController,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    init {
        setMediaControllerCallback()
    }


//    fun setAlreadyLunchedFlag() {
//        savedStateHandle["alreadyLunched"] = true
//    }
//    val alreadyLunched: StateFlow<Boolean> = savedStateHandle.getStateFlow("alreadyLunched", false)

    fun onPlayerEvent(event: PlayerControllerUIEvent) {
        when (event) {
            PlayerControllerUIEvent.PauseSong -> pauseSong()
            PlayerControllerUIEvent.ResumeSong -> resumeSong()
            is PlayerControllerUIEvent.SeekSongToPosition -> seekSongPosition(event.position)
            PlayerControllerUIEvent.SkipToNextSong -> skipNextSong()
            PlayerControllerUIEvent.SkipToPreviousSong -> skipPreviousSong()
            is PlayerControllerUIEvent.PlaySong -> playSong(event.mediaItemIndex)
            PlayerControllerUIEvent.OpenFullScreenPlayer -> openFullScreenPlayer()
            PlayerControllerUIEvent.CloseThePlayer -> closePlayer()
        }
    }

    private fun playSong(mediaItemIndex: Int) {
        playSongUseCase(mediaItemIndex)
        _playerUiStateFlow.value = _playerUiStateFlow.value.copy(isPlaying = true)
    }

    private fun skipPreviousSong() {
        skipToPreviousSongUseCase.invoke()
        updateCurrentSong()
    }

    private fun skipNextSong() {
        skipToNextSongUseCase.invoke()
        updateCurrentSong()
    }

    private fun updateCurrentSong() {
        val currentSong = yawKiPlayerController.getCurrentSong()
        currentSong?.apply { isPlaying = true }
        _playerUiStateFlow.value = _playerUiStateFlow.value.copy(currentSong = currentSong)
    }

    private fun seekSongPosition(position: Long) {
        seekSongToPositionUseCase(position)
        _playerUiStateFlow.value = _playerUiStateFlow.value.copy(currentPosition = position)
    }

    private fun resumeSong() {
        resumeSongUseCase()
        _playerUiStateFlow.value = _playerUiStateFlow.value.copy(isPlaying = true)
    }

    private fun pauseSong() {
        pauseSongUseCase.invoke()
        _playerUiStateFlow.value = _playerUiStateFlow.value.copy(isPlaying = false)
    }

    private fun closePlayer() {
        closeThePlayerUseCase.invoke()
        _playerUiStateFlow.value = _playerUiStateFlow.value.copy(currentSong = null, isPlaying = false)
    }

    private fun openFullScreenPlayer() {
        composeNavigator.navigate(YawKiScreens.PlayerUIScreen.route)
    }

    private val _playerUiStateFlow = MutableStateFlow(PlayerUIState())
    val playerUiStateFlow = _playerUiStateFlow.asStateFlow()

    private val _selectedMonkFlow = MutableStateFlow<Monk?>(null)
    val selectedMonkFlow = _selectedMonkFlow.asStateFlow()

    fun setSelectedMonk(monk: Monk) {
        _selectedMonkFlow.value = monk
    }

    private fun setMediaControllerCallback() {
        setMediaControllerCallbackUseCase { playerState, currentSong, currentPosition, totalDuration,
                                            isShuffleEnabled, isRepeatOneEnabled ->
            viewModelScope.launch {
                _playerUiStateFlow.value = playerUiStateFlow.value.copy(
                    playerState = playerState,
                    currentSong = currentSong,
                    currentPosition = currentPosition,
                    totalDuration = totalDuration,
                    isShuffleEnabled = isShuffleEnabled,
                    isRepeatOneEnabled = isRepeatOneEnabled
                )

                if (playerState == PlayerState.PLAYING) {
                    while (true) {
                        delay(1.seconds)
                        _playerUiStateFlow.value = _playerUiStateFlow.value.copy(
                            currentPosition = getCurrentMusicPositionUseCase()
                        )
                    }
                }
            }
        }
    }
}