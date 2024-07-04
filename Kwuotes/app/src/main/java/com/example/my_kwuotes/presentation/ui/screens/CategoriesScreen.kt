package com.example.my_kwuotes.presentation.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kwuotes.R
import com.example.my_kwuotes.data.models.Tags
import com.example.my_kwuotes.data.models.TagsItem
import com.example.my_kwuotes.data.mappers.toTagItemEntity
import com.example.my_kwuotes.presentation.ui.components.ErrorBar
import com.example.my_kwuotes.presentation.ui.components.ErrorItem
import com.example.my_kwuotes.presentation.ui.components.LoadingDialog
import com.example.my_kwuotes.presentation.ui.components.NullItem
import com.example.my_kwuotes.presentation.ui.components.ShowCustomToast
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.utils.Resource
import com.example.my_kwuotes.presentation.utils.UserPrefManager
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import com.example.my_kwuotes.ui.theme.Blue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MutableCollectionMutableState",
    "CoroutineCreationDuringComposition"
)
@Composable
fun CategoriesScreen(
    navController: NavController,
    quotesViewModel: QuotesViewModel
){

    val tagList by quotesViewModel.tagList.observeAsState()
    val addTagResponse by quotesViewModel.addUserTagsResponse.observeAsState()
    val scope = rememberCoroutineScope()
    val checked = remember {
        mutableStateListOf<TagsItem>()
    }
    val context = LocalContext.current
    val userPrefManager = remember {
        UserPrefManager(context)
    }

    var navigate by remember {
        mutableStateOf(true)
    }

    var isError by remember {
        mutableStateOf(false)
    }

    var errorMessge by remember {
        mutableStateOf("")
    }
LaunchedEffect(Unit) {
    quotesViewModel.getAllTags()
}

    Scaffold (
        bottomBar = {
                    if (isError){
                        BottomAppBar {
                            ErrorBar(
                                errorMessage = errorMessge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                scope.launch {
                                    quotesViewModel.getAllTags()
                                }
                            }
                        }
                    }
        },
       topBar = {
           TopAppBar(
               colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
               ),
               title = { Text(text = "Choose Your Categories",fontSize= 16.sp) },
               actions = {
                   ElevatedButton(
                       colors = ButtonDefaults.buttonColors(
                           containerColor = Color.White,
                           contentColor = Blue
                       ),
                       elevation = ButtonDefaults.elevatedButtonElevation(
                           defaultElevation = 8.dp
                       ),
                       modifier = Modifier
                           .padding(16.dp),

                       onClick = {
                           val tagItemList = checked.mapIndexed { index, tagsItem ->
                               tagsItem.toTagItemEntity(index+1)
                           }
                           if (tagItemList.isNotEmpty()) {
                               quotesViewModel.clearTagCategories()
                               quotesViewModel.addUserTagsCategory(tagItemList)
                               Log.e("LIST_OF_TAGS","${tagItemList.size}")
                               tagItemList.forEach {
                                   Log.e("LIST_OF_TAGS","$it")
                               }
                           } else{
                               Toast.makeText(
                                   context,
                                   "category cannot be empty, select at least one category to continue",
                                   Toast.LENGTH_LONG
                               ).show()
                           }
                       }) {
                       Text(text = "Finish")
                   }
               }
           )
       } ,
        modifier = Modifier.fillMaxSize()
    ) {
       Box (
           modifier = Modifier
               .background(MaterialTheme.colorScheme.surface)
               .fillMaxSize(),
           contentAlignment = Alignment.Center
       ){
//           if(isError){
//               ErrorItem(
//                   errorMessage = errorMessge,
//                   modifier = Modifier
//                       .fillMaxSize()
//                       .align(Alignment.Center)
//               ) {
//                   scope.launch {
//                       quotesViewModel.getAllTags()
//                   }
//               }
//           }
           LazyColumn(
               modifier = Modifier.fillMaxSize()
           ) {
             when(tagList){
                 is Resource.Error -> {
                    item {
                        val errMsg = (tagList as Resource.Error<Tags>).message.toString()
                        Toast.makeText(context,errMsg,Toast.LENGTH_LONG).show()
                        isError = true
                        errorMessge = errMsg
                    }
                 }

                 is Resource.Loading -> {
                     isError = false
                     item {
                         LoadingDialog(isDialogOpen = true, onDismissRequest = {})
                     }
                 }
                 is Resource.Success -> {
                     isError = false
                      val tagsAsCategories = (tagList as Resource.Success<Tags>).data!!.toList()
                     items(tagsAsCategories, key = {it._id}){ tagItem ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .animateItemPlacement(
                                    tween(250)
                                )
                                .fillMaxWidth()
                                .padding(16.dp, 8.dp)
                        ) {
                            BadgedBox(badge = {
                                Badge {
                                    Text(text = tagItem.quoteCount.toString()+ stringResource(R.string.quotess))
                                }
                            }) {
                                Text(text = tagItem.name)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Checkbox(
                                checked = checked.contains(tagItem) ,
                                onCheckedChange = {
                                    if (checked.contains(tagItem)){
                                        checked.remove(tagItem)
                                    } else{
                                        checked.add(tagItem)
                                    }
                                    Log.d("TAGS_LIST", "${checked.size}")
                                }
                            )
                        }
                     }


                 }
                 null -> {
                       item {
//                       NullItem(modifier = Modifier
//                           .fillMaxSize()
//                           .align(Alignment.Center))
                       }
                 }
             }
           }

       }
        when (addTagResponse){
            is Resource.Error -> {
                val errMsg = (addTagResponse as Resource.Error<String>).message
                if (errMsg != null) {
                    ShowCustomToast(errMsg)
                    scope.launch {
                        delay(2000)
                        quotesViewModel.addUserTagsResponse.postValue(null)
                    }
                }
            }
            is Resource.Loading -> {
                LoadingDialog(isDialogOpen = true, onDismissRequest = { /*TODO*/ })
            }
            is Resource.Success -> {
                val successMsg = (addTagResponse as Resource.Success<String>).message.toString()
                ShowCustomToast(successMsg)
                userPrefManager.setHasSavedCategories(true)
               if(navigate){
                   navController.navigate(NavHelper.HomeScreen.route) {
                       popUpTo(NavHelper.CategoriesScreen.route) {
                           inclusive = true
                       }
                   }
                   navigate = false
               }
                Log.d("USERTAGS", successMsg)
                scope.launch {
                    delay(2000)
                    quotesViewModel.addUserTagsResponse.postValue(null)
                }
            }
            null -> {
//                NullItem(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

