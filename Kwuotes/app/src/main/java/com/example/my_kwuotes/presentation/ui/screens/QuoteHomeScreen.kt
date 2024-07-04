package com.example.my_kwuotes.presentation.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import com.example.kwuotes.R
import com.example.my_kwuotes.data.local.models.TagsItemEntity
import com.example.my_kwuotes.data.models.Quote
import com.example.my_kwuotes.presentation.ui.components.CategoryItem
import com.example.my_kwuotes.presentation.ui.components.NullItem
import com.example.my_kwuotes.presentation.ui.components.QuoteItem
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.utils.Resource
import com.example.my_kwuotes.presentation.utils.generateDummyTagsItems
import com.example.my_kwuotes.presentation.utils.getDummyCategoryItem
import com.example.my_kwuotes.presentation.utils.getDummyQuotes
import com.example.my_kwuotes.presentation.utils.shimmerEffect
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import com.example.my_kwuotes.ui.theme.Blue
import com.example.my_kwuotes.ui.theme.Blue02
import com.example.my_kwuotes.ui.theme.Brown01
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuoteHomeScreen(
    quotesViewModel: QuotesViewModel,
    navController: NavController,
) {


    val categoriesData by quotesViewModel.userTags.observeAsState()

    var category: String = "Knowledge"
    var categories: List<TagsItemEntity> = generateDummyTagsItems()

    var quotes : LazyPagingItems<Quote> = quotesViewModel.pagingFlowFromCategory.collectAsLazyPagingItems()
    Log.e("quotessss","${quotes.itemSnapshotList.items.size}")
    var isLoading by remember{
        mutableStateOf(true)
    }


    var isNull by remember{
        mutableStateOf(false)
    }
    LaunchedEffect (true) {
        quotesViewModel.getUserTagsCategory()
        delay(3000)
      isLoading = false
    }



    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    val font = FontFamily(
        Font(R.font.wellfleet_regular, FontWeight.Bold)
    )


    val font2 = FontFamily(
        Font(R.font.adventpro_variable, FontWeight.Bold)
    )
    var isSelected by remember{
        mutableIntStateOf(0)
    }

    var state by remember {
        mutableStateOf(true)
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = quotes.loadState) {
        if (quotes.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                context.getString(R.string.error_no_internet_connection),
//                "Error: " + (quotes.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }



   Column (
       modifier = Modifier
           .fillMaxSize()
           .background(MaterialTheme.colorScheme.surface)
           .padding(8.dp)
   ) {
      Row (
          modifier = Modifier
              .fillMaxWidth()
              .padding(8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
      ) {
          Text(
              text = stringResource(R.string.explore),
              style = TextStyle.Default.copy(
                  fontSize = 32.sp,
                  color = MaterialTheme.colorScheme.secondary,
                  fontFamily = font
              )
          )

          IconButton(onClick = { navController.navigate(NavHelper.AuthorsScreen.route) }) {
              Icon(
                  imageVector = Icons.Default.SupervisedUserCircle,
                  contentDescription = null
              )
          }
      }
       Spacer(modifier = Modifier.height(8.dp))
       Text(
           text = stringResource(R.string.awesome_quotes_organised_for_you_in_your_own_categories),
           style = TextStyle.Default.copy(
               fontSize = 14.sp,
               color = MaterialTheme.colorScheme.secondary,
               fontFamily = font2
           )
       )
       Spacer(modifier = Modifier.height(8.dp))

       when(categoriesData){
           is Resource.Error -> {
               val err = (categoriesData as Resource.Error<List<TagsItemEntity>>).message
               Toast.makeText(context,
                   stringResource(R.string.error_getting_categories),Toast.LENGTH_LONG).show()
           }
           is Resource.Loading -> {
               Row(
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(42.dp)
                       .shimmerEffect()
               ) {

               }
           }
           is Resource.Success -> {
               val tags = (categoriesData as Resource.Success<List<TagsItemEntity>>).data
               Log.d("ALL-TAGS","${tags?.size}")

               LazyRow (
                   modifier = Modifier.fillMaxWidth()
               ) {

                  tags?.let { userTags->
                      if (state){
                          quotesViewModel.setCategory(userTags[0].name)
                          isSelected = 0
                          state = false
                      }
                      itemsIndexed(userTags){index ,tag ->
                          CategoryItem(
                              name = tag.name,
                              count = tag.quoteCount,
                              isSelected = isSelected == index
                          ) {
                              isSelected = index
                              quotesViewModel.setCategory(it)
                              quotes.refresh()
                              scope.launch {
                                  lazyListState.scrollToItem(0)
                              }
                          }
                      }
                  }
               }
           }
           null -> {
               Row(
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(42.dp)
                       .shimmerEffect()
               ) {

               }
           }
       }

       Spacer(modifier = Modifier.height(8.dp))

       Box(modifier = Modifier.fillMaxSize()) {
           if (quotes.loadState.refresh is LoadState.Loading) {
               CircularProgressIndicator(
                   modifier = Modifier.align(Alignment.Center)
               )
           } else {
               if(quotes.loadState.refresh is LoadState.Error){
                   NullItem(modifier = Modifier.fillMaxSize(), stringResource(id = R.string.error_loading_more_quotes))
               } else{
                   if(quotes.itemSnapshotList.isNotEmpty()){
                       LazyColumn (
                           state = lazyListState,
                           contentPadding = PaddingValues(bottom = 62.dp)
                       ) {
                           items(quotes, key = {it.id}){  quote ->
                               if (quote != null) {
                                   QuoteItem(quote,lazyListState){ q ->
                                       navController.navigate(NavHelper.QuoteScreen.route+"/${q.quoteId}"+"/${q.category}")
                                   }
                               }
                           }

                           item {
                               if (quotes.loadState.append is LoadState.Loading) {
                                   Box(modifier = Modifier.fillMaxWidth()){
                                       CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                   }
                               }
                           }
                       }
                   } else{
                       Box(
                           modifier = Modifier
                               .fillMaxSize()
                               .clickable { },
                           contentAlignment = Alignment.Center
                       ){
                           NullItem(modifier = Modifier.fillMaxSize(),text = stringResource(R.string.no_quote_found_for_this_category))
                       }

                   }
               }

           }
       }
   }
}

@SuppressLint("SuspiciousIndentation")
@Preview
@Composable
fun HomePreview(){
    val dummyQuotes = getDummyQuotes()
    val font = FontFamily(
        Font(R.font.wellfleet_regular, FontWeight.Bold)
    )

    val font2 = FontFamily(
        Font(R.font.adventpro_variable, FontWeight.Bold)
    )
    val dummyCategoryItem = getDummyCategoryItem()
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(8.dp)
    ) {
        Text(
            text = "Explore",
            style = TextStyle.Default.copy(
                fontSize = 32.sp,
                color = Blue,
                fontFamily = font
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Awesome quotes organised for you in your own categories",
            style = TextStyle.Default.copy(
                fontSize = 14.sp,
                color = Blue02,
                fontFamily = font2
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
          items(getDummyCategoryItem()){ category ->
              CategoryItem(
                  name = category.name,
                  count = category.count,
                  isSelected = category.isSelected
              ) {

              }

          }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 2),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(dummyQuotes) { quote ->
//                QuoteItem(quote)
            }
        }
    }
}