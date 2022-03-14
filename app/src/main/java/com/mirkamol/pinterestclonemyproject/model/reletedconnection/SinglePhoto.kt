package com.mirkamol.pinterestclonemyproject.model.reletedconnection

import com.mirkamol.pinterestclonemyproject.model.homephoto.Urls

data class SinglePhoto(
    val id: String,
    val urls: Urls,
    val likes: Long,
    val related_collections: RelatedCollections,
)
