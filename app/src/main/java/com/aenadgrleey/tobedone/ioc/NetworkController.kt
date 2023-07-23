package com.aenadgrleey.tobedone.ioc

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.aenadgrleey.core.di.AppContext
import com.aenadgrleey.todo.work.di.NetworkTracker
import javax.inject.Inject

class NetworkController @Inject constructor(
    @AppContext private val context: Context,
    @NetworkTracker private val networkTracker: ConnectivityManager.NetworkCallback,
) {
    fun setUpNetworkControl() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkTracker)
    }
}