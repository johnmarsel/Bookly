package com.johnmarsel.bookly.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.johnmarsel.bookly.model.BestSellerItem
import com.johnmarsel.bookly.model.CarouselItem
import com.johnmarsel.bookly.model.SimilarItem

@Database(entities = [ BestSellerItem::class, CarouselItem::class, SimilarItem::class ], version=1)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}