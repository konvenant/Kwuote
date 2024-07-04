package com.example.my_kwuotes.presentation.utils

sealed class NavHelper (var route: String) {
    object SplashScreen: NavHelper("splash_screen")
    object OnboardingScreen: NavHelper("onboardingScreen")
    object WelcomeScreen: NavHelper("welcomeScreen")
    object HomeScreen: NavHelper("homeScreen")
    object CategoriesScreen: NavHelper("categoriesScreen")

    object UserCategoriesScreen: NavHelper("userCategoriesScreen")
    object QuotesScreen: NavHelper("quotesScreen")
    object NotificationQuotesScreen: NavHelper("singleNotificationQuotesScreen")
    object QuoteScreen: NavHelper("singleSuoteScreen")
    object SettingScreen: NavHelper("settingScreen")
    object UserQuoteScreen: NavHelper("userQuoteScreen")
    object ShareQuoteScreen: NavHelper("shareQuoteScreen")
    object SearchScreen: NavHelper("searchScreen")
    object FavoriteQuote: NavHelper("favoriteQuoteScreen")
    object ProfileScreen: NavHelper("profileScreen")
    object UserMoreScreen: NavHelper("userMoreScreen")
    object RegisterUser: NavHelper("registerUserScreen")
    object AuthorsScreen: NavHelper("authorsScreen")
    object AuthorDetailsScreen: NavHelper("authorDetailsScreen")
    object AuthorFullDetailsScreen: NavHelper("authorFullDetailsScreen")

}