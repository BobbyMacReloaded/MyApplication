package com.example.myapplication.domain.repository

interface Downloader {
    fun downloadFile(url:String , fileName:String?)
}