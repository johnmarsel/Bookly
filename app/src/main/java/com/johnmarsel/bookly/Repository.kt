package com.johnmarsel.bookly

import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import com.johnmarsel.bookly.api.EbookApi
import com.johnmarsel.bookly.database.BookDatabase
import com.johnmarsel.bookly.model.BestSellerItem
import com.johnmarsel.bookly.model.CarouselItem
import com.johnmarsel.bookly.model.SimilarItem
import com.johnmarsel.bookly.util.Resource
import com.johnmarsel.bookly.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val ebookApi: EbookApi,
    private val database: BookDatabase
) {

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
}