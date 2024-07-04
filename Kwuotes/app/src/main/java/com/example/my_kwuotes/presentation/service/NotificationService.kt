package com.example.my_kwuotes.presentation.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.kwuotes.R // Assuming GetQuotesUseCase is in com.example.kwuotes package
import com.example.my_kwuotes.data.models.RandomQuote
import com.example.my_kwuotes.data.remote.models.QuoteDto
import com.example.my_kwuotes.domain.usecase.GetQuotesUseCase
import com.example.my_kwuotes.domain.usecase.GetRandomQuoteUseCase
import com.example.my_kwuotes.presentation.ui.MainActivity
import com.example.my_kwuotes.presentation.utils.Resource
import com.example.my_kwuotes.presentation.viewmodels.QuotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService @Inject constructor(
    private val quotesViewModel: QuotesViewModel,
    private val lifecycleOwner: LifecycleOwner
) : Service() {

    val quote = quotesViewModel.randomQuote
    override fun onCreate() {
        super.onCreate()
        quote.observe(lifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                    val randomQuote = it.data
                    showQuoteNotification(randomQuote!!)
                    stopSelf()
                }

                is Resource.Error -> TODO()
                is Resource.Loading -> TODO()
            }
        })

    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }



    private fun showQuoteNotification(quote: RandomQuote) {
        val notificationIntent = buildQuotePendingIntent(quote)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(quote.content)
            .setContentText("- ${quote.author}")
            .setContentIntent(notificationIntent)
            .setSmallIcon(R.drawable.baseline_menu_book_24)
            .setAutoCancel(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun buildQuotePendingIntent(quote: RandomQuote): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
            .apply {
                putExtra(QUOTE_TEXT, quote.content)
                putExtra(QUOTE_AUTHOR, quote.author)
                // Add category or other quote details if needed
            }
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object {
        private const val CHANNEL_ID = "quote_notification_channel"
        private const val NOTIFICATION_ID = 1
        private const val QUOTE_TEXT = "quote_text"
        private const val QUOTE_AUTHOR = "quote_author"
    }
}
