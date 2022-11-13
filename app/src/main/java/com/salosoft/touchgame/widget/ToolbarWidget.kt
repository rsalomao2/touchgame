package com.salosoft.touchgame.widget

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salosoft.touchgame.ui.widget.GridSizeOption
import com.salosoft.touchgame.ui.widget.SettingsDialogView

@Composable
fun ToolbarWidget(title: String, lastSelectedItem: GridSizeOption, onGridSizeSelected: (GridSizeOption) -> Unit) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var onActionItemClicked by remember { mutableStateOf<ActionItemClicked>(ActionItemClicked.None) }

    TopAppBar(
        title = { Text(title, color = Color.White) },
        elevation = 0.dp,
        actions = {
            IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                Icon(Icons.Default.MoreVert, "")
            }

            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = {
                    onActionItemClicked = ActionItemClicked.None
                    isMenuExpanded = false
                }
            ) {

                DropdownMenuItem(onClick = {
                    onActionItemClicked = ActionItemClicked.Settings
                }) {
                    Text(text = "Settings")
                }
            }
        }
    )

    when (onActionItemClicked) {
        is ActionItemClicked.Settings -> SettingsDialogView(
            lastSelectedItem = lastSelectedItem,
            onDismiss = {
                onActionItemClicked = ActionItemClicked.None
            },
            onSelected = {
                onGridSizeSelected(it)
                onActionItemClicked = ActionItemClicked.None
            }
        )
        is ActionItemClicked.None -> isMenuExpanded = false
    }
}

sealed class ActionItemClicked {
    object Settings : ActionItemClicked()
    object None : ActionItemClicked()
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        ToolbarWidget("Title", GridSizeOption.FourByFour) {}
    }
}
