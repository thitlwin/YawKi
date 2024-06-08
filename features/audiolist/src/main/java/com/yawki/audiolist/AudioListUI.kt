package com.yawki.audiolist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yawki.common.data.DataProvider
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Song
import com.yawki.common.presentation.SharedViewModel
import com.yawki.common.utils.TestTag
import com.yawki.common_ui.components.EmptyView
import com.yawki.common_ui.components.ErrorView
import com.yawki.common_ui.components.ListItemDivider
import com.yawki.common_ui.components.LoadingView
import com.yawki.common_ui.components.SecondaryTopApBar

//const val TAG = "AudioListUI --> "

@Composable
fun AudioListUI(
    sharedViewModel: SharedViewModel
) {
    val audioListVM: AudioListVM = hiltViewModel()
    val state = audioListVM.audioListScreenUIState
    val selectedMonk = sharedViewModel.selectedMonkFlow.collectAsState().value
    LaunchedEffect(key1 = Unit) {
        audioListVM.onEvent(AudioListUIEvent.FetchSong(selectedMonk?.id ?: 0))
    }
    AudioListScreen(
        modifier = Modifier.fillMaxWidth(),
        state = state,
        selectedMonk = selectedMonk!!,
        onEvent = audioListVM::onEvent,
        onSongClick = {
            state.songs?.indexOf(it).let { songIndex ->
                songIndex?.let {
                    sharedViewModel.setSelectedSongIndex(it)
                }
            }
        }
    )
}

@Composable
fun AudioListScreen(
    modifier: Modifier,
    state: AudioListUIState,
    selectedMonk: Monk,
    onEvent: (AudioListUIEvent) -> Unit,
    onSongClick: (Song) -> Unit,
) {
    Scaffold(
        topBar = {
            SecondaryTopApBar(title = stringResource(id = R.string.mp3)) {
                onEvent(AudioListUIEvent.OnBackPress)
            }
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            when {
                state.loading -> {
                    LoadingView()
                }

                state.error != null -> {
                    ErrorView(throwable = state.error)
                }

                state.songs != null -> {
                    if (state.songs.isEmpty())
                        EmptyView(stringResource(id = R.string.empty_audio))
                    else {
                        AudioList(
                            selectedMonk = selectedMonk,
                            songList = state.songs,
                            onFavoriteIconClick = { onEvent(AudioListUIEvent.OnFavoriteIconClick(it)) },
                            onSongClick = {
                                onSongClick(it)
                                onEvent(AudioListUIEvent.OnSongClick(it))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AudioList(
    selectedMonk: Monk,
    songList: List<Song>?,
    onFavoriteIconClick: (audio: Song) -> Unit,
    onSongClick: (Song) -> Unit
) {
    if (songList.isNullOrEmpty()) {
        return EmptyView(stringResource(id = R.string.empty_audio))
    }
    LazyColumn {
        items(count = songList.size,
            itemContent = { item ->
                AudioListItem(
                    selectedMonk = selectedMonk,
                    item = songList[item],
                    onFavoriteIconClick = onFavoriteIconClick,
                    onItemClick = onSongClick,
                )
                if (item != songList.size - 1)
                    ListItemDivider()
            })
    }
}


@Composable
fun AudioListItem(
    selectedMonk: Monk,
    item: Song,
    onFavoriteIconClick: (audio: Song) -> Unit,
    onItemClick: (audio: Song) -> Unit
) {
    val typography = MaterialTheme.typography
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .testTag("${TestTag.AUDIO_LIST_SCREEN_LIST_ITEM}-${item.id}")
            .clickable { onItemClick(item) },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        val imageModifier = Modifier
            .size(50.dp)
            .height(50.dp)
            .clip(shape = CircleShape)
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            modifier = imageModifier,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = typography.titleLarge
                )
                Text(
                    text = selectedMonk.name,
                    style = typography.bodyMedium
                )
            }
            Image(
                imageVector = if (item.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onFavoriteIconClick(item) },
                contentDescription = null
            )
        }
    }
}


@Preview
@Composable
fun AudioListItemPreview(
    monk: Monk = DataProvider.monks.first(),
    item: Song = DataProvider.audioList.first()
) {
    AudioListItem(monk, item, onItemClick = {}, onFavoriteIconClick = {})
}

@Preview
@Composable
fun ContentPreview(
    monk: Monk = DataProvider.monks.first(),
    item: Song = DataProvider.audioList.first()
) {
    AudioList(monk, DataProvider.audioList, onFavoriteIconClick = {}) {}
}
