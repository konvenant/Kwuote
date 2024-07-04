package com.example.my_kwuotes.presentation.ui

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.my_kwuotes.presentation.ui.graph.MainGraph
import com.example.my_kwuotes.presentation.utils.UserPrefManager
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import com.example.my_kwuotes.presentation.workers.DailyQuoteWorker
import com.example.my_kwuotes.ui.theme.KwuotesTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {


        val userPrefManager = UserPrefManager(this)

    if (userPrefManager.getHasSavedCategories()){
        scheduleDailyQuoteWorker(this)
    }
        super.onCreate(savedInstanceState)
        val quoteId = intent.getStringExtra("quote_id")
        val category = intent.getStringExtra("category")
        setContent {
            val quoteViewModel : QuotesViewModel = hiltViewModel()
            val isDarkTheme = quoteViewModel.isDarkTheme.collectAsState(initial = false).value
            KwuotesTheme (
                darkTheme = isDarkTheme
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainGraph(quoteId, category,quoteViewModel)
                }
            }
        }
    }
}



private fun scheduleDailyQuoteWorker(context: Context) {
    val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyQuoteWorker>(2, TimeUnit.HOURS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "DailyQuoteWork",
        ExistingPeriodicWorkPolicy.UPDATE,
        dailyWorkRequest
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KwuotesTheme {
        Greeting("Android")
    }
}
