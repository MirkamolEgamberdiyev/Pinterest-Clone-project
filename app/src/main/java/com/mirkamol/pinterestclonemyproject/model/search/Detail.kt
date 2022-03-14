package com.mirkamol.pinterestclonemyproject.model.search

import com.mirkamol.pinterestclonemyproject.model.homephoto.Urls

data class Detail(
    val color:String,
    val id:String,
    val description:String,
    val urls: Urls
)
