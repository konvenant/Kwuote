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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kwuotes.R
import com.example.my_kwuotes.data.local.models.TagsItemEntity
import com.example.my_kwuotes.data.models.Tags
import com.example.my_kwuotes.data.models.TagsItem
import com.example.my_kwuotes.data.mappers.toTagItem
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
import com.example.my_kwuotes.ui.theme.Red0
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MutableCollectionMutableState",
    "CoroutineCreationDuringComposition"
)
@Composable
fun UserCategoriesScreen(
    navController: NavController,
    quotesViewModel: QuotesViewModel
){

    val tagList by quotesViewModel.userTags.observeAsState()
    val allTagList by quotesViewModel.tagList.observeAsState()
    val addTagResponse by quotesViewModel.addUserTagsResponse.observeAsState()
    val scope = rememberCoroutineScope()
    val checked = remember {
        mutableStateListOf<TagsItemEntity>()
    }

    val updatedChecked = remember {
        mutableStateListOf<TagsItem>()
    }

    var updateCategories by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val userPrefManager = remember {
        UserPrefManager(context)
    }

    var isError by remember {
        mutableStateOf(false)
    }

    var onceChecked by remember {
        mutableStateOf(true)
    }

    var errorMessge by remember {
        mutableStateOf("")
    }

LaunchedEffect(Unit) {
    quotesViewModel.getUserTagsCategory()
}

    Scaffold (
        bottomBar = {
            if (isError){
                BottomAppBar {
                    ErrorBar(
                        errorMessage = errorMessge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
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
               title = { Text(
                   text = if (updateCategories) stringResource(R.string.update_your_categories) else stringResource(
                       R.string.my_categories
                   ),
                   fontSize = 16.sp
               )},
               actions = {
                  if (updateCategories){
                      ElevatedButton(
                          colors = ButtonDefaults.buttonColors(
                              containerColor = Color.White,
                              contentColor = Blue
                          ),
                          elevation = ButtonDefaults.elevatedButtonElevation(
                              defaultElevation = 8.dp
                          ),
                          modifier = Modifier
                              .clip(CircleShape)
                              .padding(16.dp),
                          onClick = {
                              updateCategories = false
                          }){
                          Icon(
                              imageVector = Icons.Default.Cancel,
                              contentDescription = null,
                              tint = Red0
                          )

                      }

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
                              val tagItemList = updatedChecked.mapIndexed { index, tagsItem ->
                                  tagsItem.toTagItemEntity(index+1)
                              }
                              if (updatedChecked.isNotEmpty()) {
                                  quotesViewModel.clearTagCategories()
                                  quotesViewModel.addUserTagsCategory(tagItemList)
                              } else{
                                  Toast.makeText(
                                      context,
                                      context.getString(R.string.category_cannot_be_empty_select_at_least_one_category_to_continue),
                                      Toast.LENGTH_LONG
                                  ).show()
                              }
                          }) {
                          Text(text = stringResource(R.string.update))
                      }
                  } else{
                      ElevatedButton(
                          onClick = {
                              updateCategories = true
                              scope.launch{
                                  quotesViewModel.getAllTags()
                              }
                                    },
                          colors = ButtonDefaults.buttonColors(
                              containerColor = Color.White,
                              contentColor = Blue
                          ),
                          elevation = ButtonDefaults.elevatedButtonElevation(
                              defaultElevation = 8.dp
                          ),
                          shape = ButtonDefaults.elevatedShape
                      ) {
                          Icon(
                              imageVector = Icons.Default.Edit,
                              contentDescription = null
                          )
                      }
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
           LazyColumn(
               modifier = Modifier.fillMaxSize(),
               contentPadding = PaddingValues(top = 64.dp)
           ) {
           if(updateCategories){
               when(allTagList){
                   is Resource.Error -> {
                       item {
                           val errMsg = (allTagList as Resource.Error<Tags>).message.toString()
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
                       if (onceChecked){
                       checked.forEach {
                           if (!updatedChecked.contains(it.toTagItem())){
                               updatedChecked.add(it.toTagItem())
                           }
                       }
                           onceChecked = false
                        }
                       val tagsAsCategories = (allTagList as Resource.Success<Tags>).data!!.toList()
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
                                       Text(text = tagItem.quoteCount.toString()+ stringResource(R.string.quotes2))
                                   }
                               }) {
                                   Text(text = tagItem.name)
                               }

                               Checkbox(
                                   checked = updatedChecked.contains(tagItem) ,
                                   onCheckedChange = {
                                       if (updatedChecked.contains(tagItem)){
                                           updatedChecked.remove(tagItem)
                                       } else{
                                           updatedChecked.add(tagItem)
                                       }
                                       Log.d("TAGS_LIST", "${updatedChecked.size}")
                                   }
                               )
                           }
                       }


                   }
                   null -> {
                       item {
                           NullItem(modifier = Modifier.align(Alignment.Center))
                       }
                   }
               }
           } else{
               when(tagList){
                   is Resource.Error -> {
                       item {
                           val errMsg = (tagList as Resource.Error<List<TagsItemEntity>>).message.toString()
                           ErrorItem(
                               errorMessage = errMsg,
                               modifier = Modifier.align(Alignment.Center)
                           ) {

                           }
                       }
                   }

                   is Resource.Loading -> {
                       item {
                           LoadingDialog(isDialogOpen = true, onDismissRequest = {})
                       }
                   }
                   is Resource.Success -> {
                       val tagsAsCategories = (tagList as Resource.Success<List<TagsItemEntity>>).data!!.toList()
                       checked.clear()
                       checked.addAll(tagsAsCategories)
                       items(tagsAsCategories, key = {it.id}){ tagItem ->
                           Row(
                               verticalAlignment = Alignment.CenterVertically,
                               modifier = Modifier
                                   .animateItemPlacement(
                                       tween(250)
                                   )
                                   .fillMaxWidth()
                                   .padding(16.dp)
                           ) {

                               Text(text = tagItem.name)
                               Spacer(modifier = Modifier.width(8.dp))
                               Badge {
                                   Text(text = tagItem.quoteCount.toString()+ stringResource(R.string.quotes11))
                               }

                           }
                       }


                   }
                   null -> {
                       item {
                           NullItem(modifier = Modifier.align(Alignment.Center))
                       }
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
//                                 userPrefManager.setHasSavedCategories(true)
                Toast.makeText(context,
                    stringResource(R.string.categories_updated_successfully),Toast.LENGTH_LONG).show()
                scope.launch {
                    delay(2000)
                 quotesViewModel.addUserTagsResponse.postValue(null)
                }

                updateCategories = false
            }
            null -> {}
        }

    }
}


