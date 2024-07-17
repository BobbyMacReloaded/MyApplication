package com.example.myapplication.data.paging

import android.provider.SyncStateContract.Constants
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.data.mapper.toDomainModelList
import com.example.myapplication.data.remote.UnsplashApiService
import com.example.myapplication.data.util.Constant
import com.example.myapplication.domain.model.UnsplashImage

class SearchPagingSource(
    private val query:String,
    private val unsplashApi: UnsplashApiService
):PagingSource<Int,UnsplashImage>() {
    companion object{
        private const val STARTING_PAGE_INDEX = 1
    }
    override fun getRefreshKey(state: PagingState<Int, UnsplashImage>): Int? {
        Log.d(Constant.IV_LOG_TAG ,"getRefreshKey: ${state.anchorPosition}")
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashImage> {
        val currentPage = params.key?: STARTING_PAGE_INDEX
        Log.d(Constant.IV_LOG_TAG ,"currentPage: $currentPage")

        return  try {
           val response = unsplashApi.searchImages(
               query =query,
               page = currentPage,
               perPage = params.loadSize
           )
           val endOfPaginationReached = response.images.isEmpty()
            Log.d(Constant.IV_LOG_TAG ,"Load Result response: ${response.images.toDomainModelList()}")
            Log.d(Constant.IV_LOG_TAG ,"endPaginationReached: $endOfPaginationReached")


            LoadResult.Page(
               data = response.images.toDomainModelList(),
               prevKey = if (currentPage== STARTING_PAGE_INDEX) null else currentPage -1,
               nextKey = if (endOfPaginationReached) null else currentPage + 1
           )
       }catch (e:Exception){
            Log.d(Constant.IV_LOG_TAG ,"LoadingResultError : ${e.message}")

            LoadResult.Error(e)
       }
    }
}