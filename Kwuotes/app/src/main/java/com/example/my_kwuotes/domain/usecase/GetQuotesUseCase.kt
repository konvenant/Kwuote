package com.example.my_kwuotes.domain.usecase

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.paging.PagingData
import com.example.my_kwuotes.data.local.dao.QuoteDao
import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.data.remote.models.QuoteDto
import com.example.my_kwuotes.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetQuotesUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val quoteDao: QuoteDao
) {
    operator fun invoke(category: String): Flow<PagingData<QuoteEntity>> {
        return quoteRepository.getQuotesByCategory(category)
    }

    private suspend fun saveQuotesToLocal(quotes: List<QuoteEntity>) {
        // Use LocalDataSource to save quotes to the database
        quoteDao.upsertAll(quotes)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =   context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            return when {
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                // Add other transport types as needed (e.g., NetworkCapabilities.TRANSPORT_VPN)
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

}
