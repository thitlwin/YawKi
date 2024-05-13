package com.yawki.ui_dashboard.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yawki.common_ui.theme.YawKiTheme

@Composable
fun HomeScreenUI() {
    YawKiTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Greeting("Home")
        }
    }
}

@Composable
fun Greeting(text: String) {
    Text(text = "This is $text Screen")
}
