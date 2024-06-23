package com.yawki.common_ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.yawki.common.data.DataProvider
import com.yawki.common.domain.models.PlayerState
import com.yawki.common.domain.models.song.Song
import com.yawki.common.presentation.PlayerControllerUIEvent
import com.yawki.common.utils.ContentDescriptions
import com.yawki.common_ui.R
import com.yawki.common_ui.theme.YawKiTypography

@Composable
fun PlayerBottomBar(
    modifier: Modifier = Modifier,
    onEvent: (PlayerControllerUIEvent) -> Unit,
    playerState: PlayerState,
    song: Song?,
) {
    AnimatedVisibility(
        visible = playerState != PlayerState.STOPPED,
        modifier = modifier
    ) {
        if (song != null) {
            Material3Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            ) {
                PlayerBottomBarItem(
                    song = song,
                    onEvent = onEvent,
                    playerState = playerState
                )
            }
        }
    }
}

@Composable
fun PlayerBottomBarItem(
    song: Song,
    onEvent: (PlayerControllerUIEvent) -> Unit,
    playerState: PlayerState,
) {
    Box(
        modifier = Modifier
            .height(64.dp)
            .clickable(onClick = { onEvent(PlayerControllerUIEvent.OpenFullScreenPlayer) })

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
        ) {
            Image(
                painter = if (song.artworkUri.isNotEmpty())
                    rememberAsyncImagePainter(song.artworkUri)
                else
                    painterResource(id = R.drawable.little_monk),
                contentDescription = song.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .offset(16.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 8.dp, horizontal = 32.dp),
            ) {
                Text(
                    song.name,
                    style = YawKiTypography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    song.monkName,
                    style = YawKiTypography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = 0.80f
                        }
                )
            }
            Row {
                val painter = if (playerState == PlayerState.PLAYING) {
                    R.drawable.ic_pause
                } else {
                    R.drawable.ic_play
                }
                Image(
                    painter = painterResource(painter),
                    contentDescription = ContentDescriptions.PLAYER_PLAY_PAUSE_BUTTON,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RectangleShape)
                        .clickable(onClick = {
                            if (playerState == PlayerState.PLAYING) {
                                onEvent(PlayerControllerUIEvent.PauseSong)
                            } else {
                                onEvent(PlayerControllerUIEvent.ResumeSong)
                            }
                        }),
                    contentScale = ContentScale.Crop
                )

                Image(
                    imageVector = Icons.Filled.Close,
                    contentDescription = ContentDescriptions.CLOSE_PLAYER_BUTTON,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable(onClick = { onEvent(PlayerControllerUIEvent.CloseThePlayer) }),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview
@Composable
fun PlayerBottomBarItemPreview() {
    PlayerBottomBarItem(
        song = DataProvider.songs.first(),
        onEvent = {},
        playerState = PlayerState.PLAYING
    )
}