package com.yawki.player

import android.util.Log
import com.yawki.common.domain.models.song.Mp3.Song
import com.yawki.common.presentation.BaseViewModel
import com.yawki.common.presentation.PlayerUIEvent
import com.yawki.common.presentation.PlayerUIState
import com.yawki.navigator.ComposeNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerVM @Inject constructor(
    private val composeNavigator: ComposeNavigator,
) : BaseViewModel<PlayerUIState, PlayerUIEvent>(PlayerUIState()) {
    val TAG = "PlayerVM-->"

    override suspend fun handleEvent(event: PlayerUIEvent) {
        val updateUI: (Song?) -> Unit = {
            it?.let {
                Log.d("callback", "PlayerVM => ${it.name}")
            }
        }
        when (event) {
            PlayerUIEvent.OnBackPress -> onBackPress()
            PlayerUIEvent.OnDownload -> onDownload()
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