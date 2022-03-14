package com.mirkamol.pinterestclonemyproject.model.search

data class ResponseSearch(
    val results:List<Detail>,
    val likes:Long
)