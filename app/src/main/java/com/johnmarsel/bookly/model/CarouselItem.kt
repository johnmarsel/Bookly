package com.johnmarsel.bookly.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carousel")
data class CarouselItem (
    @PrimaryKey val id: Int,
    val image: String
    )