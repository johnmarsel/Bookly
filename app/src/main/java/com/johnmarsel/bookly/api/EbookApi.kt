package com.johnmarsel.bookly.api

import com.johnmarsel.bookly.model.BestSellerItem
import com.johnmarsel.bookly.model.CarouselItem
import com.johnmarsel.bookly.model.SimilarItem
import retrofit2.http.GET

interface EbookApi {

    @GET("stellardiver/ebookdata/carousel")
    suspend fun fetchCarousel(): List<CarouselItem>

    @GET("stellardiver/ebookdata/best")
    suspend fun fetchBestSellers(): List<BestSellerItem>

    @GET("stellardiver/ebookdata/similar")
    suspend fun fetchSimilar(): List<SimilarItem>

    companion object {
        const val BASE_URL = "https://my-json-server.typicode.com/"
    }
}