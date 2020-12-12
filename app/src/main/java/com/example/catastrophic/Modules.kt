package com.example.catastrophic

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.browsing.BrowseCatsRepository
import com.example.browsing.BrowseService
import com.example.browsing.CatsDao
import com.example.core.utils.NetworkHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://api.thecatapi.com/"
const val SCOPE_BROWSE = "browse"

@JvmField
val coreModule = module {
    single {
        NetworkHelper(androidContext())
    }
}
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

@JvmField
val roomModule = module {
    single {
        Room.databaseBuilder(androidContext(), CatastrophicDataBase::class.java, DATABASE_NAME).build()
    }

    single {
        get<CatastrophicDataBase>().catsDao()
    }
}

@JvmField
val browseModule = module {
    single {
        BrowseCatsRepository(get(), get())
    }
}