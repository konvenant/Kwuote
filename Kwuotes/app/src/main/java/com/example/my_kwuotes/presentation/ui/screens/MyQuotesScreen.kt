package com.example.my_kwuotes.presentation.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.example.kwuotes.R
import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.data.mappers.toQUoteEntity
import com.example.my_kwuotes.presentation.ui.components.AddQuoteDialog
import com.example.my_kwuotes.presentation.ui.components.ConfirmDeleteDialog
import com.example.my_kwuotes.presentation.ui.components.LoadingDialog
import com.example.my_kwuotes.presentation.ui.components.MyQuoteItem
import com.example.my_kwuotes.presentation.ui.components.NullItem
import com.example.my_kwuotes.presentation.ui.components.QuoteItem
import com.example.my_kwuotes.presentation.ui.components.ShowCustomToast
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.utils.Resource
import com.example.my_kwuotes.presentation.utils.UserPrefManager
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import com.example.my_kwuotes.ui.theme.Blue
import com.example.my_kwuotes.ui.theme.Red0
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun MyQuotesScreen(
    navController: NavController,
    quotesViewModel: QuotesViewModel
) {
    val context = LocalContext.current

    val userPrefManager = remember {
        UserPrefManager(context)
    }

    val profileImage = userPrefManager.getImageFromPreferences(context)

    val font = FontFamily(
        Font(R.font.wellfleet_regular, FontWeight.Bold)
    )

    val font2 = FontFamily(
        Font(R.font.handlee_regular, FontWeight.Normal)
    )

    val font3 = FontFamily(
        Font(R.font.adventpro_variable, FontWeight.Normal)
    )

    val lazyListState = rememberLazyListState()

    var showDialog by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false)}

        var quoteId by remember { mutableStateOf("") }

    var quoteToDelete by remember {
        mutableStateOf<QuoteEntity?>(null)
    }

    val addQuoteState by quotesViewModel.addMyQuoteResponse.observeAsState()

    val deleteQuoteState by quotesViewModel.deleteMyQuoteResponse.observeAsState()

    val quotes = quotesViewModel.myQuotesFlow.collectAsLazyPagingItems()

    val scope = rememberCoroutineScope()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item {
                  Row(
                      modifier = Modifier
                          .fillMaxWidth()
                          .padding(8.dp),
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                      Text(text = stringResource(R.string.my_quotes), fontFamily = font)

                      AsyncImage(
                          model = profileImage.image,
                          contentDescription = null,
                          modifier = Modifier
                              .size(50.dp)
                              .padding(8.dp)
                              .border(1.dp, Red0, CircleShape)
                              .clip(CircleShape)
                              .clickable { },
                          contentScale = ContentScale.Crop
                      )

                  }
                }
                


                if(quotes.itemSnapshotList.items.isNotEmpty()){
                    items(quotes) { quote ->
                        if (quote != null) {
                            MyQuoteItem(quote,lazyListState, onClick =  { q ->
                                navController.navigate(NavHelper.QuoteScreen.route + "/${q.quoteId}/${q.category}")
                            }, onDelete = { q->
                                quoteToDelete = q.toQUoteEntity()
                                showDeleteDialog = true
                                quoteId = q.quoteId
                            })
                        }
                    }
                }
                else{
                item{
                    NullItem(
                        modifier = Modifier.fillMaxWidth().height(500.dp)
                            .align(Alignment.BottomCenter),
                        text = stringResource(id = R.string.no_quotes_found)
                    )
                }
            }
                item{
                    Box(modifier = Modifier.height(32.dp))
                }

                quotes.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillParentMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val e = quotes.loadState.refresh as LoadState.Error
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillParentMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Error: ${e.error.localizedMessage}",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }

                        loadState.append is LoadState.Error -> {
                            val e = quotes.loadState.append as LoadState.Error
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        stringResource(R.string.error1, e.error.localizedMessage),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    showDialog=true
                },
                containerColor = Blue,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 76.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            if (showDialog) {
                AddQuoteDialog(
                    onDismissRequest = { showDialog=false },
                    onSave = { newQuote ->
                        quotesViewModel.addMyQuote(newQuote)
                        showDialog=false
                    }
                )
            }

            if(showDeleteDialog){
                ConfirmDeleteDialog( onConfirm = {
                    quoteToDelete?.let { quotesViewModel.deleteMyQuote(it) }
                   scope.launch {
                       val isFav = quotesViewModel.favQuoteDuplicate(quoteId)
                       if (isFav){
                           quoteToDelete?.let { quotesViewModel.deleteFavQuote(it) }
                       }
                   }

                    showDeleteDialog = false
                }) {
                    showDeleteDialog = false
                }
            }

            when (val state = addQuoteState) {
                is Resource.Loading -> {
                    LoadingDialog(
                        isDialogOpen = true,
                        onDismissRequest = { }
                    )
                }

                is Resource.Success -> {
                    state.data?.let { message ->
                        ShowCustomToast(message)
                        scope.launch {
                            delay(2000)
                            quotesViewModel.addMyQuoteResponse.postValue(null)
                        }
                    }

                }

                is Resource.Error -> {
                    state.message?.let { message ->
                        ShowCustomToast(message)
                        scope.launch {
                            delay(2000)
                            quotesViewModel.addMyQuoteResponse.postValue(null)
                        }
                    }

                }

                else -> {
                    // Do nothing
                }
            }

            when (val state = deleteQuoteState) {
                is Resource.Loading -> {
                    LoadingDialog(
                        isDialogOpen = true,
                        onDismissRequest = { }
                    )
                }

                is Resource.Success -> {
                    state.data?.let { message ->
                        ShowCustomToast(message)
                        scope.launch {
                            delay(2000)
                            quotesViewModel.deleteMyQuoteResponse.postValue(null)
                        }
                    }
                }

                is Resource.Error -> {
                    state.message?.let { message ->
                        ShowCustomToast(message)
                        scope.launch {
                            delay(2000)
                            quotesViewModel.deleteMyQuoteResponse.postValue(null)
                        }
                    }
                }

                else -> {
                    // Do nothing
                }
            }
        }


}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun MyQuotePrev(){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Quotes") },
                actions = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_2),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(8.dp)
                            .border(1.dp, Red0, CircleShape)
                            .clickable { }
                            .padding(8.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                },
                containerColor = Blue
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) {  paddingValues ->

    }
}


