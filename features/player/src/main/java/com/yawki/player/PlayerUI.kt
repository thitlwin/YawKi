package com.yawki.player

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yawki.common.data.DataProvider
import com.yawki.common.domain.models.PlayerState
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Song
import com.yawki.common.presentation.PlayerControllerUIEvent
import com.yawki.common.presentation.PlayerUIEvent
import com.yawki.common.presentation.PlayerUIState
import com.yawki.common.presentation.SharedViewModel
import com.yawki.common.utils.ContentDescriptions
import com.yawki.common.utils.TestTags
import com.yawki.common.utils.toTime
import com.yawki.common_ui.components.EmptyView
import com.yawki.common_ui.components.ErrorView
import com.yawki.common_ui.components.LoadingView
import com.yawki.common_ui.components.Material3Card

@Composable
fun PlayerUI(
    sharedViewModel: SharedViewModel,
) {
    val playerVM: PlayerVM = hiltViewModel()
    val state = sharedViewModel.playerUiStateFlow.collectAsState().value
    val selectedMonk = sharedViewModel.selectedMonkFlow.collectAsState().value
//    val selectedSongIndex = sharedViewModel.selectedSongIndexFlow.collectAsState().value ?: 0
//    Log.d("TAG", "PlayerUI: selectedSongIndex = $selectedSongIndex")
//    if (!state.isPlaying) {
//        LaunchedEffect(key1 = Unit) {
//            Log.d("TAG", "PlayerUI: launch playsong = $selectedSongIndex")
//            sharedViewModel.onPlayerEvent(PlayerControllerUIEvent.PlaySong(selectedSongIndex))
//        }
//    }

    PlayerScreen(
        uiState = state,
        monk = selectedMonk,
        onPlayerControllerEvent = {
            sharedViewModel.onPlayerEvent(it)
        },
        onEvent = {
            playerVM.onEvent(it)
//            sharedViewModel.updatePlayerUIState(playerVM.uiState.value)
        }
    )
}

@Composable
fun PlayerScreen(
    uiState: PlayerUIState,
    monk: Monk?,
    onPlayerControllerEvent: (PlayerControllerUIEvent) -> Unit,
    onEvent: (PlayerUIEvent) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.statusBarsPadding(),
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                uiState.loading -> {
                    LoadingView()
                }

                uiState.error != null -> {
                    ErrorView(throwable = uiState.error!!)
                }

                uiState.currentSong != null -> {
                    ScreenContent(
                        uiState = uiState,
                        monk = monk,
                        onPlayerControllerEvent = onPlayerControllerEvent,
                        onPlayerUIEvent = onEvent
                    )
                }

                else -> {
                    EmptyView(message = "No songs found!")
                }
            }
        }
    }
}

@Composable
fun ScreenContent(
    uiState: PlayerUIState,
    monk: Monk?,
    onPlayerControllerEvent: (PlayerControllerUIEvent) -> Unit,
    onPlayerUIEvent: (PlayerUIEvent) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top) {
        PlayerTopAppBar(
            onDownload = onPlayerUIEvent,
            onBackPress = onPlayerUIEvent
        )
        // Image Card
        Material3Card(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
        ) {
            val iconResId =
                if (uiState.playerState == PlayerState.PLAYING) R.drawable.ic_pause else R.drawable.ic_play

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                CenterImage()
                PlayerFileName(
                    currentSong = uiState.currentSong,
                    monkName = monk?.name ?: "",
                    onFavoriteIconClick = {}
                )
            }
            SliderAndPlayerControl(
                currentTime = uiState.currentPosition,
                totalTime = uiState.totalDuration,
                onSliderChange = { newPosition ->
                    onPlayerControllerEvent(
                        PlayerControllerUIEvent.SeekSongToPosition(
                            newPosition.toLong()
                        )
                    )
                },
                playPauseIcon = iconResId,
                playOrToggleSong = {
                    onPlayerControllerEvent(
                        if (uiState.playerState == PlayerState.PLAYING)
                            PlayerControllerUIEvent.PauseSong
                        else
                            PlayerControllerUIEvent.ResumeSong
                    )
                },
                playNextSong = {
                    onPlayerControllerEvent(PlayerControllerUIEvent.SkipToNextSong)
                },
                playPreviousSong = {
                    onPlayerControllerEvent(PlayerControllerUIEvent.SkipToPreviousSong)
                },
                onRewind = {
                    uiState.currentPosition.let { currentPosition ->
                        onPlayerControllerEvent(PlayerControllerUIEvent.SeekSongToPosition(if (currentPosition - 10 * 1000 < 0) 0 else currentPosition - 10 * 1000))
                    }
                },
                onForward = {
                    onPlayerControllerEvent(PlayerControllerUIEvent.SeekSongToPosition(uiState.currentPosition + 10 * 1000))
                }
            )
        }
    }
}

@Composable
fun SliderAndPlayerControl(
    currentTime: Long,
    totalTime: Long,
    onSliderChange: (Float) -> Unit,
    @DrawableRes playPauseIcon: Int,
    playOrToggleSong: () -> Unit,
    playNextSong: () -> Unit,
    playPreviousSong: () -> Unit,
    onRewind: () -> Unit,
    onForward: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {
        PlayerSlider(
            currentTime = currentTime,
            totalTime = totalTime,
            onSliderChange = onSliderChange
        )
        Material3Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
        ) {
            PlayerControl(
                playPauseIcon = playPauseIcon,
                playOrToggleSong = playOrToggleSong,
                playNextSong = playNextSong,
                playPreviousSong = playPreviousSong,
                onRewind = onRewind,
                onForward = onForward
            )
        }
    }
}

@Composable
fun PlayerTopAppBar(
    onBackPress: (PlayerUIEvent) -> Unit,
    onDownload: (PlayerUIEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 10.dp, end = 20.dp)
            .testTag(TestTags.PLAYER_TOP_APP_BAR),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            onBackPress(PlayerUIEvent.OnBackPress)
        }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        IconButton(onClick = {
            onDownload(PlayerUIEvent.OnDownload)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_download),
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun PlayerFileName(currentSong: Song?, monkName: String, onFavoriteIconClick: (Song) -> Unit) {
    currentSong ?: return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = currentSong.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = 0.60f
                    }
                    .testTag(TestTags.PLAYER_FILE_NAME)
            )

            Text(monkName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = 0.60f
                    }
                    .testTag(TestTags.PLAYER_MONK_NAME)
            )
        }
        Image(
            imageVector = if (currentSong.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            modifier = Modifier
                .padding(8.dp)
                .size(30.dp)
                .clickable { onFavoriteIconClick(currentSong) },
            contentDescription = null
        )
    }

}

@Composable
fun PlayerControl(
    @DrawableRes playPauseIcon: Int,
    playOrToggleSong: () -> Unit,
    playNextSong: () -> Unit,
    playPreviousSong: () -> Unit,
    onRewind: () -> Unit,
    onForward: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(20.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_skip_prev),
            contentDescription = ContentDescriptions.SKIP_TO_PREVIOUS_SONG,
            modifier = Modifier
                .size(30.dp)
                .clickable(onClick = playPreviousSong),
            contentScale = ContentScale.Crop
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_backward_10_sec),
            contentDescription = ContentDescriptions.REPLAY_10_SECONDS,
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onRewind)
                .padding(12.dp)
                .size(32.dp)
        )

        Image(
            painter = painterResource(playPauseIcon),
            contentDescription = ContentDescriptions.PLAYER_PLAY_PAUSE_BUTTON,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .clickable(onClick = playOrToggleSong),
            contentScale = ContentScale.Crop
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_forward_10_sec),
            contentDescription = ContentDescriptions.SKIP_10_SECONDS,
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onForward)
                .padding(12.dp)
                .size(32.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_skip_next),
            contentDescription = ContentDescriptions.SKIP_TO_NEXT_SONG,
            modifier = Modifier
                .size(30.dp)
                .clickable(onClick = playNextSong),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun PlayerSlider(
    currentTime: Long,
    totalTime: Long,
    onSliderChange: (Float) -> Unit
) {
    Log.d("slider", "currentTime = $currentTime, currentTime.toTime()=${currentTime.toTime()}")
    Log.d("slider", "totalTime = $totalTime, totalTime.toTime()=${totalTime.toTime()}")
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    currentTime.toTime(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    totalTime.toTime(), style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            value = currentTime.toFloat(),
            onValueChange = onSliderChange,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.tertiaryContainer,
                activeTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 3,
            valueRange = 0f..totalTime.toFloat()
        )
    }
}

@Composable
fun CenterImage(modifier: Modifier = Modifier) {
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )
        )
    }
    val borderWidth = 4.dp
    Image(
        painter = painterResource(id = com.yawki.common_ui.R.drawable.little_monk),
        contentDescription = "Logo",
        modifier = modifier
            .size(300.dp)
            .border(
                BorderStroke(borderWidth, rainbowColorsBrush),
                CircleShape
            )
            .padding(borderWidth)
            .clip(CircleShape)
            .testTag(TestTags.PLAYER_IMAGE),
        contentScale = ContentScale.Crop
    )
}

//
//@Composable
//@Preview(device = "spec:width=411dp,height=891dp")
//fun PlayerScreenPreviewSmall() {
//    PlayerScreen()
//}
//
//@Composable
//@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
//fun PlayerScreenPreviewLandscape() {
//    PlayerScreen()
//}
//
//@Composable
//@Preview(device = "spec:width=673dp,height=841dp")
//fun PlayerScreenPreviewTablet() {
//    PlayerScreen()
//}

@Composable
@Preview
fun PlayerFileNamePreview() {
    PlayerFileName(currentSong = DataProvider.audioList.first(), "Thit") {}
}
