package com.example.myapplication.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.domain.model.UnsplashImage
import com.example.myapplication.domain.repository.ImageRepository
import com.example.myapplication.presentation.util.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ImageRepository
):ViewModel() {
    private val _snackbarEvent = Channel<SnackBarEvent>()
    val snackBarEvent= _snackbarEvent.receiveAsFlow()

    val  images: StateFlow<PagingData<UnsplashImage>> = repository.getEditorialFeedImages()
        .catch { exception->
            _snackbarEvent.send(
                SnackBarEvent(message = "Something Went Wrong. ${exception.message}")
            )
        }
        .cachedIn(viewModelScope)
        .stateIn(
            scope =viewModelScope ,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = PagingData.empty()
        )
    val favoritesImageIds: StateFlow<List<String>> = repository.getFavoriteImageIds()
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

