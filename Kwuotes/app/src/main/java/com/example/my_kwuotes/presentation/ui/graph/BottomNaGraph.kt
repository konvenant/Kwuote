package com.example.my_kwuotes.presentation.ui.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.my_kwuotes.presentation.ui.screens.FavoritesScreen
import com.example.my_kwuotes.presentation.ui.screens.MyQuotesScreen
import com.example.my_kwuotes.presentation.ui.screens.QuoteHomeScreen
import com.example.my_kwuotes.presentation.ui.screens.SearchScreen
import com.example.my_kwuotes.presentation.ui.screens.UserMoreScreen
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import com.example.my_kwuotes.presentation.viewmodels.SharedUserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavGraph(
    mainNavController: NavController,
    userViewModel: SharedUserViewModel,
    quotesViewModel: QuotesViewModel,
    bottomNavController: NavHostController
){


    val userViewModel: QuotesViewModel = hiltViewModel()

    val sharedViewModel: SharedUserViewModel = viewModel()

    NavHost(
        navController = bottomNavController,
        startDestination = NavHelper.QuotesScreen.route
    ){
       composable(NavHelper.QuotesScreen.route){
             QuoteHomeScreen(quotesViewModel , mainNavController )
       }

        composable(NavHelper.SearchScreen.route){
              SearchScreen(quotesViewModel,mainNavController)
        }

        composable(NavHelper.UserQuoteScreen.route){
                  MyQuotesScreen(mainNavController,quotesViewModel)
        }

        composable(NavHelper.FavoriteQuote.route){
               FavoritesScreen(mainNavController,quotesViewModel)
        }

        composable(NavHelper.UserMoreScreen.route){
            UserMoreScreen(bottomNavController,mainNavController,quotesViewModel)
        }
    }
}