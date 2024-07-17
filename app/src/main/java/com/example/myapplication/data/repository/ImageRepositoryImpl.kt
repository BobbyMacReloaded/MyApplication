package com.example.myapplication.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.myapplication.data.local.ImageVistaDatabase
import com.example.myapplication.data.mapper.toDomainModel
import com.example.myapplication.data.mapper.toDomainModelList
import com.example.myapplication.data.mapper.toFavoriteImageEntity
import com.example.myapplication.data.paging.EditorialFeedRemoteMediator
import com.example.myapplication.data.paging.SearchPagingSource
import com.example.myapplication.data.remote.UnsplashApiService
import com.example.myapplication.data.util.Constant.ITEMS_PER_PAGE
import com.example.myapplication.domain.model.UnsplashImage
import com.example.myapplication.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
@OptIn(ExperimentalPagingApi::class)
class ImageRepositoryImpl(
    private val unsplashApi:UnsplashApiService,
    private val database: ImageVistaDatabase
):ImageRepository {
    private val favoriteImageDao = database.favoriteImageDao()
    private val editorialFeedImageDao = database.editorialFeedDao()

    override  fun getEditorialFeedImages(): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            remoteMediator = EditorialFeedRemoteMediator(unsplashApi, database),
            pagingSourceFactory = {
               editorialFeedImageDao.getAllEditorialFeedImages()
            }
        ).flow
            .map { pagingData->
                pagingData.map { it.toDomainModel() }
            }
    }

    override suspend fun getImage(imageId: String): UnsplashImage {
        return unsplashApi.getImages(imageId).toDomainModel()
    }

    override fun searchImages(query: String): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                SearchPagingSource(query, unsplashApi)
            }
        ).flow
    }

    override suspend fun toggleFavoriteStatus(image: UnsplashImage) {
        val isFavorite = favoriteImageDao.isImageFavorite(image.id)
        val favoriteImage = image.toFavoriteImageEntity()
        if (isFavorite) {
            favoriteImageDao
                .deleteFavoriteImages(favoriteImage)
        } else {
            favoriteImageDao.insertFavoriteImages(favoriteImage)
        }
    }

    override fun getFavoriteImageIds(): Flow<List<String>> {
        return favoriteImageDao.getFavoriteImageIds()
    }

    override fun getAllFavoriteImages(): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                favoriteImageDao.getAllFavoriteImages()
            }
        ).flow
            .map { pagingData ->
                pagingData.map {
                    it.toDomainModel()
                }
            }
    }
}