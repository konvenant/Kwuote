package com.example.my_kwuotes.presentation.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Picture
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.example.kwuotes.R
import com.example.my_kwuotes.presentation.ui.components.BottomSheetItem
import com.example.my_kwuotes.presentation.ui.components.CaptureBitmap
import com.example.my_kwuotes.presentation.ui.components.GradientOptionButton
import com.example.my_kwuotes.presentation.ui.components.saveToDisk
import com.example.my_kwuotes.presentation.ui.components.shareBitmap
import com.example.my_kwuotes.presentation.ui.components.shareQuoteAsText
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.utils.captureComposableAsBitmap
import com.example.my_kwuotes.presentation.utils.createBitmapFromPicture
import com.example.my_kwuotes.ui.theme.Blue
import com.example.my_kwuotes.ui.theme.Brown01
import com.example.my_kwuotes.ui.theme.Gray01
import com.example.my_kwuotes.ui.theme.Gray02
import com.example.my_kwuotes.ui.theme.Red0
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ShareQuoteScreen(
    navController: NavController,
    category: String,
    content:String,
    author: String
) {

    val context = LocalContext.current

    var selectedGradient by remember { mutableStateOf(Blue) }
    var selectedGradientIndex by remember { mutableIntStateOf(0) }
    var saveBitmap by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val picture = remember { Picture() }
    val snackbarHostState = remember { SnackbarHostState() }
    val buttonClick = remember { mutableStateOf(false) }

    val font = FontFamily(
        Font(R.font.wellfleet_regular, FontWeight.Bold)
    )

    val font2 = FontFamily(
        Font(R.font.handlee_regular, FontWeight.Normal)
    )

    val font3 = FontFamily(
        Font(R.font.adventpro_variable, FontWeight.Normal)
    )

    val bitmapSize = IntSize(1080, 1920)
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val clipboardManager = LocalClipboardManager.current

    val writeStorageAccessState = rememberMultiplePermissionsState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // No permissions are needed on Android 10+ to add files in the shared storage
            emptyList()
        } else {
            listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    )

    val snapShot = CaptureBitmap {
       Box(
           modifier = Modifier
               .fillMaxWidth()
               .background(
                   selectedGradient
               )
               .padding(16.dp, 32.dp)
       ){
           Column(
               modifier = Modifier
                   .align(Alignment.Center)
                   .fillMaxWidth()
                   .clip(RoundedCornerShape(20.dp))
                   .background(
                       Brush.linearGradient(
                           colors = listOf(selectedGradient, Color.Black)
                       )
                   )
                   .padding(16.dp),
               verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally
           ) {
               Row (
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(8.dp),
                   horizontalArrangement = Arrangement.Start,
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   Text(
                       text = category,
                       modifier = Modifier
                           .padding(8.dp)
                           .background(Color.White, RoundedCornerShape(8.dp))
                           .padding(8.dp),
                       fontFamily = font3,
                       color = Blue
                   )
               }
               Spacer(modifier = Modifier.height(16.dp))
               Text(
                   text = if(content.length < 100) content else content.slice(0..99)+"...." ,
                   style = TextStyle.Default.copy(
                       fontSize = 18.sp,
                       fontFamily = font,
                       color = Color.White
                   ),
                   textAlign = TextAlign.Center,
                   modifier = Modifier
                       .padding(16.dp,8.dp)
               )
               Spacer(modifier = Modifier.height(16.dp))
               Text(
                   text = author,
                   style = TextStyle.Default.copy(
                       fontSize = 14.sp,
                       fontFamily = font2,
                       color = Color.White
                   ),
               )
               Spacer(modifier = Modifier.height(16.dp))
               Row (
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(16.dp)
                   ,
                   verticalAlignment = Alignment.CenterVertically,
               ) {
                   Image(
                       painter = painterResource(id = R.drawable.kwuote_logo),
                       contentDescription = null,
                       contentScale = ContentScale.Crop,
                       modifier = Modifier
                           .size(20.dp)
                           .clip(CircleShape)
                   )
                   Spacer(modifier = Modifier.width(16.dp))
                   Text(
                       text = stringResource(id = R.string.app_name),
                       style = TextStyle.Default.copy(
                           fontSize = 12.sp,
                           fontFamily = font2,
                           color = Color.White,
                           fontWeight = FontWeight.Bold
                       ))
               }


           }
       }
    }




    fun shareBitmapFromComposable() {
        if (writeStorageAccessState.allPermissionsGranted) {
            coroutineScope.launch(Dispatchers.IO) {
                val bitmapp = snapShot.invoke()
                val uri = bitmapp.saveToDisk(context)
                shareBitmap(context, uri)
            }
        } else if (writeStorageAccessState.shouldShowRationale) {
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "The storage permission is needed to save the image",
                    actionLabel = "Grant Access"
                )

                if (result == SnackbarResult.ActionPerformed) {
                    writeStorageAccessState.launchMultiplePermissionRequest()
                }
            }
        } else {
            writeStorageAccessState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect (bitmap.value) {
      if (bitmap.value != null){
          val uri = bitmap.value!!.saveToDisk(context)
          shareBitmap(context, uri)
          saveBitmap = false
      }
    }

    Scaffold  (
        modifier = Modifier
            .fillMaxSize()
           ,
        containerColor = Gray02,
        topBar = {
            TopAppBar(
                title = { Text(
                    text = stringResource(R.string.share_quote),
                    color = Color.White
                ) },
                navigationIcon = {
                  IconButton(
                      onClick = {
                                navController.navigateUp()
                      },
                      colors = IconButtonDefaults.iconButtonColors(
                          containerColor = Gray02,
                          contentColor = Color.White
                      )
                  ) {
                      Icon(
                          imageVector = Icons.Default.Close,
                          contentDescription = null
                      )
                  }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Gray01
                )
            )
        },
        bottomBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .padding(8.dp)
                    .background(Gray02)
                    ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BottomSheetItem(
                    text = stringResource(R.string.copy_quote),
                    icon = Icons.Outlined.ContentCopy
                ) {
                    val quote = "$content - $author ($category)"

                    clipboardManager.setText(AnnotatedString(quote))
                    Toast.makeText(context,
                        context.getString(R.string.quote_copied),Toast.LENGTH_SHORT).show()
                }

                BottomSheetItem(
                    text = stringResource(R.string.share_as_text),
                    icon = Icons.Outlined.Share
                ) {
                    val quote = "$content - $author ($category)"
                    shareQuoteAsText(context,quote)
                }

                BottomSheetItem(
                    text = stringResource(R.string.share_as_image),
                    icon = Icons.Outlined.Image
                ) {
                    shareBitmapFromComposable()
                }
            }
        }
    ) {
       Box(
           modifier = Modifier
               .fillMaxSize()
               .background(
                   Brush.linearGradient(
                       colors = listOf(selectedGradient, Color.Black)
                   )
               )
               .padding(32.dp, 64.dp)
               .clip(RoundedCornerShape(12.dp))

       ){
           Column(
               modifier = Modifier
                   .align(Alignment.Center)
                   .fillMaxWidth()
                   .clip(RoundedCornerShape(16.dp))
                   .background(
                       Brush.linearGradient(
                           colors = listOf(selectedGradient, Color.Black)
                       )
                   )
                   .padding(16.dp),
               verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally
           ) {
               Row (
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(8.dp),
                   horizontalArrangement = Arrangement.Start,
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   Text(
                       text = category,
                       modifier = Modifier
                           .padding(8.dp)
                           .background(Color.White, RoundedCornerShape(8.dp))
                           .padding(8.dp),
                       fontFamily = font3,
                       color = Blue
                   )
               }
               Spacer(modifier = Modifier.height(16.dp))
               Text(
                   text = if(content.length < 100) content else content.slice(0..99)+"...." ,
                   style = TextStyle.Default.copy(
                       fontSize = 18.sp,
                       fontFamily = font,
                       color = Color.White
                   ),
                   textAlign = TextAlign.Center,
                   modifier = Modifier
                       .padding(16.dp,8.dp)
               )
               Spacer(modifier = Modifier.height(16.dp))
               Text(
                   text = author,
                   style = TextStyle.Default.copy(
                       fontSize = 14.sp,
                       fontFamily = font2,
                       color = Color.White
                   ),
                    )
               Spacer(modifier = Modifier.height(16.dp))
               Row (
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(16.dp)
                   ,
                   verticalAlignment = Alignment.CenterVertically,
               ) {
                   Image(
                       painter = painterResource(id = R.drawable.kwuote_logo),
                       contentDescription = null,
                       contentScale = ContentScale.Crop,
                       modifier = Modifier
                           .size(20.dp)
                           .clip(CircleShape)
                   )
                   Spacer(modifier = Modifier.width(16.dp))
                   Text(
                       text = stringResource(id = R.string.app_name),
                       style = TextStyle.Default.copy(
                           fontSize = 12.sp,
                           fontFamily = font2,
                           color = Color.White,
                           fontWeight = FontWeight.Bold
                       ))
               }


           }
           Row (
               modifier = Modifier
                   .fillMaxWidth(0.7f)
                   .align(Alignment.BottomCenter)
                   .padding(8.dp)
                   .padding(bottom = 64.dp)
               ,
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.SpaceBetween
           ) {
               val  colors = listOf(Blue, Red0, Brown01)

               colors.forEachIndexed { index, color  ->
                   GradientOptionButton(
                       color = color,
                       onSelected = {
                           selectedGradient = it
                           selectedGradientIndex = index
                       },
                       isSelected = selectedGradientIndex == index
                   )

               }
           }



       }
    }
}






