package com.johnmarsel.bookly

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.johnmarsel.bookly.model.BestSellerItem
import com.johnmarsel.bookly.model.CarouselItem

class MainFragmentViewModel: ViewModel() {

    private val repository = Repository.get()

    val carousel: LiveData<List<CarouselItem>> = repository.getCarousel()
    val bestSellers: LiveData<List<BestSellerItem>> = repository.getBestSellers()
}