package com.example.browsing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.NetworkHelper
import com.example.core.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class BrowseCatsViewModel: ViewModel(), KoinComponent {
    private val browseCatsRepository: BrowseCatsRepository by inject()
    private val networkHelper: NetworkHelper by inject()

    val catsLiveData = MutableLiveData<Resource<List<Cat>>>()
    var currentPage = 1
//    fun fetchCats() {
//        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
//            exception.printStackTrace()
//            catsLiveData.postValue(Resource.error(message = exception.message ?: ""))
//        }
//        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
//            catsLiveData.postValue(Resource.loading())
//
//            val list = browseCatsRepository.fetchCatsFromApi(defaultParamsMap.plus("page" to currentPage.toString()))
//            list?.let {
//                browseCatsRepository.save(it)
//                catsLiveData.postValue(Resource.success(it))
//                return@launch
//            }
//        }
//    }

    fun fetchCats() {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
            catsLiveData.postValue(Resource.error(message = exception.message ?: ""))
        }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            catsLiveData.postValue(Resource.loading())

            val list = if (networkHelper.connectivityLiveData.value == true)
                browseCatsRepository.fetchCatsFromApi(defaultParamsMap.plus("limit" to "20").plus("offset" to "${currentPage*20}"))
                    ?.also { browseCatsRepository.save(it) }
                else
                browseCatsRepository.getAll()
            list?.let {
                catsLiveData.postValue(Resource.success(it))
                currentPage++
            }
        }
    }
}