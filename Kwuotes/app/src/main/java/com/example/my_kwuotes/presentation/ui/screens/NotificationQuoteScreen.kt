package com.example.my_kwuotes.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kwuotes.R
import com.example.my_kwuotes.data.models.Quote
import com.example.my_kwuotes.data.mappers.toQUoteEntity
import com.example.my_kwuotes.presentation.ui.components.LoadingDialog
import com.example.my_kwuotes.presentation.ui.components.ShowCustomToast
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.utils.Resource
import com.example.my_kwuotes.presentation.utils.UserPrefManager
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import com.example.my_kwuotes.ui.theme.Blue
import com.example.my_kwuotes.ui.theme.Red0
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationQuoteScreen(
    navController: NavController,
    quoteId: String,
    quotesViewModel: QuotesViewModel,
    category: String
) {
    val font = FontFamily(
        Font(R.font.wellfleet_regular, FontWeight.Bold)
    )

    val font2 = FontFamily(
        Font(R.font.handlee_regular, FontWeight.Normal)
    )

    val font3 = FontFamily(
        Font(R.font.adventpro_variable, FontWeight.Normal)
    )

    LaunchedEffect (Unit) {
        quotesViewModel.getQuotesById(quoteId,category)
    }

    val quote by quotesViewModel.quoteById.observeAsState()

    val context = LocalContext.current

    val userPrefManager = remember {
        UserPrefManager(context)
    }

    val profileImage = userPrefManager.getImageFromPreferences(context)


    var isFav by remember {
        mutableStateOf(false)
    }

    val addQuoteState by quotesViewModel.addFavQuoteResponse.observeAsState()

    val deleteQuoteState by quotesViewModel.deleteFavQuoteByIdResponse.observeAsState()

    val isFavorite by quotesViewModel.isFavorite.observeAsState()

    val scope = rememberCoroutineScope()

    LaunchedEffect (quoteId) {
        quotesViewModel.doesQuoteExist(quoteId)
        isFav = isFavorite == true
    }

    Scaffold (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {navController.navigate(NavHelper.HomeScreen.route)}) {
                        Icon(
                            imageVector = Icons.Default.ArrowCircleLeft,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                   AsyncImage(
                       model =  profileImage.image,
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
                    quote?.let {
                        navController.navigate(NavHelper.ShareQuoteScreen.route+"/${it.content}"+"/${it.author}"+"/${it.category}")
                    }
                },
                shape = FloatingActionButtonDefaults.extendedFabShape,
                modifier = Modifier
                    .clip(CircleShape),
                containerColor = Blue,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(8.dp,12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) {
          LazyColumn(
              verticalArrangement = Arrangement.Center,
              contentPadding = PaddingValues(top = 64.dp)
          ) {
            if (quote != null){
                item{
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth()
                                .padding(32.dp)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Blue, Color.Black)
                                    ),
                                    RoundedCornerShape(16.dp)
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                quote?.let { it1 ->
                                    LazyRow (modifier = Modifier.fillMaxWidth(0.7f)){
                                        items(it1.tags){ tag ->
                                            Text(
                                                text = tag,
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .background(Color.White, RoundedCornerShape(8.dp))
                                                    .padding(8.dp),
                                                fontFamily = font3,
                                                color = Blue
                                            )
                                        }
                                    }
                                }

                                when(isFavorite){

                                    true -> {
                                        IconButton(
                                            onClick = {
                                                quote?.let {
                                                        it1 -> quotesViewModel.deleteFavQuoteById(it1.quoteId)
                                                    quotesViewModel.doesQuoteExist(quoteId)
                                                }

                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Favorite,
                                                contentDescription = null,
                                                tint = Color.White
                                            )
                                        }
                                    }
                                    false -> {
                                        IconButton(
                                            onClick = {
                                                quote?.let {
                                                        it1 -> quotesViewModel.addFavQuote(it1.toQUoteEntity())
                                                    quotesViewModel.doesQuoteExist(quoteId)
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.FavoriteBorder,
                                                contentDescription = null,
                                                tint = Color.White
                                            )
                                        }
                                    }
                                    null -> {
                                        IconButton(
                                            onClick = {
                                                quote?.let {
                                                        it1 -> quotesViewModel.addFavQuote(it1.toQUoteEntity())
                                                    quotesViewModel.doesQuoteExist(quoteId)
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.FavoriteBorder,
                                                contentDescription = null,
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            }

//                  Spacer(modifier = Modifier.height(32.dp))

                            Column (
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                quote?.let { it1 ->
                                    Text(
                                        text = it1.content,
                                        style = TextStyle.Default.copy(
                                            fontSize = 18.sp,
                                            fontFamily = font,
                                            color = Color.White
                                        ),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(16.dp,8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                quote?.let { it1 ->
                                    Text(
                                        text = it1.author,
                                        style = TextStyle.Default.copy(
                                            fontSize = 14.sp,
                                            fontFamily = font2,
                                            color = Color.White
                                        ),
                                        modifier = Modifier
                                            .clickable { navController.navigate(NavHelper.AuthorFullDetailsScreen.route+"/${it1.authorSlug}") }
                                    )
                                }
                            }


//                  Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = stringResource(R.string.posted_at) +": ${quote?.dateAdded}",
                                style = TextStyle.Default.copy(
                                    fontSize = 14.sp,
                                    fontFamily = font3,
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            } else{
                item{
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth()
                                .height(65.dp)
                                .padding(32.dp)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Red0, Color.Black)
                                    ),
                                    RoundedCornerShape(16.dp)
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {

                        }}}
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
                        quotesViewModel.addFavQuoteResponse.postValue(null)
                    }
                }
            }

            is Resource.Error -> {
                state.message?.let { message ->
                    ShowCustomToast(message)
                    scope.launch {
                        delay(2000)
                        quotesViewModel.addFavQuoteResponse.postValue(null)
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
                        quotesViewModel.deleteFavQuoteByIdResponse.postValue(null)
                    }
                }
            }

            is Resource.Error -> {
                state.message?.let { message ->
                    ShowCustomToast(message)
                    scope.launch {
                        delay(2000)
                        quotesViewModel.deleteFavQuoteByIdResponse.postValue(null)
                    }
                }
            }

            else -> {
                // Do nothing
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun NotificationQuoteScreenPrev(){
    val q = Quote(
        id = 1,
        quoteId = "1",
        author = "Albert Einstein",
        authorSlug = "albert-einstein",
        content = "Life is like riding a bicycle. To keep your balance, you must keep moving.",
        dateAdded = "2023-01-01",
        dateModified = "2023-01-02",
        length = 55,
        tags = listOf("life", "balance", "inspirational"),
        category = "Science"
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                shape = FloatingActionButtonDefaults.extendedFabShape,
                modifier = Modifier
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) {

    }
}
