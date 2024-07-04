package com.example.my_kwuotes.presentation.workers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.remember
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.Observer
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.my_kwuotes.data.mappers.toQuote
import com.example.my_kwuotes.data.models.Quote
import com.example.my_kwuotes.domain.repository.QuoteRepository
import com.example.my_kwuotes.domain.usecase.GetRandomQuoteUseCase
import com.example.my_kwuotes.presentation.utils.NotificationHandler
import com.example.my_kwuotes.presentation.utils.Resource
import com.example.my_kwuotes.presentation.utils.UserPrefManager
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@HiltWorker
class DailyQuoteWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val getRandomQuoteUseCase: GetRandomQuoteUseCase,
    private val quoteRepository: QuoteRepository,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val sharedPreferences = appContext.getSharedPreferences("QuoteAppPreferences", Context.MODE_PRIVATE)
        val lastOpenDate = sharedPreferences.getString("lastOpenDate", null)
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val userPrefManager = UserPrefManager(applicationContext)
        val notificationHandler = NotificationHandler(applicationContext)
        val userSetting  = userPrefManager.getUserSettings()
        Log.e("Worker_LOG","$lastOpenDate $currentDate")
        return@withContext if (true) {
            try {
                sharedPreferences.edit().putString("lastOpenDate", currentDate).apply()
                if (hasInternetConnection(appContext)) {
                    val response = getRandomQuoteUseCase.invoke()
                    if (response.isSuccessful) {
                        response.body()?.let {
                          if (userSetting.notificationsEnabled){
                              notificationHandler.showSimpleNotification(it.toQuote())
                          }
                        }
                    } else {
                        val randomQuoteFromDb = quoteRepository.getRandomQuoteFromDb()
                        randomQuoteFromDb?.let {
                            if (userSetting.notificationsEnabled){
                                notificationHandler.showSimpleNotification(it.toQuote())
                            }
                        }
                    }
                } else {
                    val randomQuoteFromDb = quoteRepository.getRandomQuoteFromDb()
                    randomQuoteFromDb?.let {
                        if (userSetting.notificationsEnabled){
                            notificationHandler.showSimpleNotification(it.toQuote())
                        }
                    }
                }
                Result.success()
            } catch (e: Exception) {
                e.message?.let { Log.e("EXCEPTION_FROM_WORKER", it) }
                Result.failure()
            }

        } else {
            Result.failure()
        }
    }


    private fun showNotification(){

    }

   private  fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
       val network = connectivityManager.activeNetwork ?: return false
       val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
       return when {
           activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
           activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
           activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
           else -> false
       }
   }


}


