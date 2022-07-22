package com.johnmarsel.bookly

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.johnmarsel.bookly.api.EbookApi
import com.johnmarsel.bookly.model.BestSellerItem
import com.johnmarsel.bookly.model.CarouselItem
import com.johnmarsel.bookly.model.recItem
import kotlinx.coroutines.Dispatchers

class Repository private constructor(context: Context) {

    private val ebookApi = EbookApi.get()



    fun getCarousel(): LiveData<List<CarouselItem>>  {
        return liveData(context = Dispatchers.IO) {
            emit(ebookApi.fetchCarousel())
        }
    }

    fun getBestSellers(): LiveData<List<BestSellerItem>>  {
        return liveData(context = Dispatchers.IO) {
            emit(ebookApi.fetchBestSellers())
        }
    }

    fun getSimilar(): LiveData<List<recItem>>  {
        return liveData(context = Dispatchers.IO) {
            emit(ebookApi.fetchSimilar())
        }
    }



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