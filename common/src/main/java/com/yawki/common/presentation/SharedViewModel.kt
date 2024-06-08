package com.yawki.common.presentation

import androidx.lifecycle.ViewModel
import com.yawki.common.domain.models.monk.Monk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

//const val TAG = "SharedVM-->"
@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private val _selectedMonkFlow = MutableStateFlow<Monk?>(null)
    val selectedMonkFlow = _selectedMonkFlow.asStateFlow()

    private val _selectedSongIndexFlow = MutableStateFlow<Int?>(null)
    val selectedSongIndexFlow = _selectedSongIndexFlow.asStateFlow()

    fun setSelectedSongIndex(songIndex: Int) {
        _selectedSongIndexFlow.value = songIndex
    }

    fun setSelectedMonk(monk: Monk) {
        _selectedMonkFlow.value = monk
    }
}