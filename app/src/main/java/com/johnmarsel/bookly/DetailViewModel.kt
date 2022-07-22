package com.johnmarsel.bookly

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.johnmarsel.bookly.model.BestSellerItem
import com.johnmarsel.bookly.model.recItem

class DetailViewModel: ViewModel() {

    private val repository = Repository.get()

    val selectedBook: LiveData<List<BestSellerItem>> = repository.getBestSellers()
    val similar: LiveData<List<recItem>> = repository.getSimilar()
}