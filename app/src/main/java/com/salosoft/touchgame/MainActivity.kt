package com.salosoft.touchgame

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salosoft.touchgame.ui.theme.TouchGameTheme
import com.salosoft.touchgame.widget.ToolbarWidget
import kotlin.math.sqrt


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
private fun App() {
    TouchGameTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { ToolbarWidget(title = "") }
        ) {
            it
            val viewModel: MainViewModel = viewModel()
            val position by viewModel.positionFlow.collectAsState(initial = -1)
            val errorMessage by viewModel.errorMessageFlow.collectAsState(initial = "")
            handleErrorMessage(errorMessage)

            Column {
                GridField(position) { isHighlight ->
                    viewModel.onItemClicked(isHighlight)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val points = viewModel.points
                    val timer by viewModel.timeFlow.collectAsState(initial = "0:00")
                    Text(text = "P: $points", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text(text = timer, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { viewModel.startStopHighlighting() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    val buttonText = if (viewModel.start) "Stop" else "Start"
                    Text(
                        text = buttonText, color = Color.White, modifier = Modifier.padding(8.dp),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun handleErrorMessage(errorMessage: String) {
    if (errorMessage.isNotBlank())
        Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_LONG).show()
}

@Composable
private fun GridField(highlightPos: Int, onItemClick: (Boolean) -> Unit) {
    val fieldList: List<Boolean> = Array(16) { it == 12 }.toList()
    val listSize = fieldList.size
    val columSize = sqrt(listSize.toDouble()).toInt()
    LazyVerticalGrid(
        columns = GridCells.Fixed(columSize),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.padding(14.dp)
    ) {
        items(listSize) { index ->
            FieldView(index == highlightPos) { isHighlight ->
                onItemClick(isHighlight)
            }
        }
    }
}

@Composable
fun FieldView(isHighlight: Boolean, onItemClick: (Boolean) -> Unit) {
    val size = 50.dp
    val padding = 4.dp
    Box(
        modifier = Modifier
            .height(size)
            .width(size)
            .background(color = if (isHighlight) Color.Blue else Color.White)
            .padding(padding)
            .clickable {
                onItemClick(isHighlight)
            }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TouchGameTheme {
        App()
    }
}


