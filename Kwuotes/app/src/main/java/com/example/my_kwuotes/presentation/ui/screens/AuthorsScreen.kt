package com.example.my_kwuotes.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwitchLeft
import androidx.compose.material.icons.filled.SwitchRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.kwuotes.R
import com.example.my_kwuotes.data.mappers.toAuthor
import com.example.my_kwuotes.data.models.Author
import com.example.my_kwuotes.data.models.SearchAuthorResult
import com.example.my_kwuotes.data.models.SearchResult
import com.example.my_kwuotes.data.remote.models.QuoteDto
import com.example.my_kwuotes.presentation.ui.components.AuthorListItem
import com.example.my_kwuotes.presentation.ui.components.ErrorItem
import com.example.my_kwuotes.presentation.ui.components.NullItem
import com.example.my_kwuotes.presentation.ui.components.SearchedQuoteByAuthorItem
import com.example.my_kwuotes.presentation.ui.components.SearchedQuoteItem
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AuthorsScreen(
    viewModel: QuotesViewModel,
    navController: NavController
) {

    var isAuthor by remember {
        mutableStateOf(true)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }
    val authors : LazyPagingItems<Author> = viewModel.authors.collectAsLazyPagingItems()
    val searchedAuthors : LazyPagingItems<SearchAuthorResult> = viewModel.searchForAuthors.collectAsLazyPagingItems()
    val searchText by viewModel.searchTextForAuthor.collectAsState("")
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        viewModel.getAuthors()
    }

    Column {

        SearchBar(
            desc = stringResource(R.string.search_for_authors),
            value = searchText,
            onValueChange = { viewModel.updateAuthorSearchText(it) },
            onSearch =  {
                isAuthor = false
                viewModel.getSearchedAuthors(searchText)
            },
            changeFocus = {
                         if (!isAuthor){
                             isAuthor = true
                             scope.launch {
                                 delay(1000)
                                 viewModel.getAuthors()
                                 viewModel.updateAuthorSearchText("")
                                 viewModel.updateAuthorSearchText("")
                             }

                         }
            },
            icon =  if (isAuthor) Icons.Default.Person else Icons.Default.Cancel ,
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)) {
            if (isLoading){
                if (authors.loadState.refresh == LoadState.Loading){
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                if (searchedAuthors.loadState.refresh == LoadState.Loading){
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 62.dp)
            ) {
                item {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(R.string.authors), fontSize = 24.sp)
                    }
                }
                if (isAuthor) {
                    if (authors.itemCount>=1){
                        items(authors) { author ->
                            if (author != null) {
                                isLoading = false
                                AuthorListItem(author) {
                                    navController.navigate(NavHelper.AuthorDetailsScreen.route+"/${author._id}")
                                }
                            } else{
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
                    }else{
                        item{
                            NullItem(
                                modifier = Modifier.fillMaxWidth().height(500.dp)
                                    .align(Alignment.BottomCenter),
                                text = stringResource(id = R.string.no_quotes_found)
                            )
                        }
                    }

                } else {
                    if (authors.itemCount>=1) {
                        items(searchedAuthors) { author ->
                            if (author != null) {
                                AuthorListItem(author.toAuthor()) {
                                    navController.navigate(NavHelper.AuthorDetailsScreen.route + "/${author._id}")
                                }
                            } else {
                                ErrorItem(
                                    errorMessage = stringResource(R.string.no_author_found),
                                    modifier = Modifier.fillMaxSize()
                                ) {

                                }
                            }
                        }
                    } else{
                        item{
                            NullItem(
                                modifier = Modifier.fillMaxWidth().height(500.dp)
                                    .align(Alignment.BottomCenter),
                                text = stringResource(id = R.string.no_author_found)
                            )
                        }
                    }

                }

                when (authors.loadState.append) {
                    is LoadState.Loading -> {
                        item { CircularProgressIndicator() }
                    }

                    is LoadState.Error -> {
                        item { Text(stringResource(R.string.error_loading_more_authors)) }
                    }

                    else -> {
                        // No-op
                    }
                }

                when (searchedAuthors.loadState.append) {
                    is LoadState.Loading -> {
                        item { CircularProgressIndicator() }
                    }

                    is LoadState.Error -> {
                        item { Text(stringResource(R.string.error_loading_more_authors)) }
                    }

                    else -> {
                        // No-op
                    }
                }

            }
        }

    }
}