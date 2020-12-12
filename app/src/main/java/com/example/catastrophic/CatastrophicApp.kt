package com.example.catastrophic

import android.app.Application
import com.example.core.utils.NetworkHelper
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent

class CatastrophicApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin()
        KoinJavaComponent.get(NetworkHelper::class.java)
    }

    private fun startKoin() {
        startKoin {
            androidContext(this@CatastrophicApp)
            modules(
                listOf(
                    networkModule,
                    roomModule,
                    browseModule,
                    coreModule
                )
            )

        }
    }
}