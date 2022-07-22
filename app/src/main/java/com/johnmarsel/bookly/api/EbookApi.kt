package com.johnmarsel.bookly.api

import com.johnmarsel.bookly.model.BestSellerItem
import com.johnmarsel.bookly.model.CarouselItem
import com.johnmarsel.bookly.model.recItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface EbookApi {

    @GET("stellardiver/ebookdata/carousel")
    suspend fun fetchCarousel(): List<CarouselItem>

    @GET("stellardiver/ebookdata/best")
    suspend fun fetchBestSellers(): List<BestSellerItem>

    @GET("stellardiver/ebookdata/similar")
    suspend fun fetchSimilar(): List<recItem>

    companion object {

        private var INSTANCE: EbookApi? = null

        private const val BASE_URL = "https://my-json-server.typicode.com/"

        fun create() {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(OkHttpClient.Builder().also { client ->
                        val logging = HttpLoggingInterceptor()
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                        client.addInterceptor(logging)
                    }.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(EbookApi::class.java)
            }
        }

        fun get(): EbookApi {
            return INSTANCE ?:
            throw IllegalStateException("EbookApi must be initialized")
        }
    }
}