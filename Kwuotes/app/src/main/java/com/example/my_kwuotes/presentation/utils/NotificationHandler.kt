package com.example.my_kwuotes.presentation.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.kwuotes.R
import com.example.my_kwuotes.data.models.Quote
import com.example.my_kwuotes.presentation.ui.MainActivity
import kotlin.random.Random

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification_channel_id"


    // SIMPLE NOTIFICATION
    fun showSimpleNotification(quote: Quote) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("quote_id", quote.quoteId)
            putExtra("category", quote.category)
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle(quote.author)
            .setContentText(quote.content)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.baseline_menu_book_24)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()  // finalizes the creation

        notificationManager.notify(Random.nextInt(), notification)
    }
}