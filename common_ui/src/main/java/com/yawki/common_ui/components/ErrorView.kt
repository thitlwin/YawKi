package com.yawki.common_ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.yawki.common_ui.theme.YawKiTheme

@Composable
fun ErrorView(throwable: Throwable) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        throwable.message?.let {
            Text(text = it, color = Color.Red)
        }
    }
}

@Preview
@Composable
fun ErrorViewPreview() {
    YawKiTheme {
        ErrorView(Throwable(message = "Something went wrong!"))
    }
}