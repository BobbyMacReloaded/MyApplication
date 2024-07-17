package com.example.myapplication.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState.Loading.endOfPaginationReached
import androidx.paging.LoadType
import androidx.paging.LoadType.APPEND
import androidx.paging.LoadType.PREPEND
import androidx.paging.LoadType.REFRESH
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.myapplication.data.local.ImageVistaDatabase
import com.example.myapplication.data.local.entity.UnsplashImageEntity
import com.example.myapplication.data.local.entity.UnsplashRemoteKeys
import com.example.myapplication.data.mapper.toEntityList
import com.example.myapplication.data.remote.UnsplashApiService
import com.example.myapplication.data.util.Constant
import com.example.myapplication.data.util.Constant.ITEMS_PER_PAGE

@OptIn(ExperimentalPagingApi::class)
class EditorialFeedRemoteMediator(
    private val apiService: UnsplashApiService,
    private val database: ImageVistaDatabase
):RemoteMediator<Int, UnsplashImageEntity>() {
    companion object{
        private const val   STARTING_PAGE_INDEX =1
    }

private val editorialFeedDao = database.editorialFeedDao()
    
    
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashImageEntity>
    ): MediatorResult {
      
        try {
            val currentPage = when(loadType){
                REFRESH -> {
                    STARTING_PAGE_INDEX
                }
                PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    Log.d(Constant.IV_LOG_TAG, "remoteKeyPrev:${remoteKeys?.prevPage}")
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    prevPage
                }
                APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    Log.d(Constant.IV_LOG_TAG, "remoteKeyNext:${remoteKeys?.nextPage}")

                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                  nextPage
                }
            }
         val response = 
             apiService.getEditorialFeedImages(page =currentPage ,
                 perPage = ITEMS_PER_PAGE)
            val endOfPaginationReached = response.isEmpty()
            Log.d(Constant.IV_LOG_TAG ,"endOfPaginationReached : $endOfPaginationReached")

            val prevPage = if (currentPage==1)null else currentPage -1
            val nextPage = if (endOfPaginationReached)null else currentPage +1
            database.withTransaction {
                if (loadType == LoadType.REFRESH){
                    editorialFeedDao.deleteAllEditorialFeedImages()
                    editorialFeedDao.deleteAllRemoteKeys()
                }
                val remoteKeys =response.map { unsplashImageDto ->
                    UnsplashRemoteKeys(
                        id = unsplashImageDto.id,
                        prevPage = prevPage,
                        nextPage = nextPage,
                    )
                }
                editorialFeedDao.insertAllRemoteKeys(remoteKeys)
                editorialFeedDao.insertEditorialFeedImages(response.toEntityList())
            }
            return MediatorResult.Success(endOfPaginationReached= endOfPaginationReached)
        }catch (e:Exception){
            Log.d(Constant.IV_LOG_TAG ,"LoadResultError : ${e.message}")

            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UnsplashImageEntity>): UnsplashRemoteKeys? {
     return state.pages.firstOrNull{ it.data.isNotEmpty()}?.data?.firstOrNull()
         ?.let { unsplashImage->
             editorialFeedDao.getRemoteKeys(id =  unsplashImage.id)
         }
    }
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UnsplashImageEntity>): UnsplashRemoteKeys? {
        return state.pages.lastOrNull{ it.data.isNotEmpty()}?.data?.lastOrNull()
            ?.let { unsplashImage->
                editorialFeedDao.getRemoteKeys(id =  unsplashImage.id)
            }
    }

}















