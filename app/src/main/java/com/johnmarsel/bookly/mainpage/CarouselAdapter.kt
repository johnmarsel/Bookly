package com.johnmarsel.bookly.mainpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.johnmarsel.bookly.databinding.CarouselItemBinding
import com.johnmarsel.bookly.model.CarouselItem
import com.johnmarsel.bookly.util.loadImage

class CarouselAdapter: ListAdapter<CarouselItem, CarouselAdapter.CarouselHolder>(DiffCallback()) {

    inner class CarouselHolder(private val binding: CarouselItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: CarouselItem) {
            binding.carouselImage.loadImage(book.image)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarouselHolder {
        val binding = CarouselItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CarouselHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<CarouselItem>() {

        override fun areItemsTheSame(oldItem: CarouselItem, newItem: CarouselItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CarouselItem, newItem: CarouselItem): Boolean {
            return oldItem == newItem
        }
    }
}