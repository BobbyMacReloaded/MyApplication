package com.example.myapplication.data.mapper

import com.example.myapplication.data.local.entity.FavoriteImageEntity
import com.example.myapplication.data.local.entity.UnsplashImageEntity
import com.example.myapplication.data.remote.dto.UnsplashImageDto
import com.example.myapplication.domain.model.UnsplashImage

fun UnsplashImageDto.toDomainModel():UnsplashImage{
    return  UnsplashImage(
        id = this.id,
        imageUrlSmall = this.urls.small,
        imageUrlRaw = this.urls.full,
        imageUrlRegular = this.urls.regular,
        photographerName = this.user.name,
        photographerUsername = this.user.username,
        photographerProfileImageUrl = this.user.profile_image.small,
        photographerProfileLink = this.user.links.html,
        width = this.width,
        height = this.height,
        description = this.description

    )
}
fun UnsplashImageDto.toEntity():UnsplashImageEntity{
    return  UnsplashImageEntity(
        id = this.id,
        imageUrlSmall = this.urls.small,
        imageUrlRaw = this.urls.full,
        imageUrlRegular = this.urls.regular,
        photographerName = this.user.name,
        photographerUsername = this.user.username,
        photographerProfileImageUrl = this.user.profile_image.small,
        photographerProfileLink = this.user.links.html,
        width = this.width,
        height = this.height,
        description = this.description

    )
}

fun UnsplashImage.toFavoriteImageEntity():FavoriteImageEntity{
    return FavoriteImageEntity(
        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRaw = this.imageUrlRaw,
        imageUrlRegular = this.imageUrlRegular,
        photographerName = this.photographerName,
        photographerUsername = this.photographerUsername,
        photographerProfileImageUrl = this.photographerProfileImageUrl,
        photographerProfileLink = this.photographerProfileLink,
        width = this.width,
        height = this.height,
        description = this.description
    )
}
fun FavoriteImageEntity.toDomainModel():UnsplashImage{
    return UnsplashImage(
        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRaw = this.imageUrlRaw,
        imageUrlRegular = this.imageUrlRegular,
        photographerName = this.photographerName,
        photographerUsername = this.photographerUsername,
        photographerProfileImageUrl = this.photographerProfileImageUrl,
        photographerProfileLink = this.photographerProfileLink,
        width = this.width,
        height = this.height,
        description = this.description
    )
}
fun UnsplashImageEntity.toDomainModel():UnsplashImage{
    return UnsplashImage(
        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRaw = this.imageUrlRaw,
        imageUrlRegular = this.imageUrlRegular,
        photographerName = this.photographerName,
        photographerUsername = this.photographerUsername,
        photographerProfileImageUrl = this.photographerProfileImageUrl,
        photographerProfileLink = this.photographerProfileLink,
        width = this.width,
        height = this.height,
        description = this.description
    )
}

fun List<UnsplashImageDto>.toDomainModelList():List<UnsplashImage>{
    return this.map { it.toDomainModel() }
}
fun List<UnsplashImageDto>.toEntityList():List<UnsplashImageEntity>{
    return this.map { it.toEntity() }
}
