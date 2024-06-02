package com.yawki.common.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yawki.common.domain.models.PlayerState
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.usecases.DestroyMediaControllerUseCase
import com.yawki.common.domain.usecases.GetCurrentSongPositionUseCase
import com.yawki.common.domain.usecases.SetMediaControllerCallbackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

const val TAG = "SharedVM-->"
@HiltViewModel
class SharedViewModel @Inject constructor(
    private val setMediaControllerCallbackUseCase: SetMediaControllerCallbackUseCase,
    private val getCurrentMusicPositionUseCase: GetCurrentSongPositionUseCase,
    private val destroyMediaControllerUseCase: DestroyMediaControllerUseCase
) : ViewModel() {

    private val _selectedMonkFlow = MutableStateFlow<Monk?>(null)
    val selectedMonkFlow = _selectedMonkFlow.asStateFlow()

    private val _selectedSongIndexFlow = MutableStateFlow<Int?>(null)
    val selectedSongIndexFlow = _selectedSongIndexFlow.asStateFlow()

    private val _musicControllerUiState = MutableStateFlow(PlayerUIState())
    val musicControllerUiState = _musicControllerUiState.asStateFlow()

    init {
        setMediaControllerCallback()
    }

    fun setMusicControllerUiState(uiState: PlayerUIState) {
        _musicControllerUiState.value = uiState
    }

    private fun setMediaControllerCallback() {
        setMediaControllerCallbackUseCase { playerState, currentSong, currentPosition, totalDuration,
                                            isShuffleEnabled, isRepeatOneEnabled ->
            _musicControllerUiState.value = _musicControllerUiState.value.copy(
                playerState = playerState,
                currentSong = currentSong,
                currentPosition = currentPosition,
                totalDuration = totalDuration,
                isShuffleEnabled = isShuffleEnabled,
                isRepeatOneEnabled = isRepeatOneEnabled
            )

            if (playerState == PlayerState.PLAYING) {
                viewModelScope.launch {
                    while (true) {
                        delay(3.seconds)
                        _musicControllerUiState.value = _musicControllerUiState.value.copy(
                            currentPosition = getCurrentMusicPositionUseCase()
                        )
                    }
                }
            }
        }
    }

    fun destroyMediaController() {
        destroyMediaControllerUseCase()
    }

    fun setSelectedSongIndex(songIndex: Int) {
        Log.d(TAG, "selectedSongIndex = $songIndex")
        _selectedSongIndexFlow.value = songIndex
    }

    fun setSelectedMonk(monk: Monk) {
        _selectedMonkFlow.value = monk
    }

}