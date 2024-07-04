package com.example.my_kwuotes.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.my_kwuotes.data.local.dao.QuoteDao
import com.example.my_kwuotes.data.local.dao.QuoteDao_Impl
import com.example.my_kwuotes.data.local.database.FavoriteQuotesDatabase
import com.example.my_kwuotes.data.local.database.QuotesDatabase
import com.example.my_kwuotes.data.local.database.TagsDatabase
import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.data.remote.api.QuoteApi
import com.example.my_kwuotes.data.remote.mediators.QuoteRemoteMediator
import com.example.my_kwuotes.data.repositories.local.FavQuoteRepositoryImpl
import com.example.my_kwuotes.data.repositories.local.TagRepositoryImpl
import com.example.my_kwuotes.data.repositories.remote.QuoteRepositoryImpl
import com.example.my_kwuotes.di.Utils.BASE_URL
import com.example.my_kwuotes.domain.repository.FavoriteQuoteRepository
import com.example.my_kwuotes.domain.repository.QuoteRepository
import com.example.my_kwuotes.domain.repository.TagRepository
import com.example.my_kwuotes.domain.usecase.GetQuotesUseCase
import com.example.my_kwuotes.domain.usecase.GetRandomQuoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideQuotesDatabase(@ApplicationContext context: Context): QuotesDatabase {
        return Room.databaseBuilder(
            context,
            QuotesDatabase::class.java,
            "quote.db"
        ).build()
    }


    @Provides
    @Singleton
    fun provideQuoteApi(): QuoteApi {
        val retrofit by lazy{
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        return retrofit.create(QuoteApi::class.java)
    }


    @Provides
    @Singleton
    fun provideFavoriteQuotesDatabase(@ApplicationContext context: Context): FavoriteQuotesDatabase {
        return Room.databaseBuilder(
            context,
            FavoriteQuotesDatabase::class.java,
            "favquote.db"
        ).build()
    }

    @Provides
    fun provideQuoteRepository(
        apiService: QuoteApi,
        database: QuotesDatabase,
        @ApplicationContext context: Context
    ): QuoteRepository {
        return QuoteRepositoryImpl(apiService,database,context)
    }

    @Provides
    fun provideFavQuoteRepository(
        database: FavoriteQuotesDatabase
    ): FavoriteQuoteRepository {
        return FavQuoteRepositoryImpl(database)
    }


    @Provides
    @Singleton
    fun provideTagsDatabase(@ApplicationContext context: Context): TagsDatabase {
        return Room.databaseBuilder(
            context,
            TagsDatabase::class.java,
            "tag.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTagRepository (database: TagsDatabase): TagRepository {
        return TagRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideGetQuoteUseCase(
        quoteDao: QuoteDao,
        quoteRepository: QuoteRepository,
        @ApplicationContext context: Context
    ) : GetQuotesUseCase {
        return GetQuotesUseCase(quoteRepository, quoteDao)
    }

    @Provides
    @Singleton
    fun provideGetRandomQuoteUseCase(
        quoteRepository: QuoteRepository
    ) : GetRandomQuoteUseCase {
        return GetRandomQuoteUseCase(quoteRepository)
    }

    @Provides
    @Singleton
    fun provideGetQuotesByCategoriesUseCase(
        database: QuotesDatabase
    ) : QuoteDao {
        return QuoteDao_Impl(database)
    }


    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideQuotePager(
        apiService: QuoteApi,
        database: QuotesDatabase
    ) : Pager<Int, QuoteEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = QuoteRemoteMediator(database,apiService,"wisdom"),
            pagingSourceFactory = {
                database.quoteDao.getQuotesByCategory("wisdom")
            }
        )
    }

    @Singleton
    @Provides
    fun provideContext(
        @ApplicationContext context: Context
    ) = context


}