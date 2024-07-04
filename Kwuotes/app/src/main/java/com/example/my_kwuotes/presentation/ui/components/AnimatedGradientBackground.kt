package com.example.my_kwuotes.presentation.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedGradientBackground(color1: Color, color2: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val color1 by infiniteTransition.animateColor(
        initialValue = color1,
        targetValue = color2,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val color2 by infiniteTransition.animateColor(
        initialValue = color2,
        targetValue = color1,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(color1, color2)
                )
            )
    )
}


@Composable
fun AnimatedGradientBackgroundModifier(color1: Color, color2: Color) : Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "")

//    val color1 by infiniteTransition.animateColor(
//        initialValue = color1,
//        targetValue = color2,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 4000, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ), label = ""
//    )
//
//    val color2 by infiniteTransition.animateColor(
//        initialValue = color2,
//        targetValue = color1,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 4000, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ), label = ""
//    )

    return Modifier.background(
        Brush.linearGradient(
            colors = listOf(color1,color2)
        )
    )
}

@Composable
fun simpleColorAnimated(color1: Color,color2: Color): Color{
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val color by infiniteTransition.animateColor(
        initialValue = color1,
        targetValue = color2,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    return color
}