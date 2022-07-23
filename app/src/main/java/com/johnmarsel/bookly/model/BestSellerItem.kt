package com.johnmarsel.bookly.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bestSellers")
data class BestSellerItem (
    @PrimaryKey val id: Int,
    val title: String,
    val author: String,
    val price: Float,
    val image: String,
    @Embedded val rate: Rate
)