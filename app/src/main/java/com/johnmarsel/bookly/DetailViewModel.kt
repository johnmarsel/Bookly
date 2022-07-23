package com.johnmarsel.bookly

import androidx.lifecycle.*
import com.johnmarsel.bookly.model.BestSellerItem
import com.johnmarsel.bookly.model.SimilarItem
import java.util.*

class DetailViewModel: ViewModel() {

    private val repository = Repository.get()

    val similarBooks = repository.getSimilar().asLiveData()

    private val bookIdLiveData = MutableLiveData<Int>()

    var bookLiveData: LiveData<BestSellerItem> =
        Transformations.switchMap(bookIdLiveData) { bookId ->
            repository.getBestSeller(bookId)
        }

    fun loadBestSeller(bookId: Int) {
        bookIdLiveData.value = bookId
    }
}