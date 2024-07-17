package com.example.myapplication.presentation.home_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.myApplication.R
import com.example.myapplication.domain.model.UnsplashImage
import com.example.myapplication.presentation.component.ImageTopAppBar
import com.example.myapplication.presentation.component.ImagesVerticalGrid
import com.example.myapplication.presentation.component.ZoomedImageCard
import com.example.myapplication.presentation.util.SnackBarEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    snackbarHostState: SnackbarHostState,
    snackBarEvent: Flow<SnackBarEvent>,
    images:LazyPagingItems<UnsplashImage>,

    favoriteImageIds:List<String>,
    onToggleFavoriteStatus:(UnsplashImage)->Unit,
    onImageClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    onFABClick: () -> Unit,
    onFavoritesClick:()->Unit,

) {
    var showImagePreview by remember{
        mutableStateOf(false)
    }
    var activeImage by remember{
        mutableStateOf<UnsplashImage?>(null)
    }

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
                scrollBehavior = scrollBehavior,
                onSearchClick = onSearchClick,
                onFavoritesClick =onFavoritesClick,

            )
            ImagesVerticalGrid(
                images = images,
                onImageClick = onImageClick,
                onImageDragStart = {image->
                    activeImage=image
                    showImagePreview=true

                },
                onImageDragEnd ={showImagePreview=false

                } ,
                onToggleFavoriteStatus = onToggleFavoriteStatus  ,
                favoriteImageIds =favoriteImageIds

            )
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            onClick = { onFABClick() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_save_24),
                contentDescription = "save",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        ZoomedImageCard(
            modifier = Modifier.padding(20.dp),
            image =activeImage,
            isVisible = showImagePreview
        )
    }
}
