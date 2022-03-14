package com.mirkamol.pinterestclonemyproject.networking.service

import com.mirkamol.pinterestclonemyproject.model.homephoto.HomePhotoItem
import com.mirkamol.pinterestclonemyproject.model.profile.User
import com.mirkamol.pinterestclonemyproject.model.reletedconnection.SinglePhoto
import com.mirkamol.pinterestclonemyproject.model.search.ResponseSearch
import com.mirkamol.pinterestclonemyproject.model.search.Topic
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("photos")
    fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<List<HomePhotoItem>>

    @GET("photos/{id}")
    fun getSelectedPhoto(@Path("id") id: String): Call<HomePhotoItem>

    @GET("search/photos?")
    fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<ResponseSearch>

    @GET("photos/{id}")
    fun getImageToRelated(@Path("id") id: String): Call<SinglePhoto>


    @GET("topics")
    fun getTopics(@Query("page") page:Int, @Query("per_page") per_page: Int): Call<List<Topic>>
    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Call<User>

}