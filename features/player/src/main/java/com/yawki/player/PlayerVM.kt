package com.yawki.player

import androidx.lifecycle.SavedStateHandle
import com.yawki.common.domain.models.song.Song
import com.yawki.common.domain.usecases.PauseSongUseCase
import com.yawki.common.domain.usecases.PlaySongUseCase
import com.yawki.common.domain.usecases.ResumeSongUseCase
import com.yawki.common.domain.usecases.SeekSongToPositionUseCase
import com.yawki.common.domain.usecases.SkipToNextSongUseCase
import com.yawki.common.domain.usecases.SkipToPreviousSongUseCase
import com.yawki.common.presentation.BaseViewModel
import com.yawki.common.presentation.PlayerUIState
import com.yawki.navigator.ComposeNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val composeNavigator: ComposeNavigator,
) : BaseViewModel<PlayerUIState, PlayerUIEvent>(PlayerUIState()) {

    override suspend fun handleEvent(event: PlayerUIEvent) {
        when (event) {
            PlayerUIEvent.PauseSong -> pauseSongUseCase()
            PlayerUIEvent.ResumeSong -> resumeSongUseCase()
            is PlayerUIEvent.SeekSongToPosition -> seekSongToPositionUseCase(event.position)
            PlayerUIEvent.SkipToNextSong -> skipToNextSongUseCase {}
            PlayerUIEvent.SkipToPreviousSong -> skipToPreviousSongUseCase {}
            is PlayerUIEvent.PlaySong -> playSongUseCase(event.mediaItemIndex)
            PlayerUIEvent.OnBackPress -> onBackPress()
            PlayerUIEvent.OnDownload -> onDownload()
            is PlayerUIEvent.BindInitialState -> bindInitialState(event.selectedSong)
        }
    }

    private fun bindInitialState(newState: PlayerUIState) {
        updateUiState {
            copy(
                loading = newState.loading,
                currentSong = newState.currentSong,
                songs = newState.songs,
                playerState = newState.playerState,
                currentPosition = newState.currentPosition,
                totalDuration = newState.totalDuration
            )
        }
    }

    private fun onDownload() {
        TODO("Not yet implemented")
    }

    private fun onBackPress() {
        composeNavigator.navigateUp()
    }
}