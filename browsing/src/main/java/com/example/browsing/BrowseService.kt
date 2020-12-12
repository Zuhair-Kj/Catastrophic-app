package com.example.browsing

import retrofit2.http.GET
import retrofit2.http.QueryMap

val defaultParamsMap = mapOf(
    "mime_types" to "png",
    "order" to "Desc",
    "limit" to "20"
)
interface BrowseService {
    @GET("/v1/images/search")
    suspend fun getCats(@QueryMap map: Map<String, String>): List<com.example.browsing.Cat>?
}