package com.example.core

import com.example.core.api.BrowseService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://api.thecatapi.com/"
const val SCOPE_BROWSE = "browse"
@JvmField
val networkModule = module {
    single<BrowseService> {
        get<Retrofit>(named(SCOPE_BROWSE)).create(BrowseService::class.java)
    }

    single(named(SCOPE_BROWSE)) { Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(get())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
         HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }
    }
}