package com.johnmarsel.bookly.di

import android.app.Application
import androidx.room.Room
import com.johnmarsel.bookly.api.EbookApi
import com.johnmarsel.bookly.database.BookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(EbookApi.BASE_URL)
            .client(OkHttpClient.Builder().also { client ->
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
            }.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideEbookApi(retrofit: Retrofit): EbookApi =
        retrofit.create(EbookApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): BookDatabase =
        Room.databaseBuilder(app, BookDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()
}