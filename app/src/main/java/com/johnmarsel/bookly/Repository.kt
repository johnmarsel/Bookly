package com.johnmarsel.bookly

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.room.Room
import androidx.room.withTransaction
import com.johnmarsel.bookly.api.EbookApi
import com.johnmarsel.bookly.database.BookDatabase
import com.johnmarsel.bookly.model.BestSellerItem
import com.johnmarsel.bookly.model.CarouselItem
import com.johnmarsel.bookly.model.SimilarItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

private const val DATABASE_NAME = "database"

class Repository private constructor(context: Context) {

    private val ebookApi = EbookApi.get()

    private val database : BookDatabase = Room.databaseBuilder(
        context.applicationContext,
        BookDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val bookDao = database.bookDao()

    fun getCarousel(): Flow<Resource<List<CarouselItem>>> = networkBoundResource(
        query = {
            bookDao.getCarousel()
        },
        fetch = {
            ebookApi.fetchCarousel()
        },
        saveFetchResult = { carousel ->
            database.withTransaction {
                bookDao.deleteCarousel()
                bookDao.insertCarousel(carousel)
            }
        },
        shouldFetch = { cachedCarousel ->
            val needsFetch = cachedCarousel.isEmpty()

            needsFetch
        }
    )

    fun getBestSellers(): Flow<Resource<List<BestSellerItem>>> = networkBoundResource(
        query = {
            bookDao.getBestSellers()
        },
        fetch = {
            ebookApi.fetchBestSellers()
        },
        saveFetchResult = { bestSellers ->
            database.withTransaction {
                bookDao.deleteBestSellers()
                bookDao.insertBestSellers(bestSellers)
            }
        },
        shouldFetch = {cachedBestSellers ->
            val needsFetch = cachedBestSellers.isEmpty()

            needsFetch
        }
    )

    fun getBestSeller(bookId: Int): LiveData<BestSellerItem> {
        return bookDao.getBestSeller(bookId)
    }

    fun getSimilar(): Flow<Resource<List<SimilarItem>>> = networkBoundResource(
        query = {
            bookDao.getSimilar()
        },
        fetch = {
            ebookApi.fetchSimilar()
        },
        saveFetchResult = { similarItems ->
            database.withTransaction {
                bookDao.deleteSimilar()
                bookDao.insertSimilar(similarItems)
            }
        },
        shouldFetch = {cachedSimilar ->
            val needsFetch = cachedSimilar.isEmpty()

            needsFetch
        }
    )

    companion object {
        private var INSTANCE: Repository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = Repository(context)
            }
        }

        fun get(): Repository {
            return INSTANCE ?:
            throw IllegalStateException("Repository must be initialized")
        }
    }

}