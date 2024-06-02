package com.yawki.player

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yawki.common.data.DataProvider
import com.yawki.common.domain.models.PlayerState
import com.yawki.common.domain.models.song.Song
import com.yawki.common.presentation.SharedViewModel
import com.yawki.common.utils.toTime
import com.yawki.common_ui.components.Material3Card
import com.yawki.common_ui.theme.YawKiTheme
import com.yawki.navigator.ComposeNavigator

@Composable
fun PlayerUI(
    composeNavigator: ComposeNavigator,
    sharedViewModel: SharedViewModel,
    playerVM: PlayerVM = hiltViewModel()
) {
    YawKiTheme {
        PlayerScreen(
            composeNavigator = composeNavigator,
            sharedViewModel = sharedViewModel,
            playerVM = playerVM
        )
    }
}

@Composable
fun PlayerScreen(
    composeNavigator: ComposeNavigator,
    sharedViewModel: SharedViewModel,
    playerVM: PlayerVM
) {
    val selectedMonk = sharedViewModel.selectedMonkFlow.collectAsState().value
    val selectedSong = sharedViewModel.selectedSongIndexFlow.collectAsState().value ?: 0

    LaunchedEffect(key1 = Unit) {
        playerVM.onEvent(PlayerUIEvent.PlaySong(selectedSong))
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.statusBarsPadding(),
    ) { innerPadding ->
        Column(verticalArrangement = Arrangement.Top) {
            PlayerTopAppBar(composeNavigator)
            // Image Card
            Material3Card(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            ) {
                val musicControllerUiState = sharedViewModel.musicControllerUiState.collectAsState().value
                val iconResId =
                    if (musicControllerUiState.playerState == PlayerState.PLAYING) R.drawable.ic_pause else R.drawable.ic_play

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    CenterImage()
                    PlayerFileName(
                        currentSong = musicControllerUiState.currentSong,
                        monkName = selectedMonk?.name ?: "",
                        onFavoriteIconClick = {}
                    )
                }
                SliderAndPlayerControl(
                    currentTime = musicControllerUiState.currentPosition,
                    totalTime = musicControllerUiState.totalDuration,
                    onSliderChange = { newPosition ->
                        playerVM.onEvent(
                            PlayerUIEvent.SeekSongToPosition(
                                newPosition.toLong()
                            )
                        )
                    },
                    playPauseIcon = iconResId,
                    playOrToggleSong = {
                        playerVM.onEvent(
                            if (musicControllerUiState.playerState == PlayerState.PLAYING)
                                PlayerUIEvent.PauseSong
                            else
                                PlayerUIEvent.ResumeSong
                        )
                    },
                    playNextSong = {
                        playerVM.onEvent(PlayerUIEvent.SkipToNextSong)
                    },
                    playPreviousSong = {
                        playerVM.onEvent(PlayerUIEvent.SkipToPreviousSong)
                    },
                    onRewind = {
                        musicControllerUiState.currentPosition.let { currentPosition ->
                            playerVM.onEvent(PlayerUIEvent.SeekSongToPosition(if (currentPosition - 10 * 1000 < 0) 0 else currentPosition - 10 * 1000))
                        }
                    },
                    onForward = {
                        playerVM.onEvent(PlayerUIEvent.SeekSongToPosition(musicControllerUiState.currentPosition + 10 * 1000))
                    }
                )
            }

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
fun PlayerTopAppBar(composeNavigator: ComposeNavigator) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 10.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            composeNavigator.navigateUp()
        }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        IconButton(onClick = {
            // download
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
                overflow = TextOverflow.Ellipsis
            )

            Text(monkName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.graphicsLayer {
                    alpha = 0.60f
                })

//            TitleText(title = currentSong.name)
//            SubtitleText(
//                subtitle = monkName,
//                modifier = Modifier.alpha(0.7f)
//            )
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
            contentDescription = "Logo",
            modifier = Modifier
                .size(30.dp)
                .clickable(onClick = playPreviousSong),
            contentScale = ContentScale.Crop
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_backward_10_sec),
            contentDescription = "Replay 10 seconds",
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onRewind)
                .padding(12.dp)
                .size(32.dp)
        )

        Image(
            painter = painterResource(playPauseIcon),
            contentDescription = "Logo",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .clickable(onClick = playOrToggleSong),
            contentScale = ContentScale.Crop
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_forward_10_sec),
            contentDescription = "Skip 10 seconds",
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onForward)
                .padding(12.dp)
                .size(32.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_skip_next),
            contentDescription = "Logo",
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
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Log.d(TAG, "currentTime = $currentTime, currentTime.toTime()=${currentTime.toTime()}")
    Log.d(TAG, "totalTime = $totalTime, totalTime.toTime()=${totalTime.toTime()}")
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

//            Text(
//                text = currentTime.toTime(),
//                color = MaterialTheme.colorScheme.onSecondary
//            )
//            Text(
//                text = totalTime.toTime(),
//                color = MaterialTheme.colorScheme.onSecondary
//            )
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
            .clip(CircleShape),
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