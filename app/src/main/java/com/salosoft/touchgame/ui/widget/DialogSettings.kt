package com.salosoft.touchgame.ui.widget

import SelectableItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsDialogView(onDismiss: () -> Unit, onSelected: (GridSizeOption) -> Unit) {
    var openDialog by remember { mutableStateOf(true) }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
            },
            title = {
                Text(text = "Grid Size")
            },
            text = {
                val modifier = Modifier.padding(bottom = 12.dp)
                Column {
                    SelectableItem(selected = false, title = "4x4", modifier = modifier) {
                        openDialog = false
                        onSelected(GridSizeOption.FourByFour)
                    }
                    SelectableItem(selected = false, title = "16x16", modifier = modifier) {
                        openDialog = false
                        onSelected(GridSizeOption.SixteenBySixteen)
                    }
                    SelectableItem(selected = false, title = "36x36", modifier = modifier) {
                        openDialog = false
                        onSelected(GridSizeOption.ThirdSixByThirdSix)
                    }
                    SelectableItem(selected = false, title = "64x64", modifier = modifier) {
                        openDialog = false
                        onSelected(GridSizeOption.SixtyFourBySixtyFour)
                    }
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onDismiss()
                            openDialog = false
                        }
                    ) {
                        Text("Dismiss", color = Color.White)
                    }
                }
            }
        )
    }
}

sealed class GridSizeOption {
    object FourByFour : GridSizeOption()
    object SixteenBySixteen : GridSizeOption()
    object ThirdSixByThirdSix : GridSizeOption()
    object SixtyFourBySixtyFour : GridSizeOption()
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    SettingsDialogView(
        onDismiss = {},
        onSelected = {}
    )
}
