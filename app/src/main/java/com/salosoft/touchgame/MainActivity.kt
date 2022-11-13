package com.salosoft.touchgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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

            Column {
                GridField(position)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val points = viewModel.points
                    val timer = viewModel.time
                    Text(text = "P: $points", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text(text = timer, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { viewModel.getRandomSequence() },
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
private fun GridField(highlightPos: Int) {
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
            FieldView(index == highlightPos)
        }
    }
}

@Composable
fun FieldView(isHighlight: Boolean) {
    val size = 50.dp
    val padding = 4.dp
    Box(
        modifier = Modifier
            .height(size)
            .width(size)
            .background(color = if (isHighlight) Color.Blue else Color.White)
            .padding(padding)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TouchGameTheme {
        App()
    }
}


