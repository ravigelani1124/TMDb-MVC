package com.interview.tmdb_mvc.service

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("3/movie/popular")
    fun getPopularMovieList(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Call<JsonObject>

    @GET("3/configuration")
    fun configuration(
        @Query("api_key") api_key: String,
    ): Call<JsonObject>
}
