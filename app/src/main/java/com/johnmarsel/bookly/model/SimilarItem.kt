package com.johnmarsel.bookly.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "similarItems")
data class SimilarItem (
    @PrimaryKey val id: Int,
    val image: String
    )