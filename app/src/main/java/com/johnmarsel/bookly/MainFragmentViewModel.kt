package com.johnmarsel.bookly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

class MainFragmentViewModel: ViewModel() {

    private val repository = Repository.get()

    val carousel = repository.getCarousel().asLiveData()
    val bestSellers = repository.getBestSellers().asLiveData()
}