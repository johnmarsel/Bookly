package com.johnmarsel.bookly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {

    val carousel = repository.getCarousel().asLiveData()
    val bestSellers = repository.getBestSellers().asLiveData()

}