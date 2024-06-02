package com.yawki.ui_dashboard.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yawki.audiolist.TAG
import com.yawki.common.data.DataProvider
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.presentation.SharedViewModel
import com.yawki.common_ui.components.Material3Card
import com.yawki.common_ui.theme.YawKiTheme
import com.yawki.ui_dashboard.R

@Composable
fun HomeScreenUI(sharedViewModel: SharedViewModel) {
    YawKiTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val viewModel: HomeScreenVM = hiltViewModel()
            with(viewModel.homeScreenUIState) {
                when {
                    loading == true -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .size(100.dp)
                                    .fillMaxHeight()
                                    .align(Alignment.Center)
                                    .padding(
                                        top = 16.dp,
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 16.dp
                                    )
                            )
                        }
                    }

                    loading == false && errorMessage == null -> {
                        if (monks != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.BottomCenter
                            )
                            {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.background)
                                        .align(Alignment.TopCenter),
                                    contentPadding = PaddingValues(bottom = 60.dp)
                                ) {
                                    items(monks) { monk ->
                                        MonkListItem(monk = monk) {
                                            // update selected monk
                                            sharedViewModel.setSelectedMonk(it)
                                            Log.d(
                                                TAG,
                                                "update selectedMonk==>${sharedViewModel.selectedMonkFlow.value}"
                                            )
                                            viewModel.onEvent(HomeScreenUIEvent.OnMonkSelected(it))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    errorMessage != null -> {
                    }
                }
            }
        }
    }
}

@Composable
fun MonkListItem(monk: Monk, onMonkClick: (Monk) -> Unit) {

    Material3Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = { onMonkClick(monk) }),
        backgroundColor = MaterialTheme.colorScheme.secondary,
        shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(monk.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.little_monk),
                contentDescription = stringResource(R.string.home),
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .clip(CircleShape)
                    .padding(0.dp)
                    .width(150.dp)
                    .height(150.dp)
            )
            Divider()
            Text(
                maxLines = 2,
                text = monk.name,
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@PreviewDynamicColors
@Composable
fun MonkListItemPreview() {
    MonkListItem(monk = DataProvider.monks.first(), onMonkClick = {})
}

//@Preview
//@Composable
//fun HomeScreenUIPreview() {
//    HomeScreenUI()
//}