package com.example.myapplication.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.myapplication.data.local.entity.FavoriteImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesImagesDao {
    @Query("SELECT * FROM favorites_images_table")
    fun getAllFavoriteImages():PagingSource<Int ,FavoriteImageEntity>

   @Upsert
    suspend fun  insertFavoriteImages(image:FavoriteImageEntity)

    @Delete
    suspend fun  deleteFavoriteImages(image:FavoriteImageEntity)
    @Query("SELECT EXISTS (SELECT 1 FROM favorites_images_table WHERE id = :id)")
    suspend fun isImageFavorite(id:String):Boolean

    @Query("SELECT id FROM  favorites_images_table")
    fun getFavoriteImageIds(): Flow<List<String>>
}