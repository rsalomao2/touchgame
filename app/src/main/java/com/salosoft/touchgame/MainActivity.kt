package com.salosoft.touchgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salosoft.touchgame.ui.theme.TouchGameTheme
import com.salosoft.touchgame.widget.ToolbarWidget
import kotlin.math.sqrt
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

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
        val scope = rememberCoroutineScope()
        // A surface container using the 'background' color from the theme
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            topBar = { ToolbarWidget(title = "") }
        ) {
            it
            val position by getRandomSequency().collectAsState(initial = 2)

            println("Position: $position")
            GridField(position)
        }
    }
}

fun getRandomSequency(): Flow<Int> {
    val listOfNumber = Array(2) { it }.toList()
    return listOfNumber.asFlow()
        .onEach {
            delay(1000)
        }
        .map {
            Random.nextInt(0, 15)
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
        modifier = Modifier.padding(start = 14.dp, end = 14.dp)
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


