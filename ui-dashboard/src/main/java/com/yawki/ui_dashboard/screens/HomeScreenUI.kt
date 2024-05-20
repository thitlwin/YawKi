package com.yawki.ui_dashboard.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yawki.common.presentation.models.UiLayerMonk
import com.yawki.common_ui.components.Material3Card
import com.yawki.common_ui.theme.YawKiTheme

val monks = listOf<UiLayerMonk>(
    UiLayerMonk(
        1,
        1,
        "Patamya",
        "https://en.wikipedia.org/wiki/Ashin_Nandamalabhivamsa#/media/File:Sayadaw_nandamalabhivamsa.jpg"
    ),
    UiLayerMonk(2, 2, "Thu Mingala", "https://www.dhammadownload.com/images/U-Thumingala.gif"),
    UiLayerMonk(
        3,
        3,
        "VAṀSAPĀLĀLAṄKĀRA",
        "https://theravada.vn/wp-content/uploads/2020/09/Ngai-Tam-Tang-8-10-800x445.jpg"
    )
)

@Composable
fun HomeScreenUI() {
    YawKiTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp)
            ) {
                items(monks) {
                    MonkUI(monks = it)
                }
            }
        }
    }
}

@Composable
fun MonkUI(monk: UiLayerMonk) {
    Material3Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 16.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = item.imageId),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )
            Divider()
            Text(
                text = monk.name,
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )

        }
    }
}

@Composable
fun Greeting(text: String) {
    Text(text = "This is $text Screen")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickableCardSample() {
    var count by remember { mutableIntStateOf(0) }
    Card(
        onClick = { count++ },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .size(width = 240.dp, height = 100.dp)
            .padding(10.dp)
    ) {
        Text("Clickable card content with count: $count")

    }
}