package com.salosoft.touchgame.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


val Purple200 = Color(0xFF6243FF)
val Blue200 = Color(0xFF0013CA)
val Turquoise = Color(0xFF21BDCA)
val MainBackGround = Color(0xFFE5E5E5)
val TextColor = Color(0xFF6B6B83)

private val GradientStart = Color(0xFF31B5FE)
private val GradientEnd = Color(0xFF6243FF)

val GradientBlue = Brush.verticalGradient(
    listOf(
        GradientStart,
        GradientEnd,
        GradientEnd
    )
)
