package com.johnmarsel.bookly.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.johnmarsel.bookly.model.BestSellerItem
import com.johnmarsel.bookly.model.CarouselItem
import com.johnmarsel.bookly.model.SimilarItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM carousel")
    fun getCarousel(): Flow<List<CarouselItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarousel(carouselItems: List<CarouselItem>)

    @Query("DELETE FROM carousel")
    suspend fun deleteCarousel()

    @Query("SELECT * FROM bestSellers")
    fun getBestSellers(): Flow<List<BestSellerItem>>

    @Query("SELECT * FROM bestSellers WHERE id=(:bookId)")
    fun getBestSeller(bookId: Int): LiveData<BestSellerItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBestSellers(carouselItems: List<BestSellerItem>)

    @Query("DELETE FROM bestSellers")
    suspend fun deleteBestSellers()

    @Query("SELECT * FROM similarItems")
    fun getSimilar(): Flow<List<SimilarItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSimilar(carouselItems: List<SimilarItem>)

    @Query("DELETE FROM similarItems")
    suspend fun deleteSimilar()
}
