package com.yawki.common_ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yawki.common.utils.TestTags
import com.yawki.common_ui.theme.YawKiTypography

@Composable
fun EmptyView(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.EMPTY_VIEW_TAG),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = Icons.Outlined.Face,
            contentDescription = "Empty Data Icon",
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = message,
            style = YawKiTypography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )
    }
}


@Preview
@Composable
fun EmptyScreenPreview() {
    EmptyView(message = "No Data Available")
}