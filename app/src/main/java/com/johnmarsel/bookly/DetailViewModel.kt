package com.johnmarsel.bookly

import androidx.lifecycle.*
import com.johnmarsel.bookly.model.BestSellerItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

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