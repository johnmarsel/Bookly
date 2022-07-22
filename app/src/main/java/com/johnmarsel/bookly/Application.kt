package com.johnmarsel.bookly

import android.app.Application
import com.johnmarsel.bookly.api.EbookApi

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        EbookApi.create()
        Repository.initialize(this)
    }
}