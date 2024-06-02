package com.yawki.common_ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yawki.common_ui.R
import com.yawki.navigator.ComposeNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTopApBar() {
    TopAppBar(
        title = { Text(text = "YawKi") },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = null
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTopApBar(composeNavigator: ComposeNavigator, title: String) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = { Text(text = title) },
        navigationIcon = {
            NavBackIcon(composeNavigator)
        }
    )
}

@Composable
private fun NavBackIcon(composeNavigator: ComposeNavigator) {
    IconButton(onClick = {
        composeNavigator.navigateUp()
    }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.padding(start = 8.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Composable
@Preview
fun PrimaryTopApBarPreview() {
    PrimaryTopApBar()
}