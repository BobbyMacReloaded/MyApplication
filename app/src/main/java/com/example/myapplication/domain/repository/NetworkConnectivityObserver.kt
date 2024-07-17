package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.NetworkStatus
import kotlinx.coroutines.flow.StateFlow

interface NetworkConnectivityObserver {
    val networkStatus:StateFlow<NetworkStatus>
}