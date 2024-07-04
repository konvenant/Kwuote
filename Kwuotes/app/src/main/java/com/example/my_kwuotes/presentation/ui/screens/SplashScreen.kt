package com.example.my_kwuotes.presentation.ui.screens

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kwuotes.R
import com.example.my_kwuotes.data.models.RandomQuote
import com.example.my_kwuotes.data.mappers.toQuote
import com.example.my_kwuotes.presentation.ui.components.AnimatedGradientBackground
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.utils.NotificationHandler
import com.example.my_kwuotes.presentation.utils.Resource
import com.example.my_kwuotes.presentation.utils.UserPrefManager
import com.example.my_kwuotes.presentation.utils.showNotification
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import com.example.my_kwuotes.ui.theme.Blue
import com.example.my_kwuotes.ui.theme.KwuotesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashScreen(
    navController: NavController,
    quotesViewModel: QuotesViewModel
) {
    val context = LocalContext.current

    val userPrefManager = remember {
        UserPrefManager(context)
    }
    val userSetting  = userPrefManager.getUserSettings()

    val randomQuote by quotesViewModel.randomQuote.observeAsState()

    val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val notificationHandler = NotificationHandler(context)

    var showNotification by remember{
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = true) {
        if (!postNotificationPermission.hasPermission) {
            postNotificationPermission.launchPermissionRequest()
        }
    }

    LaunchedEffect(true) {
        delay(3000)
        if (userPrefManager.getFirstTime()) {
            navController.navigate(NavHelper.OnboardingScreen.route) {
                popUpTo(NavHelper.SplashScreen.route) {
                    inclusive = true
                }
            }

        } else {
            if (userPrefManager.getHasSavedCategories()){
                navController.navigate(NavHelper.HomeScreen.route) {
                    popUpTo(NavHelper.SplashScreen.route) {
                        inclusive = true
                    }
                }
            } else {
                navController.navigate(NavHelper.CategoriesScreen.route) {
                    popUpTo(NavHelper.SplashScreen.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    LaunchedEffect(true) {
        if (userPrefManager.getHasSavedCategories()){
            quotesViewModel.getRandomQuote()
        }
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedGradientBackground(Color.Black, Blue)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.kwuote_logo), // Replace with your logo
                contentDescription = null,
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
           when(randomQuote){
               is Resource.Error -> {
                   Text(
                       text = stringResource(R.string.setback_but_i_can_t_stop),
                       style = MaterialTheme.typography.headlineLarge.copy(
                           fontWeight = FontWeight.Bold,
                           fontSize = 18.sp,
                           color = Color.White // Make sure the text is visible on the gradient background
                       ),
                       textAlign = TextAlign.Center
                   )
               }
               is Resource.Loading -> {
                   Text(
                       text = stringResource(R.string.preparing_your_quotes),
                       style = MaterialTheme.typography.headlineLarge.copy(
                           fontWeight = FontWeight.Bold,
                           fontSize = 18.sp,
                           color = Color.White // Make sure the text is visible on the gradient background
                       ),
                       textAlign = TextAlign.Center
                   )
               }
               is Resource.Success -> {
                   val randomQuoteToDisplay = (randomQuote as Resource.Success<RandomQuote>).data
                   if (randomQuoteToDisplay != null) {
                       Text(
                           text = "${randomQuoteToDisplay.content} - ${randomQuoteToDisplay.author}",
                           style = MaterialTheme.typography.headlineLarge.copy(
                               fontWeight = FontWeight.Bold,
                               fontSize = 18.sp,
                               color = Color.White // Make sure the text is visible on the gradient background
                           ),
                           textAlign = TextAlign.Center
                       )
                       if(userSetting.notificationsEnabled){
                           if (showNotification){
                               notificationHandler.showSimpleNotification(randomQuoteToDisplay.toQuote())
                               showNotification = false
                           }
                       }

                   } else{
                       Text(
                           text = stringResource(R.string.one_day_or_day_one_it_is_your_choice),
                           style = MaterialTheme.typography.headlineLarge.copy(
                               fontWeight = FontWeight.Bold,
                               fontSize = 18.sp,
                               color = Color.White // Make sure the text is visible on the gradient background
                           ),
                           textAlign = TextAlign.Center
                       )
                   }
               }
               null -> {

               }
           }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun SplashScreen(){
    KwuotesTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedGradientBackground(Color.Black, Blue)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.kwuote_logo), // Replace with your logo
                    contentDescription = null,
                    modifier = Modifier.size(150.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your inspiring quote goes here",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White // Make sure the text is visible on the gradient background
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}