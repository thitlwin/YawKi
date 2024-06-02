package com.yawki.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yawki.common.domain.usecases.DestroyMediaControllerUseCase
import com.yawki.common.domain.usecases.GetCurrentSongPositionUseCase
import com.yawki.common.domain.usecases.PauseSongUseCase
import com.yawki.common.domain.usecases.PlaySongUseCase
import com.yawki.common.domain.usecases.ResumeSongUseCase
import com.yawki.common.domain.usecases.SeekSongToPositionUseCase
import com.yawki.common.domain.usecases.SetMediaControllerCallbackUseCase
import com.yawki.common.domain.usecases.SkipToNextSongUseCase
import com.yawki.common.domain.usecases.SkipToPreviousSongUseCase
import com.yawki.common.presentation.PlayerUIState
import com.yawki.navigator.ComposeNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

const val TAG = "PlayerVM-->"

@HiltViewModel
class PlayerVM @Inject constructor(
    private val playSongUseCase: PlaySongUseCase,
    private val pauseSongUseCase: PauseSongUseCase,
    private val resumeSongUseCase: ResumeSongUseCase,
    private val skipToNextSongUseCase: SkipToNextSongUseCase,
    private val skipToPreviousSongUseCase: SkipToPreviousSongUseCase,
    private val seekSongToPositionUseCase: SeekSongToPositionUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val composeNavigator: ComposeNavigator,
) : ViewModel() {

    fun onEvent(event: PlayerUIEvent) {
        when (event) {
            PlayerUIEvent.PauseSong -> pauseSongUseCase
            PlayerUIEvent.ResumeSong -> resumeSongUseCase
            is PlayerUIEvent.SeekSongToPosition -> seekSongToPositionUseCase(event.position)
            PlayerUIEvent.SkipToNextSong -> skipToNextSongUseCase
            PlayerUIEvent.SkipToPreviousSong -> skipToPreviousSongUseCase
            is PlayerUIEvent.PlaySong -> playSongUseCase(event.mediaItemIndex)
        }
    }
}