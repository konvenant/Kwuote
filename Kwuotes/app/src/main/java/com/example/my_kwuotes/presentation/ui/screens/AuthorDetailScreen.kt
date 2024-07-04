package com.example.my_kwuotes.presentation.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kwuotes.R
import com.example.my_kwuotes.data.mappers.toQuote
import com.example.my_kwuotes.data.models.Author
import com.example.my_kwuotes.data.models.AuthorById
import com.example.my_kwuotes.presentation.ui.components.ErrorBar
import com.example.my_kwuotes.presentation.ui.components.LoadingDialog
import com.example.my_kwuotes.presentation.ui.components.NullItem
import com.example.my_kwuotes.presentation.ui.components.QuoteItem
import com.example.my_kwuotes.presentation.ui.components.openUrl
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.utils.Resource
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorDetailScreen(
    authorId: String,
    viewModel: QuotesViewModel,
    navController: NavController
) {

    LaunchedEffect(true) {
        viewModel.getAuthorById(authorId)
    }
    var isFav by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    val authorById by viewModel.authorById.observeAsState()
    var isError by remember {
        mutableStateOf(false)
    }

    var errorMessge by remember {
        mutableStateOf("")
    }
    Scaffold(
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
                           viewModel.getAuthorById(authorId)
                        }
                    }
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()){
           when(authorById){
               is Resource.Error -> {
                   val errMsg = (authorById as Resource.Error<AuthorById>).message.toString()
                   Toast.makeText(context,errMsg, Toast.LENGTH_LONG).show()
                   isError = true
                   errorMessge = errMsg
               }
               is Resource.Loading -> {
                   isError = false
                   LoadingDialog(isDialogOpen = true, onDismissRequest = {})
               }
               is Resource.Success -> {
                   isError = false
                   val authorFound = (authorById as Resource.Success<AuthorById>).data
                   LazyColumn {
                       authorFound?.let { author ->
                       item {
                               Column(
                                   modifier = Modifier
                                       .fillMaxSize()
                                       .padding(16.dp)
                               ) {
                                   Row(
                                       verticalAlignment = Alignment.CenterVertically
                                   ) {
                                       Image(
                                           painter = painterResource(id = R.drawable.kwuote_logo), // Placeholder image
                                           contentDescription = "Author Image",
                                           modifier = Modifier
                                               .size(100.dp)
                                               .background(Color.Gray, shape = CircleShape)
                                               .padding(8.dp)
                                               .clip(CircleShape),
                                           contentScale = ContentScale.Crop
                                       )

                                       Spacer(modifier = Modifier.width(16.dp))

                                       Column {
                                           Text(
                                               text = author.name,
                                               style = MaterialTheme.typography.headlineMedium,
                                               maxLines = 1,
                                               overflow = TextOverflow.Ellipsis
                                           )
                                           Spacer(modifier = Modifier.height(8.dp))
                                           Text(
                                               text = author.bio,
                                               style = MaterialTheme.typography.bodyMedium,
                                               maxLines = 3,
                                               overflow = TextOverflow.Ellipsis
                                           )
                                       }
                                   }

                                   Spacer(modifier = Modifier.height(16.dp))

                                   Text(
                                       text = stringResource(R.string.description),
                                       style = MaterialTheme.typography.headlineSmall
                                   )
                                   Spacer(modifier = Modifier.height(8.dp))
                                   Text(
                                       text = author.description,
                                       style = MaterialTheme.typography.bodySmall
                                   )

                                   Spacer(modifier = Modifier.height(16.dp))

                                   Text(
                                       text = stringResource(R.string.link, author.link),
                                       style = MaterialTheme.typography.bodyMedium,
                                       color = MaterialTheme.colorScheme.primary,
                                       modifier = Modifier.clickable { openUrl(context, author.link) }
                                   )

                                   Spacer(modifier = Modifier.height(16.dp))

                                   Text(
                                       text = stringResource(R.string.quotes),
                                       style = MaterialTheme.typography.headlineSmall
                                   )
                               }
                           }
                           
                           items(author.quotes){ 
                               QuoteItem(quote = it.toQuote(), listState = lazyListState ) {q ->
                                   navController.navigate(NavHelper.QuoteScreen.route+"/${q.quoteId}"+"/${q.category}")
                               }
                           }
                       }  ?: item {
                           Box(
                               modifier = Modifier
                                   .fillMaxSize()
                                   .clickable { },
                               contentAlignment = Alignment.Center
                           ){
                               NullItem(modifier = Modifier.fillMaxSize(),text = "")
                           }
                       }
                   }
               }
               null -> {

               }
           }
        }


    }


}
