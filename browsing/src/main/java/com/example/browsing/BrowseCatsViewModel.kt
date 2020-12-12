package com.example.browsing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.api.BrowseService
import com.example.core.model.Cat
import com.example.core.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class BrowseCatsViewModel: ViewModel(), KoinComponent {
    private val apiService: BrowseService by inject()

    val catsLiveData = MutableLiveData<Resource<List<Cat>>>()
    var currentPage = 1
    fun fetchCats() {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
            catsLiveData.postValue(Resource.error(message = exception.message ?: ""))
        }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            catsLiveData.postValue(Resource.loading())
            val list = apiService.getCats(20, currentPage, "png", "Desc")
            list?.let {
                catsLiveData.postValue(Resource.success(it))
                return@launch
            }
        }
    }
}