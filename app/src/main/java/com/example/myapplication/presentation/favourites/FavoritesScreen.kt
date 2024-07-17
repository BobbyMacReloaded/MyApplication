package com.example.myapplication.presentation.favourites

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.myApplication.R
import com.example.myapplication.data.util.Constant
import com.example.myapplication.domain.model.UnsplashImage
import com.example.myapplication.presentation.component.ImageTopAppBar
import com.example.myapplication.presentation.component.ImagesVerticalGrid
import com.example.myapplication.presentation.component.ZoomedImageCard
import com.example.myapplication.presentation.util.SnackBarEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onBackClick:()->Unit,
    favoriteImages: LazyPagingItems<UnsplashImage>,
    snackbarHostState: SnackbarHostState,
    snackBarEvent: Flow<SnackBarEvent>,
    favoriteImageIds:List<String>,
    scrollBehavior: TopAppBarScrollBehavior,
    onImageClick :(String)->Unit,
    onSearchClick: () -> Unit,
    onFavoritesClick:()->Unit,
    onToggleFavoriteStatus:(UnsplashImage)->Unit
){


    var showImagePreview by remember{
        mutableStateOf(false) }


    var activeImage by remember{
        mutableStateOf<UnsplashImage?>(null)
    }

    Log.d(Constant.IV_LOG_TAG ,"searchedImagesCount: ${favoriteImages.itemCount}")


    LaunchedEffect(key1 = true) {
        snackBarEvent.collect{event->
            snackbarHostState.showSnackbar(
                message = event.message,
                duration = event.duration
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            ImageTopAppBar(
                title = "Favorite Images",
                scrollBehavior = scrollBehavior,
                onSearchClick = onSearchClick,
                onFavoritesClick =onFavoritesClick,
                navigationIcon =

                {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null,
                        modifier = Modifier.clickable { onBackClick() })

                }

                )
            ImagesVerticalGrid(
                images = favoriteImages,
                onImageClick = onImageClick,
                onImageDragStart = {image->
                    activeImage=image
                    showImagePreview=true

                },
                onImageDragEnd ={showImagePreview=false

                } ,
                onToggleFavoriteStatus =onToggleFavoriteStatus ,
                favoriteImageIds =favoriteImageIds

            )
        }

        ZoomedImageCard(
            modifier = Modifier.padding(20.dp),
            image =activeImage,
            isVisible = showImagePreview
        )
        if (favoriteImages.itemCount == 0){
            EmptyState(
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}
@Composable
private fun EmptyState(
    modifier: Modifier =Modifier
){
  Column(
      modifier = Modifier,
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
  ) {
      Image(
          modifier =Modifier.fillMaxWidth(),
          painter = painterResource(id = R.drawable. mount),
          contentDescription = null
      )
      Spacer(modifier = Modifier.height(40.dp))
      Text(text = "No saved Images ",
          modifier =Modifier.fillMaxWidth(),
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
      Spacer(modifier = Modifier.height(10.dp))
      Text(text = "Images you saved will be stored here ",
          modifier =Modifier.fillMaxWidth(),
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.bodyMedium)

  }
}















