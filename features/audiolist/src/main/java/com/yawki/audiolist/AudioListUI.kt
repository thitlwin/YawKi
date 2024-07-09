package com.yawki.audiolist

import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yawki.common.data.DataProvider
import com.yawki.common.domain.models.PlayerState
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Mp3
import com.yawki.common.domain.models.song.Mp3.Song
import com.yawki.common.presentation.PlayerControllerUIEvent
import com.yawki.common.presentation.PlayerUIState
import com.yawki.common.presentation.SharedViewModel
import com.yawki.common.presentation.auth.AuthUIEvent
import com.yawki.common.presentation.auth.AuthUiState
import com.yawki.common.presentation.auth.AuthViewModel
import com.yawki.common.utils.TestTag
import com.yawki.common_ui.components.EmptyView
import com.yawki.common_ui.components.ErrorView
import com.yawki.common_ui.components.ListItemDivider
import com.yawki.common_ui.components.LoadingView
import com.yawki.common_ui.components.PlayerBottomBar
import com.yawki.common_ui.components.SecondaryTopApBar
import com.yawki.navigator.ComposeNavigator
import com.yawki.navigator.YawKiScreens

@Composable
fun AudioListUI(
    sharedViewModel: SharedViewModel,
    composeNavigator: ComposeNavigator
) {
    Log.d("TAG", "AudioListUI: building........")
    val authViewModel: AuthViewModel = hiltViewModel()
    val audioListVM: AudioListVM = hiltViewModel()
    val state = audioListVM.audioListScreenUIState
    val playerUIState = sharedViewModel.playerUiStateFlow.collectAsState().value
    val selectedMonk = sharedViewModel.selectedMonkFlow.collectAsState().value
    val authUiState = authViewModel.uiState.collectAsState().value

    AudioListScreen(
        modifier = Modifier.fillMaxWidth(),
        state = state,
        selectedMonk = selectedMonk!!,
        onEvent = audioListVM::onEvent,
        playerUIState = playerUIState,
        authUiState = authUiState,
        onPlayerEvent = sharedViewModel::onPlayerEvent,
        onSongClick = { selectedSong ->
            audioListVM.onEvent(AudioListUIEvent.OnSongClick(selectedSong))
            if (selectedSong.isPlaying)
                sharedViewModel.onPlayerEvent(PlayerControllerUIEvent.PauseSong)
            else {
                state.songs?.filterIsInstance<Song>()?.indexOf(selectedSong)?.let {
                    sharedViewModel.onPlayerEvent(PlayerControllerUIEvent.PlaySong(it))
                }
            }
        },
        onSignIn = {
            authViewModel.onEvent(AuthUIEvent.SignInAnonymously)
            composeNavigator.navigate(YawKiScreens.AuthUIScreen.route)
        }
    )
}

@Composable
fun AudioListScreen(
    modifier: Modifier,
    state: AudioListUIState,
    selectedMonk: Monk,
    playerUIState: PlayerUIState,
    authUiState: AuthUiState,
    onPlayerEvent: (PlayerControllerUIEvent) -> Unit,
    onEvent: (AudioListUIEvent) -> Unit,
    onSongClick: (Song) -> Unit,
    onSignIn: () -> Unit,
) {
    Scaffold(
        topBar = {
            SecondaryTopApBar(title = selectedMonk.name) {
                onEvent(AudioListUIEvent.OnBackPress)
            }
        },
        bottomBar = {
            PlayerBottomBar(
                modifier = Modifier,
                onEvent = onPlayerEvent,
                playerState = playerUIState.playerState ?: PlayerState.STOPPED,
                song = playerUIState.currentSong
            )
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
                            onFavoriteIconClick = {
                                if (authUiState.isAuthenticated)
                                    onEvent(AudioListUIEvent.OnFavoriteIconClick(it))
                                else
                                    onSignIn.invoke()
                            },
                            onSongClick = onSongClick,
                            onFolderClick = {
//                                onEvent(AudioListUIEvent.OnFolderClick(it))
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
    songList: List<Mp3>?,
    onFavoriteIconClick: (audio: Song) -> Unit,
    onSongClick: (Song) -> Unit,
    onFolderClick: (Mp3.SongFolder) -> Unit
) {
    if (songList.isNullOrEmpty()) {
        return EmptyView(stringResource(id = R.string.empty_audio))
    }
    LazyColumn {
        items(count = songList.size,
            itemContent = { item ->
                when (val song = songList[item]) {
                    is Song -> AudioListItem(
                        selectedMonk = selectedMonk,
                        item = song,
                        onFavoriteIconClick = onFavoriteIconClick,
                        onItemClick = onSongClick,
                    )

                    is Mp3.SongFolder -> FolderItem(item = song, onFolderClick = onFolderClick)
                }
                if (item != songList.size - 1)
                    ListItemDivider()
            })
    }
}

@Composable
fun FolderItem(item: Mp3.SongFolder, onFolderClick: (Mp3.SongFolder) -> Unit) {

    val typography = MaterialTheme.typography
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .testTag("${TestTag.AUDIO_LIST_SCREEN_LIST_ITEM}-${item.id}")
            .clickable { onFolderClick(item) },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        val imageModifier = Modifier
            .size(40.dp)
            .height(40.dp)

        Image(
            painter = painterResource(id = com.yawki.common.R.drawable.ic_folder),
            modifier = imageModifier,
            contentDescription = null,
            contentScale = ContentScale.Crop,
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
                    style = typography.bodyLarge
                )
//                Text(
//                    text = item.monkName,
//                    style = typography.bodyMedium
//                )
            }
        }
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
            .size(40.dp)
            .height(40.dp)
            .clip(shape = CircleShape)
        Image(
            painter = painterResource(if (item.isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
            modifier = imageModifier,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(
                MaterialTheme.colorScheme.secondary,
                blendMode = BlendMode.SrcOut
            )
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
                    style = typography.bodyLarge
                )
//                Text(
//                    text = item.name,
//                    style = typography.titleLarge
//                )
//                Text(
//                    text = selectedMonk.name,
//                    style = typography.bodyMedium
//                )
            }
            Image(
                imageVector = if (item.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        val song = item.copy(isFavorite = !item.isFavorite)
                        onFavoriteIconClick(song)
                    },
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun FolderItemPreview(
    item: Mp3.SongFolder = Mp3.SongFolder(id = 1, name = "Folder 1", monkId = 2)
) {
    FolderItem(item, onFolderClick = {})
}

@Preview
@Composable
fun AudioListItemPreview(
    monk: Monk = DataProvider.monks.first(),
    item: Song = DataProvider.audioList.first().copy(isPlaying = true)
) {
    AudioListItem(monk, item, onItemClick = {}, onFavoriteIconClick = {})
}

@Preview
@Composable
fun ContentPreview(
    monk: Monk = DataProvider.monks.first(),
    item: Song = DataProvider.audioList.first()
) {
    AudioList(
        monk,
        DataProvider.audioList,
        onFavoriteIconClick = {},
        onSongClick = {},
        onFolderClick = {},
    )
}
