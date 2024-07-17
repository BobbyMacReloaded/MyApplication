package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.data.util.Constant.UNSPLASH_REMOTE_KEYS_TABLE

@Entity(tableName = UNSPLASH_REMOTE_KEYS_TABLE )
data class UnsplashRemoteKeys(
    @PrimaryKey
    val id :String,
    val prevPage:Int?,
    val nextPage:Int?

)
