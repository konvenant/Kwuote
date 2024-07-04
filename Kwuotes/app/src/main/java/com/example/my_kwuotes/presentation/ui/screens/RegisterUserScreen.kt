package com.example.my_kwuotes.presentation.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.rememberNavController
import com.example.kwuotes.R
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.utils.UserPrefManager
import com.example.my_kwuotes.ui.theme.Blue02
import com.example.my_kwuotes.ui.theme.Brown01
import com.example.my_kwuotes.ui.theme.Cream01
import com.example.my_kwuotes.ui.theme.Cream02
import com.example.my_kwuotes.ui.theme.Red0
import java.io.InputStream


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController
) {
    var name by remember { mutableStateOf(("")) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showComplete by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scale = animateFloatAsState(if (showDialog) 1f else 0f)
    val userPrefManager = remember {
        UserPrefManager(context)
    }
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val color1 by infiniteTransition.animateColor(
        initialValue = Cream02,
        targetValue = Cream01,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val color2 by infiniteTransition.animateColor(
        initialValue = Cream02,
        targetValue = Cream02,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val color3 by infiniteTransition.animateColor(
        initialValue = Red0,
        targetValue = Brown01,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    Scaffold (
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.register_your_details), color = color3)
            })
        }
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(color1, color2)
                    )
                )
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable { showDialog = true },
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                        .clickable { showDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(R.string.add_image))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (showComplete){
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.your_name)) },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White ,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)),
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        imageBitmap?.let { bitmap ->
                            userPrefManager.saveUserToPreferences(context, bitmap, name)
                            userPrefManager.setFirstTime(false)
                            navController.navigate(NavHelper.CategoriesScreen.route) {
                                popUpTo(NavHelper.RegisterUser.route) {
                                    inclusive = true
                                }
                            }

                        }
                    },
                    enabled = imageBitmap != null && name.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue02,
                        disabledContainerColor = Color.Gray,
                        contentColor = Color.White
                    )) {
                    Text(stringResource(R.string.register))
                }
            }
        }

        if (showDialog) {
            ImagePickerDialog(
                onImageSelected = {
                    imageBitmap = it
                    showDialog = false
                    showComplete = imageBitmap != null
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ImagePickerDialog(onImageSelected: (Bitmap) -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            onImageSelected(bitmap)
        }
        onDismiss()
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.select_image), style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { launcher.launch("image/*") }) {
                    Text(stringResource(R.string.pick_image_from_gallery))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { onDismiss() }) {
                    Text(stringResource(R.string.cancel1))
                }
            }
        }
    }
}

@Composable
@Preview
fun MyApp(){
    RegisterScreen(navController = rememberNavController())
}