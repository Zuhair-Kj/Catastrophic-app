package com.example.catastrophic

import android.app.Application
import com.example.core.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CatastrophicApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    private fun startKoin() {
        startKoin {
            androidContext(this@CatastrophicApp)
            modules(
                listOf(
                    networkModule
                )
            )

        }
    }
}