package com.yawki.audiolist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.models.song.Song
import com.yawki.common.domain.usecases.AddMediaItemsUseCase
import com.yawki.common.domain.usecases.GetSongsUseCase
import com.yawki.navigator.ComposeNavigator
import com.yawki.navigator.YawKiScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioListVM @Inject constructor(
    private val composeNavigator: ComposeNavigator,
    private val getSongsUseCase: GetSongsUseCase,
    private val addMediaItemsUseCase: AddMediaItemsUseCase,
) : ViewModel() {
    var audioListScreenUIState by mutableStateOf(AudioListUIState())
        private set

    private fun fetchAudios(monkId: Int) {
        audioListScreenUIState = audioListScreenUIState.copy(loading = true)
        viewModelScope.launch {

            getSongsUseCase.invoke(monkId).catch {
                audioListScreenUIState = audioListScreenUIState.copy(
                    loading = false,
                    error = it
                )
            }.collect {
                audioListScreenUIState = when (it) {
                    is SafeResult.Error -> audioListScreenUIState.copy(
                        loading = false,
                        error = it.exception
                    )

                    is SafeResult.Success -> {
                        addMediaItemsUseCase(it.data)
                        audioListScreenUIState.copy(
                            loading = false,
                            songs = it.data
                        )
                    }

                    SafeResult.Loading -> audioListScreenUIState.copy(loading = true)
                }
            }
        }
    }

    fun onEvent(event: AudioListUIEvent) {
        when (event) {
            is AudioListUIEvent.FetchSong -> fetchAudios(event.monkId)
            is AudioListUIEvent.OnSongClick -> onSongClick(event.selectedSong)
            is AudioListUIEvent.OnFavoriteIconClick -> onFavoriteIconClick(event.selectedSong)
            AudioListUIEvent.OnBackPress -> onBackPress()
        }
    }

    private fun onBackPress() {
        composeNavigator.navigateUp()
    }

    private fun onSongClick(selectedSong: Song) {
        openPlayerScreen()
    }

    private fun onFavoriteIconClick(selectedSong: Song) {
//        Log.d(TAG, "selectedSong isFavorite=${selectedSong.isFavorite}")
        audioListScreenUIState = audioListScreenUIState.copy(songs = audioListScreenUIState.songs?.map {
            if (it.id == selectedSong.id)
                it.copy(isFavorite = !it.isFavorite)
            else
                it
        })
    }

    private fun openPlayerScreen() {
        composeNavigator.navigate(YawKiScreens.PlayerUIScreen.route)
    }
}