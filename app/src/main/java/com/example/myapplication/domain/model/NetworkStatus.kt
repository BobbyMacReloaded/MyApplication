package com.example.myapplication.domain.model

sealed class NetworkStatus {
    data object Connected:NetworkStatus()
    data object DisConnected:NetworkStatus()
}