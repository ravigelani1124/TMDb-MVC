package com.interview.tmdb_mvc.model.movie

data class MovieResponseModel(
    val page: Int,
    val results: List<MovieResult>,
    val total_pages: Int,
    val total_results: Int
)