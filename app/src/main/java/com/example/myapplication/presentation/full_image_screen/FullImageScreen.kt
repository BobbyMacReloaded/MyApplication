package com.example.myapplication.presentation.full_image_screen



import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.myApplication.R
import com.example.myapplication.domain.model.UnsplashImage
import com.example.myapplication.presentation.component.DownloadOptionsBottomSheet
import com.example.myapplication.presentation.component.FUllImageViewTopBar
import com.example.myapplication.presentation.component.ImageDownloadOptions
import com.example.myapplication.presentation.component.ImageVistaLoadingBar
import com.example.myapplication.presentation.util.SnackBarEvent
import com.example.myapplication.presentation.util.rememberInsetsController
import com.example.myapplication.presentation.util.toggleStatusBars
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FullImageScreen(
    image: UnsplashImage?,
    snackbarState: SnackbarHostState,
    snackBarEvent: Flow<SnackBarEvent>,
    onBackButtonClick: () -> Unit,
    onDoubleClick: () -> Unit = {},
    onPhotographerNameClick: (String) -> Unit,
    onImageDownloadClick: (String, String?) -> Unit,
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showBars by rememberSaveable { mutableStateOf(false) }
    val windowInsetsController = rememberInsetsController()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isDownloadingBottomSheetOpen by remember {
        mutableStateOf(false)

    }
    LaunchedEffect(key1 = true) {
snackBarEvent.collect{event->
    snackbarState.showSnackbar(
        message = event.message,
        duration = event.duration
    )
}
    }
    LaunchedEffect(key1 = Unit) {
        windowInsetsController.toggleStatusBars(show = showBars)
    }
    BackHandler(
        enabled = !showBars
    ) {
        windowInsetsController.toggleStatusBars(show = true)
        onBackButtonClick()
    }
    DownloadOptionsBottomSheet(
        isOpen = isDownloadingBottomSheetOpen,
        sheetState = sheetState,
        onDismissRequest = { isDownloadingBottomSheetOpen = false },
        onOptionsClick = { option ->
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) isDownloadingBottomSheetOpen = false
            }
            val url = when (option) {
                ImageDownloadOptions.SMALL -> image?.imageUrlSmall
                ImageDownloadOptions.MEDIUM -> image?.imageUrlRegular
                ImageDownloadOptions.LARGE -> image?.imageUrlRaw
            }
            url?.let {
                onImageDownloadClick(it, image?.description?.take(20))
                Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()
            }
        }
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
        ) {

            var scale by remember { mutableFloatStateOf(1f) }
            val isImageZoomed: Boolean by remember {
                derivedStateOf { scale != 1f }
            }
            var offset by remember { mutableStateOf(Offset.Zero) }
            val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
                scale = (scale * zoomChange).coerceIn(1f, 5f)
                val maxX = (constraints.maxWidth * (scale - 1)) / 2
                val maxY = (constraints.maxHeight * (scale - 1)) / 2

                offset = Offset(
                    x = (offset.x + offsetChange.x).coerceIn(-maxX, maxX),
                    y = (offset.y + offsetChange.y).coerceIn(-maxY, maxY)
                )
            }

            var isLoading by remember { mutableStateOf(true) }
            var isError by remember { mutableStateOf(false) }

            val imageLoader = rememberAsyncImagePainter(
                model = image?.imageUrlRaw,
                onState = { imageState ->
                    isLoading = imageState is AsyncImagePainter.State.Loading
                    isError = imageState is AsyncImagePainter.State.Error
                }
            )

            if (isLoading) {
                ImageVistaLoadingBar()
            }

            if (!isError) {
                Image(
                    painter = imageLoader,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .transformable(transformState)
                        .combinedClickable(
                            onClick = {
                                showBars = !showBars
                                windowInsetsController.toggleStatusBars(show = showBars)
                            },
                            onDoubleClick = {
                                scale = if (scale == 1f) 2f else 1f
                                offset = Offset.Zero
                            },
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            }
                        )
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offset.x
                            translationY = offset.y
                        }
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.baseline_error_outline_24),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            FUllImageViewTopBar(
                modifier = Modifier
                    .align(Alignment.TopCenter) // Ensure it aligns to the top
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 40.dp),
                onBackButtonClick = onBackButtonClick,
                image = image,
                onPhotographerNameClick = onPhotographerNameClick,
                onDownloadImgClick = { isDownloadingBottomSheetOpen = true },
                isVisible = showBars
            )
        }
    }
}

