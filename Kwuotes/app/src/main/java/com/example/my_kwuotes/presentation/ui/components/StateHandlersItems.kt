package com.example.my_kwuotes.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.kwuotes.R
import com.example.my_kwuotes.ui.theme.Brown01


@Composable
fun ErrorItem(
    errorMessage: String,
    modifier: Modifier,
    onRetry: () -> Unit
){
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = errorMessage)
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(onClick = {onRetry()}) {
            Text(text = LocalContext.current.resources.getString(R.string.retry), color = Color.Red)
        }
    }
}


@Composable
fun ErrorBar(
    errorMessage: String,
    modifier: Modifier,
    onRetry: () -> Unit
){
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(text = errorMessage)
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(onClick = {onRetry()}) {
            Text(text = LocalContext.current.resources.getString(R.string.retry), color = Color.Red)
        }
    }
}

@Composable
fun NullItem(
    modifier: Modifier,
    text: String? = null
){
    Box(modifier = modifier){
       Column ( modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally){
           Icon(
               imageVector = Icons.Default.PlaylistRemove,
               contentDescription = null,
               tint = Color.Red,
               modifier = Modifier
                   .size(104.dp)
           )
           Spacer(modifier = Modifier.height(8.dp))
           text?.let {
               Text(it, color = Brown01,)
           }
       }
    }
}