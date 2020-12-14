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

    private val itemsFromApi = mutableListOf<Cat>()
    val catsLiveData = MutableLiveData<Resource<List<Cat>>>(Resource.loading())
    var currentPage = 1

    fun fetchCats() {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
            catsLiveData.postValue(Resource.error(message = exception.message ?: ""))
        }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            catsLiveData.postValue(Resource.loading())

            val list = if (networkHelper.connectivityLiveData.value == true)
                browseCatsRepository.fetchCatsFromApi(defaultParamsMap.plus("limit" to "20").plus("offset" to "${(currentPage - 1)*20}"))
                    ?.also { browseCatsRepository.save(it) }
                else if (itemsFromApi.isEmpty()) {
                    browseCatsRepository.getAll()
                } else {
                emptyList()
            }
            list?.let {
                if (it.isNotEmpty()) {
                    itemsFromApi.addAll(it)
                    catsLiveData.postValue(Resource.success(itemsFromApi))
                    currentPage++
                    return@launch
                }
            }
            catsLiveData.postValue(Resource.error())
        }
    }
}