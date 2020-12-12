package com.example.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class NetworkHelper(context: Context) {

//    private val mutableLiveData = MutableLiveData<Boolean>()

    val connectivityLiveData = MutableLiveData<Boolean>() // so that no one from outside can write to this live data .. only read
//    get() = mutableLiveData

    init {
        val networkCallback: NetworkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                connectivityLiveData.postValue(true)
            }

            override fun onLost(network: Network) {
                connectivityLiveData.postValue(false)
            }
        }

        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            connectivityManager.registerNetworkCallback(request, networkCallback)
        }
    }
}