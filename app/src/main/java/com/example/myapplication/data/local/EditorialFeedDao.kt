package com.example.myapplication.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.myapplication.data.local.entity.UnsplashImageEntity
import com.example.myapplication.data.local.entity.UnsplashRemoteKeys

@Dao
interface EditorialFeedDao {

    @Query("SELECT * FROM images_table")
    fun getAllEditorialFeedImages():PagingSource<Int, UnsplashImageEntity>

    @Upsert
    suspend fun insertEditorialFeedImages(image:List<UnsplashImageEntity>)

    @Query("DELETE FROM images_table ")
    suspend fun deleteAllEditorialFeedImages()

    @Query("SELECT * FROM remote_keys_table WHERE id=:id")
   suspend fun getRemoteKeys(id:String): UnsplashRemoteKeys

    @Upsert
    suspend fun insertAllRemoteKeys(remoteKeys: List<UnsplashRemoteKeys>)

    @Query("DELETE FROM remote_keys_table ")
    suspend fun deleteAllRemoteKeys()



}














