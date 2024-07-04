package com.example.my_kwuotes.presentation.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.kwuotes.R
import com.example.my_kwuotes.BaseApplication
import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.data.local.models.TagsItemEntity
import com.example.my_kwuotes.data.models.Author
import com.example.my_kwuotes.data.models.Quote
import com.example.my_kwuotes.data.models.RandomQuote
import com.example.my_kwuotes.data.models.RandomQuotes
import com.example.my_kwuotes.data.models.SearchAuthorResult
import com.example.my_kwuotes.data.models.Tags
import com.example.my_kwuotes.data.models.SearchResult
import com.example.my_kwuotes.data.remote.models.QuoteDto
import com.example.my_kwuotes.data.mappers.toQuote
import com.example.my_kwuotes.data.mappers.toRandomQuote
import com.example.my_kwuotes.data.models.AuthorById
import com.example.my_kwuotes.data.models.SearchAuthorResponse
import com.example.my_kwuotes.domain.repository.FavoriteQuoteRepository
import com.example.my_kwuotes.domain.repository.QuoteRepository
import com.example.my_kwuotes.domain.repository.TagRepository
import com.example.my_kwuotes.domain.usecase.GetQuotesUseCase
import com.example.my_kwuotes.domain.usecase.GetRandomQuoteUseCase
import com.example.my_kwuotes.presentation.utils.Resource
import com.example.my_kwuotes.presentation.utils.UserPrefManager
import com.example.my_kwuotes.presentation.utils.getError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class QuotesViewModel @Inject
constructor(
    private val getQuotesByCategoryUseCase: GetQuotesUseCase,
    private val getRandomQuoteUseCase: GetRandomQuoteUseCase,
    private val quoteRepository: QuoteRepository,
    private val tagRepository: TagRepository,
    private val favoriteQuoteRepository: FavoriteQuoteRepository,
    private val context: Context,
    app: Application,
) : AndroidViewModel(app) {

    val randomQuote : MutableLiveData<Resource<RandomQuote>> = MutableLiveData()
    val tagList : MutableLiveData<Resource<Tags>> = MutableLiveData()
    val quoteById : MutableLiveData<Quote> = MutableLiveData()
    val randomQuotes : MutableLiveData<Resource<RandomQuotes>> = MutableLiveData()
    val addMyQuoteResponse : MutableLiveData<Resource<String>> = MutableLiveData()
    val deleteMyQuoteResponse : MutableLiveData<Resource<String>> = MutableLiveData()
    val userTags : MutableLiveData<Resource<List<TagsItemEntity>>> = MutableLiveData()
    val addUserTagsResponse : MutableLiveData<Resource<String>> = MutableLiveData()
    private val _category = MutableStateFlow("wisdom")
    val category: StateFlow<String> = _category
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _searchedQuotes: MutableStateFlow<PagingData<SearchResult>> = MutableStateFlow(value = PagingData.empty())
    val searchedQuotes: MutableStateFlow<PagingData<SearchResult>> get() = _searchedQuotes

    private val _searchedQuotesByAuthor: MutableStateFlow<PagingData<QuoteDto>> = MutableStateFlow(value = PagingData.empty())
    val searchedQuotesByAuthor: MutableStateFlow<PagingData<QuoteDto>> get() = _searchedQuotesByAuthor

    val addFavQuoteResponse : MutableLiveData<Resource<String>> = MutableLiveData()

    val deleteFavQuoteResponse : MutableLiveData<Resource<String>> = MutableLiveData()

    val deleteFavQuoteByIdResponse : MutableLiveData<Resource<String>> = MutableLiveData()

    val isFavorite : MutableLiveData<Boolean> = MutableLiveData()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()



    private val _searchTextForAuthor = MutableStateFlow("")
    val searchTextForAuthor: StateFlow<String> = _searchTextForAuthor

    private val _authors: MutableStateFlow<PagingData<Author>> = MutableStateFlow(value = PagingData.empty())
    val authors: MutableStateFlow<PagingData<Author>> get() = _authors


    private val _searchForAuthors: MutableStateFlow<PagingData<SearchAuthorResult>> = MutableStateFlow(value = PagingData.empty())
    val searchForAuthors: MutableStateFlow<PagingData<SearchAuthorResult>> get() = _searchForAuthors

    val authorById : MutableLiveData<Resource<AuthorById>> = MutableLiveData()

    val authorBySlug : MutableLiveData<Resource<SearchAuthorResponse>> = MutableLiveData()


    private val _randomQuote = MutableStateFlow<Resource<RandomQuote>>(Resource.Loading())
    val workerQuote: StateFlow<Resource<RandomQuote>> = _randomQuote.asStateFlow()
    init {
        val darkTheme = UserPrefManager(context).getUserSettings().isDarkTheme
        _isDarkTheme.value = darkTheme
    }


    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            val darkTheme = UserPrefManager(context).getUserSettings()
            UserPrefManager(context).saveUserSettingsPreferences(darkTheme.copy(isDarkTheme = isDark))
            _isDarkTheme.value = isDark
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingFlowFromCategory = _category.flatMapLatest { category ->
        getQuotesByCategoryUseCase.invoke(category)
            .map { pagingData ->
                pagingData.map {
                    Log.e("CALLED LOCATION:", "Called from viewmodel ${it.toQuote()}")
                    it.toQuote()
                }
            }
            .cachedIn(viewModelScope)
    }

    val myQuotesFlow = quoteRepository.getMyQuotes().flow.map { pagingData ->
        pagingData.map {
            Log.e("My Quotes:", context.getString(R.string.called_from_viewmodel, it.toQuote()))
            it.toQuote()
        }
    }.cachedIn(viewModelScope)

    val favQuotesFlow = favoriteQuoteRepository.getFavQuotes().flow.map { pagingData ->
        pagingData.map {
            it.toQuote()
        }
    }.cachedIn(viewModelScope)

    fun setCategory(category: String) {
        _category.value = category
    }

    fun getAllPagingSource() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val qq = quoteRepository.getAllQuotesAsList()
//                getQuotesByCategoryUseCase.invoke("wisdom")
                Log.e("QQTAG", "getAllQuotes: ${qq}")
            }
        }
    }

     fun getRandomQuote()  {
        randomQuote.postValue(Resource.Loading())
        try {
           if (hasInternetConnection()){
               viewModelScope.launch {
                   val response = getRandomQuoteUseCase.invoke()
                   if (response.isSuccessful){
                       response.body()?.let {
                           randomQuote.postValue(Resource.Success(it))
                       }
                   } else{
                       randomQuote.postValue(Resource.Error(getError(response.errorBody()?.string())))
                   }
               }
           } else {
               viewModelScope.launch {
                   val randomQuoteFromBd = quoteRepository.getRandomQuoteFromDb()
                   randomQuoteFromBd?.let {
                       randomQuote.postValue(Resource.Success(it.toRandomQuote()))
                   }
               }
           }

        } catch (e: Exception) {
            randomQuote.postValue(Resource.Error(e.message!!))
        }

    }

    fun getRandomQuoteForWorker() {
        viewModelScope.launch {
            try {
             if (hasInternetConnection()){
                 val response = getRandomQuoteUseCase.invoke()
                 if (response.isSuccessful){
                     response.body()?.let {
                         _randomQuote.emit(Resource.Success(it))
                     }
                 } else{
                     _randomQuote.emit(Resource.Error(getError(response.errorBody()?.string())))
                 }
             } else{
                 val randomQuoteFromBd = quoteRepository.getRandomQuoteFromDb()
                 randomQuoteFromBd?.let {
                     _randomQuote.emit(Resource.Success(it.toRandomQuote()))
                 }
             }
            } catch (e: Exception) {
                _randomQuote.emit(Resource.Error(e.message ?: "An unknown error occurred"))
            }
        }
    }
    fun getQuotesByAuthor(author: String) {
        viewModelScope.launch {
            quoteRepository.getQuotesByAuthor(author)
                .flow
                .cachedIn(viewModelScope)
                .collect {
                    _searchedQuotesByAuthor.value = it
                }
        }
    }


    fun addMyQuote(quote:QuoteEntity) {
        addMyQuoteResponse.postValue(Resource.Loading())
        try {
            viewModelScope.launch {
               val response = quoteRepository.addMyQuote(quote)
                addMyQuoteResponse.postValue(Resource.Success(response))
            }
        } catch (e:Exception){
            addMyQuoteResponse.postValue(Resource.Error(e.message.toString()))
        }
    }


    fun deleteMyQuote(quote:QuoteEntity) {
        deleteMyQuoteResponse.postValue(Resource.Loading())
        try {
            viewModelScope.launch {
                val response = quoteRepository.deleteMyQuote(quote)
                deleteMyQuoteResponse.postValue(Resource.Success(response))
            }
        } catch (e:Exception){
            deleteMyQuoteResponse.postValue(Resource.Error(e.message.toString()))
        }
    }



    fun addFavQuote(quote: QuoteEntity) {
        addFavQuoteResponse.postValue(Resource.Loading())
        try {
            viewModelScope.launch {
                val response = favoriteQuoteRepository.addFavQuote(quote)
                addFavQuoteResponse.postValue(Resource.Success(response))
            }
        } catch (e:Exception){
            addFavQuoteResponse.postValue(Resource.Error(e.message.toString()))
        }
    }


    fun deleteFavQuote(quote:QuoteEntity) {
        deleteFavQuoteResponse.postValue(Resource.Loading())
        try {
            viewModelScope.launch {
                val response = favoriteQuoteRepository.deleteFavQuote(quote)
                deleteFavQuoteResponse.postValue(Resource.Success(response))
            }
        } catch (e:Exception){
            deleteFavQuoteResponse.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun deleteFavQuoteById(quoteId:String) {
        deleteFavQuoteByIdResponse.postValue(Resource.Loading())
        try {
            viewModelScope.launch {
                val response = favoriteQuoteRepository.deleteQuoteById(quoteId)
                deleteFavQuoteByIdResponse.postValue(Resource.Success(response))
            }
        } catch (e:Exception){
            deleteFavQuoteByIdResponse.postValue(Resource.Error(e.message.toString()))
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun getQuotesById(id: String, category: String) {
        viewModelScope.launch {
            val quote = quoteRepository.getQuoteById(id,category)
               quoteById.postValue(quote)
        }
    }


    fun searchQuotes(query: String) {
        viewModelScope.launch {
             quoteRepository.getSearchQuote(query)
                 .flow
                 .cachedIn(viewModelScope)
                 .collect {
                     _searchedQuotes.value = it
                 }
        }
    }

    fun updateSearchText(newText: String) {
        _searchText.value = newText
    }

    fun updateAuthorSearchText(newText: String) {
        _searchTextForAuthor.value = newText
    }
    fun getSearchedAuthors(query: String ){
    viewModelScope.launch {
        quoteRepository.getSearchAuthors(query)
            .flow
            .cachedIn(viewModelScope)
            .collect{
                _searchForAuthors.value = it
            }
    }
    }

    fun getAuthors(){
        viewModelScope.launch {
            quoteRepository.getAuthors()
                .flow
                .cachedIn(viewModelScope)
                .collect{
                    _authors.value = it
                }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun getAllTags()  {
        tagList.postValue(Resource.Loading())
        try {
          if (hasInternetConnection()){
              val response = quoteRepository.getTags()
              if (response.isSuccessful){
                  response.body()?.let {
                      tagList.postValue(Resource.Success(it))
                  }
              } else{
                  tagList.postValue(Resource.Error(getError(response.errorBody()?.string())))
              }
          } else{
              tagList.postValue(Resource.Error(context.getString(R.string.no_internet_connection)))
          }
        } catch (e: Exception) {
            tagList.postValue(Resource.Error(e.message!!))
        }
    }

    suspend fun getRandomQuotes()  {
        randomQuotes.postValue(Resource.Loading())
        try {
           if (hasInternetConnection()){
               val response = quoteRepository.getRandomQuotes()
               if (response.isSuccessful){
                   response.body()?.let {
                       randomQuotes.postValue(Resource.Success(it))
                   }
               } else{
                   randomQuotes.postValue(Resource.Error(getError(response.errorBody()?.string())))
               }
           }else{
               randomQuotes.postValue(Resource.Error(context.getString(R.string.no_internet_connection)))
           }
        } catch (e: Exception) {
            randomQuotes.postValue(Resource.Error(e.message!!))
        }
    }

    suspend fun getAuthorById(authorId:String){
        authorById.postValue(Resource.Loading())
        try {
             if (hasInternetConnection()){
                 val response = quoteRepository.getAuthorById(authorId)
                 if (response.isSuccessful){
                     response.body()?.let {
                         authorById.postValue(Resource.Success(it))
                     }
                 } else{
                     authorById.postValue(Resource.Error(getError(response.errorBody()?.string())))
                 }
             } else{
                 authorById.postValue(Resource.Error(context.getString(R.string.no_internet_connection)))
             }
        } catch (e: Exception){
            authorById.postValue(Resource.Error(e.message!!))
        }
    }

    suspend fun getAuthorBySlug(slug:String){
        authorBySlug.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = quoteRepository.getAuthorBySlug(slug)
                if (response.isSuccessful){
                    response.body()?.let {
                        authorBySlug.postValue(Resource.Success(it))
                    }
                } else{
                    authorBySlug.postValue(Resource.Error(getError(response.errorBody()?.string())))
                }
            } else{
                authorBySlug.postValue(Resource.Error(context.getString(R.string.no_internet_connection)))
            }
        } catch (e: Exception){
            authorBySlug.postValue(Resource.Error(e.message!!))
        }
    }
    
    
    suspend fun getUserTagsCategory()  {
        userTags.postValue(Resource.Loading())
        try {
            tagRepository.getUserTags().collect{
                userTags.postValue(Resource.Success(it))
            }
        } catch (e: Exception) {
            userTags.postValue(Resource.Error(e.message!!))
        }
    }




     fun addUserTagsCategory(tags:List<TagsItemEntity>)  {
         Log.e("ADDTAGSRESPONSE","called now!!")
         addUserTagsResponse.postValue(Resource.Loading())
        try {
           viewModelScope.launch {
               val msg = tagRepository.addUserTags(tags)
               addUserTagsResponse.postValue(Resource.Success(msg))
               Log.e("ADDTAGSRESPONSE","called success now!! ${msg}")
           }
            Log.e("ADDTAGSRESPONSE","called try now!!")
        } catch (e: Exception) {
            addUserTagsResponse.postValue(Resource.Error(e.message!!))
            Log.e("ADDTAGSRESPONSE", e.message!!)
        }
    }

    fun clearTagCategories(){
        viewModelScope.launch {
            tagRepository.clearAllTags()
        }
    }

    fun doesQuoteExist(quoteId: String) {
        viewModelScope.launch {
            val exists = favoriteQuoteRepository.isFavorite(quoteId)
            isFavorite.postValue(exists)
        }
    }

    suspend fun favQuoteDuplicate(quoteId: String) : Boolean {
        return favoriteQuoteRepository.isFavorite(quoteId)
    }



    fun updateLocale(newLocale: Locale) {
//        _locale.value = newLocale
    }



















    private fun hasInternetConnection() : Boolean {
        val connectivityManager = getApplication<BaseApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
        return false
    }
}