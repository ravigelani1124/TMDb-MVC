package com.interview.tmdb_mvc.model.configuration

data class ConfigurationResponseModel(
    val change_keys: List<String>,
    val images: Images
)