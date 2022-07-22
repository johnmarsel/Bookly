package com.johnmarsel.bookly.model

data class BestSellerItem (
    val id: Int,
    val title: String,
    val author: String,
    val price: Float,
    val image: String,
    val rate: Rate
)