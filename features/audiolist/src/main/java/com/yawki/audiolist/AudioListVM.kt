package com.yawki.audiolist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Song
import com.yawki.common.domain.usecases.AddMediaItemsUseCase
import com.yawki.common.domain.usecases.GetMonkUseCase
import com.yawki.common.domain.usecases.GetSongsUseCase
import com.yawki.common.domain.usecases.UpdateSongUseCase
import com.yawki.common.utils.KEY_ARGS
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
    private val getMonkUseCase: GetMonkUseCase,
    private val addMediaItemsUseCase: AddMediaItemsUseCase,
    private val updateSongUseCase: UpdateSongUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val selectedMonk = savedStateHandle.get<Int>(KEY_ARGS.SELECTED_MONK)
    private lateinit var monk: Monk
    init {
        viewModelScope.launch {
            selectedMonk?.let { monkId ->
                monk = getMonkUseCase.invoke(monkId)
                Log.d("TAG", "selectedMonk---->$monk")
                fetchAudios(monk)
            }
        }

    }

    var audioListScreenUIState by mutableStateOf(AudioListUIState())
        private set

    private fun fetchAudios(monk: Monk) {
        audioListScreenUIState = audioListScreenUIState.copy(loading = true)
        viewModelScope.launch {

            getSongsUseCase.invoke(monk).catch {
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
                        addMediaItemsUseCase(it.data, monk)
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
            is AudioListUIEvent.FetchSong -> fetchAudios(event.monk)
            is AudioListUIEvent.OnSongClick -> toggleIconState(event.selectedSong)
            is AudioListUIEvent.OnFavoriteIconClick -> onFavoriteIconClick(event.selectedSong)
            AudioListUIEvent.OnBackPress -> onBackPress()
        }
    }

    private fun onBackPress() {
        composeNavigator.navigateUp()
    }

    private fun toggleIconState(selectedSong: Song) {
        selectedSong.apply {
            isPlaying = !selectedSong.isPlaying
        }
        audioListScreenUIState = audioListScreenUIState.copy(
            songs = audioListScreenUIState.songs?.map {
                if (it.id == selectedSong.id)
                    it.copy(isPlaying = selectedSong.isPlaying)
                else
                    it.copy(isPlaying = false)
            }
        )
    }

    private fun onFavoriteIconClick(selectedSong: Song) {
        viewModelScope.launch {
            updateSongUseCase.invoke(selectedSong, monk)
        }
    }

    private fun openPlayerScreen() {
        composeNavigator.navigate(YawKiScreens.PlayerUIScreen.route)
    }
}