package com.example.my_kwuotes.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import com.example.my_kwuotes.presentation.ui.components.ShowCustomToast
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.utils.Resource
import com.example.my_kwuotes.presentation.utils.UserPrefManager
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import com.example.my_kwuotes.ui.theme.Blue
import com.example.my_kwuotes.ui.theme.Red0
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(
    navController: NavController,
    quotesViewModel: QuotesViewModel
){
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

    var showDeleteDialog by remember { mutableStateOf(false) }

    var quoteToDelete by remember {
        mutableStateOf<QuoteEntity?>(null)
    }

    val deleteQuoteState by quotesViewModel.deleteFavQuoteResponse.observeAsState()

    val scope = rememberCoroutineScope()

    val quotes = quotesViewModel.favQuotesFlow.collectAsLazyPagingItems()

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
                    Text(text = stringResource(R.string.favorite_quotes), fontFamily = font)

                    AsyncImage(
                        model = profileImage.image,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(8.dp)
                            .border(1.dp, Red0, CircleShape)
                            .clip(CircleShape)
                            .clickable { }
                            .padding(8.dp),
                        contentScale = ContentScale.Crop
                    )

                }
            }




            if(quotes.itemCount >= 1){
                items(quotes) { quote ->
                    if (quote != null) {
                        MyQuoteItem(quote,lazyListState, onClick =  { q ->
                            navController.navigate(NavHelper.QuoteScreen.route + "/${q.quoteId}/${q.category}")
                        }, onDelete = { q->
                            quoteToDelete = q.toQUoteEntity()
                            showDeleteDialog = true
                        })
                    } else{
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ){
                            NullItem(modifier = Modifier.fillMaxSize(),text = stringResource(id = R.string.no_quotes_found))
                        }
                    }
                }
            } else{
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
                                    stringResource(R.string.error, e.error.localizedMessage),
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
                                    stringResource(R.string.error, e.error.localizedMessage),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }



//        if (showDialog) {
//            AddQuoteDialog(
//                onDismissRequest = { showDialog=false },
//                onSave = { newQuote ->
//                    quotesViewModel.addMyQuote(newQuote)
//                    showDialog=false
//                }
//            )
//        }

        if(showDeleteDialog){
            ConfirmDeleteDialog( onConfirm = {
                quoteToDelete?.let {
                    quotesViewModel.deleteFavQuote(it)

                }
                showDeleteDialog = false
            }) {
                showDeleteDialog = false
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
                    scope.launch{
                        delay(2000)
                        quotesViewModel.deleteFavQuoteResponse.postValue(null)
                    }
                }

            }

            is Resource.Error -> {
                state.message?.let { message ->
                    ShowCustomToast(message)
                    scope.launch{
                        delay(2000)
                        quotesViewModel.deleteFavQuoteResponse.postValue(null)
                    }
                }
            }

            else -> {
                // Do nothing
            }
        }
    }


}