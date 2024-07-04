package com.example.my_kwuotes.presentation.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import com.example.kwuotes.R
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.utils.UserPref
import com.example.my_kwuotes.presentation.utils.UserPrefManager
import com.example.my_kwuotes.presentation.utils.UserSettings
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import com.example.my_kwuotes.ui.theme.Blue02
import com.example.my_kwuotes.ui.theme.Cream01
import com.example.my_kwuotes.ui.theme.Cream03
import com.example.my_kwuotes.ui.theme.Red0

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserMoreScreen(
    navController: NavController,
    mainNavController: NavController,
    quotesViewModel: QuotesViewModel
){
    val context = LocalContext.current
    val userPrefManager = remember {
        UserPrefManager(context)
    }
    val initialUser = userPrefManager.getImageFromPreferences(context)
    var user by remember {
        mutableStateOf<UserPref>(initialUser)
    }

    val initialSettings = userPrefManager.getUserSettings()
    var settings by remember {
        mutableStateOf<UserSettings>(initialSettings)
    }

    var showSettingsDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
//    var showMyCategoriesDialog by remember { mutableStateOf(false) }
//    var showMyQuotesDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.profile)
                ) }
            )
        }
    ) {

        LazyColumn (
            contentPadding = PaddingValues(vertical = 64.dp)
        ){
          item {
              Column(
              modifier = Modifier
                  .background(MaterialTheme.colorScheme.surface)
                  .fillMaxSize()
                  .padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally
          ) {
              AsyncImage(
                  model = user.image,
                  contentDescription = "Profile Image",
                  modifier = Modifier
                      .size(100.dp)
                      .clip(CircleShape)
                      .border(2.dp, Color.Gray, CircleShape)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(text = user.name, style = MaterialTheme.typography.headlineSmall)
              Spacer(modifier = Modifier.height(16.dp))

                  ActionCard(
                      icon = Icons.Default.Edit,
                      title = stringResource(R.string.edit_profile),
                      description = stringResource(R.string.edit_your_profile_information),
                      onClick = {showEditProfileDialog=true}
                  )

              ActionCard(
                  icon = Icons.Default.Settings,
                  title = stringResource(R.string.settings),
                  description = stringResource(R.string.manage_your_settings),
                  onClick = {showSettingsDialog=true}
              )
              ActionCard(
                  icon = Icons.Default.List,
                  title = stringResource(R.string.my_categories1),
                  description = stringResource(R.string.view_and_edit_your_categories)
                  ,
                  onClick = { mainNavController.navigate(NavHelper.UserCategoriesScreen.route)}
              )
              ActionCard(
                  icon = Icons.Default.Book,
                  title = stringResource(R.string.my_quotes1),
                  description = stringResource(R.string.view_your_quotes),
                  onClick = {
                      navController.navigate(NavHelper.UserQuoteScreen.route){
                          popUpTo(navController.graph.findStartDestination().id){
                              saveState = true
                          }
                          launchSingleTop = true
                          restoreState = true
                      }
                  }
              )
              ActionCard(
                  icon = Icons.Default.Help,
                  title = stringResource(R.string.help),
                  description = stringResource(R.string.get_help_and_support),
                  onClick = {  }
              )
          }
          }
        }


        if (showEditProfileDialog){
            EditProfileDialog(
                userPref = user,
                onDismissRequest = { showEditProfileDialog = false },
            ) { userPref ->
                userPrefManager.saveUserToPreferences(context, userPref.image!!, userPref.name)
                user = userPref
            }
        }

        if(showSettingsDialog){
            SettingsDialog(
                settings = settings,
                onDismissRequest = { showSettingsDialog = false },
                onSettingsChange = { newSettings ->
                    userPrefManager.saveUserSettingsPreferences(newSettings)
                    settings = newSettings
                },
                quotesViewModel = quotesViewModel
            )
        }
    }
}

@Composable
fun ActionCard(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Cream03,
            contentColor = Blue02
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.headlineSmall)
                Text(text = description, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


@Composable
fun EditProfileDialog(
    userPref: UserPref,
    onDismissRequest: () -> Unit,
    onUpdate: (UserPref) -> Unit
) {
    var name by remember { mutableStateOf(userPref.name) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> imageUri = uri }
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(R.string.edit_profile1)) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    imageUri?.let {
                        val bitmap = remember {
                            MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                        }
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape)
                        )
                        imageBitmap = bitmap
                    } ?: userPref.image?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape)
                        )
                        imageBitmap = it
                    }
                    IconButton(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Red0
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Update Image",
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(stringResource(R.string.name)) }
                )

            }
        },
        confirmButton = {
            Button(onClick = {
                imageBitmap?.let {
                    val newUser = UserPref(
                        name,imageBitmap
                    )
                    onUpdate(newUser)
                }
                onDismissRequest()
            }) {
                Text(stringResource(R.string.update8))
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel11))
            }
        }
    )
}


@Composable
fun SettingsDialog(
    settings: UserSettings,
    onDismissRequest: () -> Unit,
    onSettingsChange: (UserSettings) -> Unit,
    quotesViewModel: QuotesViewModel
) {
    var darkTheme by remember { mutableStateOf(settings.isDarkTheme) }
    var language by remember { mutableStateOf(settings.currentLanguage) }
    var notifications by remember { mutableStateOf(settings.notificationsEnabled) }
    var updates by remember { mutableStateOf(settings.updatesEnabled) }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(R.string.settings1)) },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.dark_theme))
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = darkTheme,
                        onCheckedChange = {
                            darkTheme = it
                            onSettingsChange(settings.copy(isDarkTheme = it))
                            quotesViewModel.toggleTheme(it)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.language))
                    Spacer(modifier = Modifier.weight(1f))
                   TextButton(onClick = { expanded = true }) {
                       Text(text = language)
                   }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {expanded = false},
                        modifier = Modifier.wrapContentSize()
                    ) {


                        listOf(stringResource(R.string.english),
                            stringResource(R.string.spanish), stringResource(R.string.french)
                        ).forEach { lang ->
                            DropdownMenuItem(onClick = {
                                language = lang
                                onSettingsChange(settings.copy(currentLanguage = lang))
                               when(lang){
                                   context.resources.getString(R.string.english) -> {
                                       val locale = LocaleListCompat.forLanguageTags("en")
                                       AppCompatDelegate.setApplicationLocales(locale)
                                   }
                                   context.resources.getString(R.string.french) -> {
                                       val locale = LocaleListCompat.forLanguageTags("fr")
                                       AppCompatDelegate.setApplicationLocales(locale)
                                   }
                                   context.resources.getString(R.string.spanish) -> {
                                       val locale = LocaleListCompat.forLanguageTags("es")
                                       AppCompatDelegate.setApplicationLocales(locale)
                                   }
                               }
                                expanded = false
                            }, text = {  Text(text = lang)})
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.notifications))
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = notifications,
                        onCheckedChange = {
                            notifications = it
                            onSettingsChange(settings.copy(notificationsEnabled = it))
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.receive_updates))
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = updates,
                        onCheckedChange = {
                            updates = it
                            onSettingsChange(settings.copy(updatesEnabled = it))
                        }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismissRequest) {
                Text(stringResource(R.string.close))
            }
        }
    )
}

