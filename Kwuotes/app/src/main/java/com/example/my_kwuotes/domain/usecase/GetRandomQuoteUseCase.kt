package com.example.my_kwuotes.domain.usecase

import androidx.paging.PagingData
import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.data.models.RandomQuote
import com.example.my_kwuotes.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class GetRandomQuoteUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    suspend operator fun invoke(): Response<RandomQuote> {
        return quoteRepository.getRandomQuote()
    }
}