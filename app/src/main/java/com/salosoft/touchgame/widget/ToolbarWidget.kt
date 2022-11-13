package com.salosoft.touchgame.widget

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ToolbarWidget(title: String) {
    TopAppBar(
        title = { Text(title, color = Color.White) },
        elevation = 0.dp,
    )
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        ToolbarWidget("Title")
    }
}
