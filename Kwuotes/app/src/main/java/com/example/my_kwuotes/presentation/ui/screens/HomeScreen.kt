package com.example.my_kwuotes.presentation.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Assistant
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kwuotes.R
import com.example.my_kwuotes.presentation.ui.graph.BottomNavGraph
import com.example.my_kwuotes.presentation.utils.BottomNavigationItem
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import com.example.my_kwuotes.presentation.viewmodels.SharedUserViewModel
import com.example.my_kwuotes.ui.theme.Blue
import com.example.my_kwuotes.ui.theme.Blue02
import com.example.my_kwuotes.ui.theme.KwuotesTheme
import com.example.my_kwuotes.ui.theme.Red0

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
   navController: NavController,
   quotesViewModel: QuotesViewModel
) {
    val viewmodel : SharedUserViewModel = viewModel()

    val bottomNavHostController = rememberNavController()

    Scaffold (
      modifier = Modifier
          .background(MaterialTheme.colorScheme.surface)
          .fillMaxSize(),
        bottomBar = {
            BottomNavBar(bottomNavHostController)
        }
    ) {

      BottomNavGraph(
          mainNavController = navController,
          userViewModel = viewmodel,
          quotesViewModel = quotesViewModel,
          bottomNavHostController
      )


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(
    navController: NavController
){
    val items = listOf(
        BottomNavigationItem(
            title = stringResource(R.string.home),
            selectedIcon = Icons.Filled.FormatQuote,
            unSelectedIcon = Icons.Outlined.FormatQuote,
            hasNew = false,
            route = NavHelper.QuotesScreen.route
        ),
        BottomNavigationItem(
            title = stringResource(R.string.search),
            selectedIcon = Icons.Filled.Search,
            unSelectedIcon = Icons.Outlined.Search,
            hasNew = false,
            route = NavHelper.SearchScreen.route
        ),
        BottomNavigationItem(
            title = stringResource(R.string.my_quotes),
            selectedIcon = Icons.Filled.Assistant,
            unSelectedIcon = Icons.Outlined.Assistant,
            hasNew = false,
            route = NavHelper.UserQuoteScreen.route
        ),
        BottomNavigationItem(
            title = stringResource(R.string.favorites),
            selectedIcon = Icons.Filled.Favorite,
            unSelectedIcon = Icons.Outlined.FavoriteBorder,
            hasNew = false,
            route = NavHelper.FavoriteQuote.route
        ),
        BottomNavigationItem(
            title = stringResource(R.string.user),
            selectedIcon = Icons.Filled.Person,
            unSelectedIcon = Icons.Outlined.PersonOutline,
            hasNew = false,
            route = NavHelper.UserMoreScreen.route
        )
    )


    var selectedItemIndex by remember {
        mutableIntStateOf(0)
    }

    NavigationBar (
        modifier = Modifier
            .fillMaxWidth()
        ,
        containerColor =  MaterialTheme.colorScheme.surface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEachIndexed { index, bottomNavigationItem ->
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any { it.route == bottomNavigationItem.route } == true,
        onClick = {
            navController.navigate(bottomNavigationItem.route){
                popUpTo(navController.graph.findStartDestination().id){
                    saveState = true
                }
                launchSingleTop = true
                selectedItemIndex = index
                restoreState = true
            }
        },
        icon = {
            BadgedBox(badge = {
                if (bottomNavigationItem.badgeCount != null){
                    Badge{
                        Text(text = bottomNavigationItem.badgeCount.toString())
                    }
                } else if(bottomNavigationItem.hasNew){
                    Badge()
                }
            }) {
                Icon(
                    imageVector = if (index == selectedItemIndex) {
                        bottomNavigationItem.selectedIcon
                    } else bottomNavigationItem.unSelectedIcon,
                    contentDescription = bottomNavigationItem.title
                )
            }
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.White,
            selectedIconColor = Blue,
            selectedTextColor = Blue,
            unselectedTextColor = Red0,
            unselectedIconColor = Red0
        )
    )
}
}
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KwuotesTheme {
        val navController = rememberNavController()
        BottomNavBar(navController)
    }
}