package com.example.my_kwuotes.presentation.utils

import android.content.Intent.createChooser
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize

@Composable
fun captureComposableAsBitmap(content: @Composable () -> Unit, size: IntSize): Bitmap {
    val context = LocalContext.current
    val bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val composeView = ComposeView(context).apply {
        setContent {
            content()
        }
    }

    composeView.measure(
        View.MeasureSpec.makeMeasureSpec(size.width, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(size.height, View.MeasureSpec.EXACTLY)
    )
    composeView.layout(0, 0, composeView.measuredWidth, composeView.measuredHeight)
    composeView.draw(canvas)

    return bitmap
}
