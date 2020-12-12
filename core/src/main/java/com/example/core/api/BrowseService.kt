package com.example.core.api

import com.example.core.model.Cat
import retrofit2.http.GET
import retrofit2.http.Query

interface BrowseService {
    @GET("/v1/images/search")
    suspend fun getCats(@Query("limit") limit: Int,
                @Query("page") page: Int,
                @Query("mime_types") type: String,
                @Query("order") order: String
    ): List<Cat>?
}