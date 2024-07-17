package com.example.myapplication.presentation.navigation

import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myapplication.presentation.favourites.FavoritesScreen
import com.example.myapplication.presentation.favourites.FavoritesViewModel
import com.example.myapplication.presentation.full_image_screen.FullImageScreen
import com.example.myapplication.presentation.full_image_screen.FullImageViewModel
import com.example.myapplication.presentation.home_screen.HomeScreen
import com.example.myapplication.presentation.home_screen.HomeViewModel
import com.example.myapplication.presentation.profile_screen.ProfileScreen
import com.example.myapplication.presentation.search_screen.SearchScreen
import com.example.myapplication.presentation.search_screen.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraphSetup (
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    snackbarHostState: SnackbarHostState,
    searchQuery:String,
    onSearchQueryChange:(String)->Unit,

){
    NavHost(navController = navController, startDestination = "HomeScreen") {
        composable("HomeScreen") {
            val homeViewModel :HomeViewModel = hiltViewModel()
           val images =homeViewModel.images.collectAsLazyPagingItems()

            val favoriteImagesIds  by homeViewModel.favoritesImageIds.collectAsStateWithLifecycle()

            HomeScreen(
                snackbarHostState =  SnackbarHostState(),
                snackBarEvent = homeViewModel.snackBarEvent,
                images = images,
                scrollBehavior = scrollBehavior,
                onImageClick = { imageId ->
                    navController.navigate("FullImageScreen/$imageId")
                },
                onSearchClick = {
                    navController.navigate("SearchScreen")
                },
                onFABClick = { navController.navigate("FavoritesScreen") },
                onFavoritesClick = {
                    navController.navigate("FavoritesScreen")
                },
                onToggleFavoriteStatus ={ homeViewModel.toggleImageStatus(it)} ,
                favoriteImageIds = favoriteImagesIds,




            )
        }
        composable("SearchScreen") {
            val searchViewModel:SearchViewModel= hiltViewModel()
            val searchedImages =searchViewModel.searchImages.collectAsLazyPagingItems()
            val favoriteImageIds by searchViewModel.favoritesImageIds.collectAsStateWithLifecycle()
          SearchScreen(
              onBackClick = { navController.navigateUp() },
              snackbarHostState = SnackbarHostState(),
              snackBarEvent =searchViewModel.snackBarEvent,
              onImageClick = {imageId ->
                  navController.navigate("FullImageScreen/$imageId")

              },
              searchedImages = searchedImages,
              onSearch = {searchViewModel.searchImages(it)},
              searchQuery = searchQuery,
              onSearchQueryChange = onSearchQueryChange,
              onToggleFavoriteStatus = { searchViewModel.toggleImageStatus(it) },
              favoriteImageIds = favoriteImageIds

          )

        }
        composable("FavoritesScreen") {
          val favoritesViewModel :FavoritesViewModel = hiltViewModel()
            val favoriteImages =favoritesViewModel.favoritesImages.collectAsLazyPagingItems()
            val favoriteImageIds by favoritesViewModel.favoritesImageIds.collectAsStateWithLifecycle()

            FavoritesScreen(

              onBackClick = { navController.navigateUp() },
              favoriteImages = favoriteImages,
              snackbarHostState =snackbarHostState ,
              snackBarEvent =favoritesViewModel.snackBarEvent ,
              favoriteImageIds = favoriteImageIds,
              scrollBehavior =scrollBehavior ,
              onImageClick ={imageId ->
            navController.navigate("FullImageScreen/$imageId")

        } ,
              onSearchClick = {navController.navigate("SearchScreen") },
              onFavoritesClick = {  },
                onToggleFavoriteStatus = { favoritesViewModel.toggleImageStatus(it)}
            )
             }
        composable(
            route = "FullImageScreen/{imageId}",
            arguments = listOf(navArgument("imageId") { type = NavType.StringType })
        ) {
            val fullImageViewModel:FullImageViewModel = hiltViewModel()
            FullImageScreen(
                image =fullImageViewModel.image,
                onBackButtonClick = { navController.navigateUp() },
                onPhotographerNameClick = { profileLink ->
                    navController.navigate("ProfileScreen/${Uri.encode(profileLink)}")
                },
                onImageDownloadClick = {url ,title->
                    fullImageViewModel.downloadImage(url, title)
                },
                snackbarState = SnackbarHostState(),
                snackBarEvent = fullImageViewModel.snackBarEvent
            )
        }
        composable(
            route = "ProfileScreen/{profileLink}",
            arguments = listOf(navArgument("profileLink") { type = NavType.StringType })
        ) {
            val profileLink = it.arguments?.getString("profileLink")
            ProfileScreen(
                profileLink = profileLink!!,
                onBackButtonClick = { navController.navigateUp() }
            )
        }
    }
}