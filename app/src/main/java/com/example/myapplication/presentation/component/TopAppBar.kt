package com.example.myapplication.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myApplication.R

import com.example.myapplication.domain.model.UnsplashImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "GenZ Interest",
    onSearchClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior,
    onFavoritesClick:()->Unit,
    navigationIcon: @Composable () -> Unit = {}

){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(title.first())
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                        append(title.drop(1))
                    }
                },
                fontWeight = FontWeight.ExtraBold
            )
        },
        actions = {
            IconButton(onClick = { onSearchClick() }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
            }
            IconButton(onClick = onFavoritesClick ) {
                Icon(imageVector = Icons.Default.Favorite, contentDescription ="Favorites" )
            }

        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = navigationIcon
    )
}
@Composable
fun FUllImageViewTopBar(
    modifier: Modifier = Modifier,
    onBackButtonClick: () -> Unit,
    image: UnsplashImage?,
    onPhotographerNameClick: (String) -> Unit,
    onDownloadImgClick: () -> Unit,
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Row(
            modifier = modifier.padding(horizontal = 5.dp , vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 5.dp , vertical = 20.dp),
                onClick = { onBackButtonClick() }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }

            Spacer(modifier = Modifier.width(10.dp))

            AsyncImage(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                model = image?.photographerProfileImageUrl,
                contentDescription = ""
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.clickable {
                    image?.let {
                        onPhotographerNameClick(it.photographerProfileLink)
                    }
                }
            ) {
                Text(
                    text = image?.photographerUsername ?: "",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onDownloadImgClick) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_download_24),
                    contentDescription = "download the image",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
