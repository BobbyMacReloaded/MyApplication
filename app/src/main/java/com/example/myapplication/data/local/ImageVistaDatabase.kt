package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.local.entity.FavoriteImageEntity
import com.example.myapplication.data.local.entity.UnsplashImageEntity
import com.example.myapplication.data.local.entity.UnsplashRemoteKeys

@Database(
    entities = [FavoriteImageEntity::class , UnsplashImageEntity::class, UnsplashRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class ImageVistaDatabase: RoomDatabase(){
    abstract fun favoriteImageDao():FavoritesImagesDao

    abstract fun  editorialFeedDao():EditorialFeedDao
}