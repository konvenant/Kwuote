package com.example.my_kwuotes.presentation.ui.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.my_kwuotes.presentation.ui.screens.AuthorDetailScreen
import com.example.my_kwuotes.presentation.ui.screens.AuthorFullDetailScreen
import com.example.my_kwuotes.presentation.ui.screens.AuthorsScreen
import com.example.my_kwuotes.presentation.ui.screens.CategoriesScreen
import com.example.my_kwuotes.presentation.ui.screens.HomeScreen
import com.example.my_kwuotes.presentation.ui.screens.NotificationQuoteScreen
import com.example.my_kwuotes.presentation.ui.screens.OnboardingScreen
import com.example.my_kwuotes.presentation.ui.screens.QuoteScreen
import com.example.my_kwuotes.presentation.ui.screens.RegisterScreen
import com.example.my_kwuotes.presentation.ui.screens.ShareQuoteScreen
import com.example.my_kwuotes.presentation.ui.screens.SplashScreen
import com.example.my_kwuotes.presentation.ui.screens.UserCategoriesScreen
import com.example.my_kwuotes.presentation.utils.NavHelper
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import com.example.my_kwuotes.presentation.viewmodels.SharedUserViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainGraph(quoteId: String?, category: String?,quoteViewModel: QuotesViewModel){
    val navController = rememberNavController()

    val userViewModel: QuotesViewModel = hiltViewModel()

    val sharedViewModel: SharedUserViewModel = viewModel()



    NavHost(
        navController = navController,
        startDestination = if(quoteId != null) NavHelper.NotificationQuotesScreen.route else NavHelper.SplashScreen.route
    ){
       composable(NavHelper.SplashScreen.route){
            SplashScreen(navController,quoteViewModel )
       }

        composable(NavHelper.OnboardingScreen.route){
            OnboardingScreen(navController )
        }

        composable(NavHelper.CategoriesScreen.route){
          CategoriesScreen(navController, quoteViewModel)
        }

        composable(NavHelper.UserCategoriesScreen.route){
            UserCategoriesScreen(navController, quoteViewModel)
        }

        composable(NavHelper.RegisterUser.route){
            RegisterScreen(navController)
        }

        composable(NavHelper.HomeScreen.route){
         HomeScreen(navController,quoteViewModel)
        }

        composable(NavHelper.QuoteScreen.route+"/{id}"+"/{category}"){backEntry ->
            val quoteId2 = backEntry.arguments?.getString("id") ?: ""
            val category2 = backEntry.arguments?.getString("category") ?: ""
            QuoteScreen(navController, quoteId2  , quoteViewModel,category2 )
        }

        composable(NavHelper.NotificationQuotesScreen.route){
                NotificationQuoteScreen(navController, quoteId!!  , quoteViewModel,category!! )
        }

        composable(NavHelper.AuthorsScreen.route){
            AuthorsScreen(quoteViewModel, navController )
        }

        composable(NavHelper.AuthorDetailsScreen.route+"/{id}"){backEntry ->
            val authorId = backEntry.arguments?.getString("id") ?: ""
            AuthorDetailScreen(authorId,quoteViewModel,navController)
        }

        composable(NavHelper.AuthorFullDetailsScreen.route+"/{slug}"){backEntry ->
            val slug = backEntry.arguments?.getString("slug") ?: ""
            AuthorFullDetailScreen(slug ,quoteViewModel, navController )
        }

        composable(NavHelper.ShareQuoteScreen.route+"/{content}"+"/{author}"+"/{category}"){backEntry ->
            val content = backEntry.arguments?.getString("content") ?: ""
            val author = backEntry.arguments?.getString("author") ?: ""
            val tag = backEntry.arguments?.getString("category") ?: ""
            ShareQuoteScreen(navController, tag , content , author )
        }
    }
}