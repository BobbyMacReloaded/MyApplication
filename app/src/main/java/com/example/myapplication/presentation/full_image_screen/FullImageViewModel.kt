package com.example.myapplication.presentation.full_image_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.UnsplashImage
import com.example.myapplication.domain.repository.Downloader
import com.example.myapplication.domain.repository.ImageRepository
import com.example.myapplication.presentation.util.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class FullImageViewModel @Inject constructor(
    private val repository: ImageRepository,
    private val downloader: Downloader,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val imageId: String? = savedStateHandle["imageId"]
    private val _snackbarEvent = Channel<SnackBarEvent>()
    val snackBarEvent= _snackbarEvent.receiveAsFlow()
    var image: UnsplashImage? by mutableStateOf(null)
        private set

    init {
        getImage()
    }

    private fun getImage() {
        viewModelScope.launch {
            try {
                imageId?.let { id ->
                    val result = repository.getImage(id)
                    image = result
                }
            } catch (e: UnknownHostException) {
                _snackbarEvent.send(
                    SnackBarEvent(message = "No Internet Connection Please check you internet")
                )
            }catch (e:Exception){
                _snackbarEvent.send(
                    SnackBarEvent(message = "Something Went Wrong: ${e.message}"))
            }
        }
    }
    fun  downloadImage(url:String , title:String?){
        viewModelScope.launch {
            try {
                downloader.downloadFile(url,title)
            }catch (e:Exception ){
                _snackbarEvent.send(
                    SnackBarEvent(message = "Something Went Wrong: ${e.message}"))
            }
        }
    }
}









