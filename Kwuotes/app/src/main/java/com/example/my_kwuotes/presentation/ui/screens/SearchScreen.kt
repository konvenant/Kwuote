package com.example.my_kwuotes.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwitchLeft
import androidx.compose.material.icons.filled.SwitchRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.kwuotes.R
import com.example.my_kwuotes.data.models.SearchResult
import com.example.my_kwuotes.data.remote.models.QuoteDto
import com.example.my_kwuotes.presentation.ui.components.NullItem
import com.example.my_kwuotes.presentation.ui.components.SearchedQuoteByAuthorItem
import com.example.my_kwuotes.presentation.ui.components.SearchedQuoteItem
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import com.example.my_kwuotes.ui.theme.Blue
import com.example.my_kwuotes.ui.theme.Blue02
import com.example.my_kwuotes.ui.theme.Brown01
import com.example.my_kwuotes.ui.theme.Cream01


@Composable
fun SearchScreen(
    viewModel: QuotesViewModel,
    navController: NavController
) {
    var isQuote by remember {
        mutableStateOf(true)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }
    val searchedQuotes : LazyPagingItems<SearchResult> = viewModel.searchedQuotes.collectAsLazyPagingItems()
    val searchedQuotesByAuthors : LazyPagingItems<QuoteDto> = viewModel.searchedQuotesByAuthor.collectAsLazyPagingItems()
    val searchText by viewModel.searchText.collectAsState("")

    Column {
        SearchBar(
            desc = if (isQuote) stringResource(R.string.search_for_quotes) else stringResource(R.string.search_quotes_by_authors),
            value = searchText,
            onValueChange = { viewModel.updateSearchText(it) },
            onSearch = { if (isQuote){
                viewModel.searchQuotes(searchText)
            } else{
                viewModel.getQuotesByAuthor(searchText)
            }
                isLoading  = true
                       },
            changeFocus = { isQuote = !isQuote },
            icon = if (isQuote) Icons.Default.SwitchLeft else Icons.Default.SwitchRight,
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)) {
            if (isLoading){
                if (searchedQuotes.loadState.refresh == LoadState.Loading){
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                if (searchedQuotesByAuthors.loadState.refresh == LoadState.Loading){
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 62.dp)
                ) {
                    if (isQuote) {
                        if(searchedQuotes.itemCount > 1){
                            items(searchedQuotes) { quote ->
                                if (quote != null) {
                                    isLoading = false
                                    SearchedQuoteItem(quote){q ->
                                        navController.navigate(NavHelper.QuoteScreen.route+"/${q._id}"+"/${q.tags[0]}")
                                    }
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

                    } else {
                        if (searchedQuotesByAuthors.itemCount>1){
                            items(searchedQuotesByAuthors) { quote ->
                                if (quote != null) {
                                    SearchedQuoteByAuthorItem(quote = quote){ q  ->
                                        navController.navigate(NavHelper.QuoteScreen.route+"/${q._id}"+"/${q.tags[0]}")
                                    }
                                }
                            }
                        }
                    }

                    when (searchedQuotes.loadState.append) {
                        is LoadState.Loading -> {
                            item { CircularProgressIndicator() }
                        }

                        is LoadState.Error -> {
                            item { Text(stringResource(R.string.error_loading_more_quotes)) }
                        }

                        else -> {
                            // No-op
                        }
                    }

                    when (searchedQuotesByAuthors.loadState.append) {
                        is LoadState.Loading -> {
                            item { CircularProgressIndicator() }
                        }

                        is LoadState.Error -> {
                            item { Text(stringResource(R.string.error_loading_more_quotes)) }
                        }

                        else -> {
                            // No-op
                        }
                    }

            }
        }

    }
}

@Composable
fun SearchBar(
    desc: String,
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit = {},
    changeFocus: () -> Unit ={},
    icon: ImageVector
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("$desc...") },
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        },
        leadingIcon = {
            IconButton(onClick = changeFocus) {
                Icon(icon, contentDescription = "Change")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Blue,
            focusedPlaceholderColor = Blue,
            unfocusedPlaceholderColor = Blue02,
            focusedTextColor = Brown01
        ),
        singleLine = true

    )
}


@Composable
fun AuthorSearchBar(
    desc: String,
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit = {},
    changeFocus: () -> Unit ={},
    icon: ImageVector
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("$desc...") },
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search1))
            }
        },
        leadingIcon = {
            IconButton(onClick = changeFocus) {
                Icon(icon, contentDescription = "Change")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Blue,
            focusedPlaceholderColor = Blue,
            unfocusedPlaceholderColor = Blue02,
            focusedTextColor = Brown01
        ),
        singleLine = true

    )
}