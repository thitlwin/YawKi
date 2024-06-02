package com.yawki.common_ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yawki.common_ui.theme.YawKiTypography


@Composable
fun TitleText(modifier: Modifier = Modifier, title: String) {
    Text(
        text = title,
        style = YawKiTypography.titleMedium,
        modifier = modifier.padding(8.dp)
    )
}

@Composable
fun SubtitleText(subtitle: String, modifier: Modifier = Modifier) {
    Text(
        text = subtitle,
        style = YawKiTypography.titleSmall,
        modifier = modifier.padding(8.dp)
    )
}


