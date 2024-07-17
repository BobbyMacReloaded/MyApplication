package com.example.myapplication.presentation.search_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.domain.model.UnsplashImage
import com.example.myapplication.domain.repository.ImageRepository
import com.example.myapplication.presentation.util.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ImageRepository
):ViewModel() {
    private val _snackbarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackbarEvent.receiveAsFlow()

    private val _searchImages = MutableStateFlow<PagingData<UnsplashImage>>(PagingData.empty())
    val searchImages = _searchImages

    fun searchImages(query: String) {
        viewModelScope.launch {
            try {
            repository
                .searchImages(query)
                .cachedIn(viewModelScope)
                .collect{_searchImages.value =it}
            } catch (e: Exception) {
                _snackbarEvent.send(
                    SnackBarEvent(message = "Something Went Wrong. ${e.message}")
                )
            }
        }
    }
    val favoritesImageIds:StateFlow<List<String>> = repository.getFavoriteImageIds()
        .catch { exception->
            _snackbarEvent.send(
                SnackBarEvent(message = "Something Went Wrong. ${exception.message}")
            )
        }
        .stateIn(
            scope =viewModelScope ,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )
    fun toggleImageStatus(image: UnsplashImage){
        viewModelScope.launch {
            try {
              repository.toggleFavoriteStatus(image)
            }catch (e:Exception){
                _snackbarEvent.send(
                    SnackBarEvent(message = "Something Went Wrong. ${e.message}")
                )
            }
        }
    }

}










