package com.example.myapplication.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.myapplication.domain.model.NetworkStatus
import com.example.myapplication.domain.repository.NetworkConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

class NetworkConnectivityObserverImpl(
    context: Context,
    scope:CoroutineScope
):NetworkConnectivityObserver {

    private val connectivityManager
            = context.getSystemService(Context.CONNECTIVITY_SERVICE)as ConnectivityManager
    private val _networkStatus = observer().stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = NetworkStatus.DisConnected
    )
    override val networkStatus: StateFlow<NetworkStatus> = _networkStatus


    private fun observer(): Flow<NetworkStatus>{
        return callbackFlow {
            val connectivityCallBack = object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(NetworkStatus.Connected)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(NetworkStatus.DisConnected)
                }
            }
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()
            connectivityManager.registerNetworkCallback(request,connectivityCallBack)
            awaitClose{
                connectivityManager.unregisterNetworkCallback(connectivityCallBack)
            }
        }
            .distinctUntilChanged()
    }
}