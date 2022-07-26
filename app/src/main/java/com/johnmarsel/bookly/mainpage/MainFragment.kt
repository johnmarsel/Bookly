package com.johnmarsel.bookly.mainpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.johnmarsel.bookly.*
import com.johnmarsel.bookly.model.BestSellerItem
import com.johnmarsel.bookly.databinding.BestSellerItemBinding
import com.johnmarsel.bookly.databinding.FragmentMainBinding
import com.johnmarsel.bookly.detailpage.BOOK_ID
import com.johnmarsel.bookly.util.Resource
import com.johnmarsel.bookly.util.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var bestSellerAdapter: BestSellerAdapter = BestSellerAdapter()
    private var carouselAdapter: CarouselAdapter = CarouselAdapter()

    private val viewModel: MainFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.viewPager2.adapter = carouselAdapter
        binding.viewPager2.offscreenPageLimit = 3
        binding.viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(30))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        binding.viewPager2.setPageTransformer(compositePageTransformer)

        binding.recyclerViewBestSeller.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewBestSeller.adapter = bestSellerAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        viewModel.bestSellers.observe(
            viewLifecycleOwner
        ) { result ->
            binding.apply {
                bestSellersTextView.isVisible = result is Resource.Success
                progressLoading.isVisible = result is Resource.Loading && result.data.isNullOrEmpty()
                textViewError.isVisible = result is Resource.Error && result.data.isNullOrEmpty()
            }

            bestSellerAdapter.submitList(result.data)

        }
        viewModel.carousel.observe(
            viewLifecycleOwner
        ) { result ->
            binding.apply {
                bestSellersTextView.isVisible = result is Resource.Success
                progressLoading.isVisible = result is Resource.Loading && result.data.isNullOrEmpty()
                textViewError.isVisible = result is Resource.Error && result.data.isNullOrEmpty()
            }

            carouselAdapter.submitList(result.data)
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.setCollapseIcon(R.drawable.logo)
        val navController = NavHostFragment.findNavController(this)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private inner class BestSellerHolder(val binding: BestSellerItemBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var book: BestSellerItem

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(book: BestSellerItem) {
            this.book = book
            binding.apply {
                bookImage.loadImage(book.image)
                bookTitle.text = book.title
                bookAuthor.text = book.author
                bookPrice.text = "${book.price} €"
                bookRateScore.text = book.rate.score.toString()
                bookRateAmount.text = "(${book.rate.amount})"
            }
        }

        override fun onClick(v: View) {
            val args = Bundle().apply {
                putInt(BOOK_ID, book.id)
            }
            findNavController().navigate(R.id.action_mainFragment_to_detailFragment, args)
        }
    }

    private inner class BestSellerAdapter: ListAdapter<BestSellerItem, BestSellerHolder>(
        DiffCallback()
    ) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestSellerHolder {
            val binding = BestSellerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return BestSellerHolder(binding)
        }

        override fun onBindViewHolder(holder: BestSellerHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    private class DiffCallback: DiffUtil.ItemCallback<BestSellerItem>() {

        override fun areItemsTheSame(oldItem: BestSellerItem, newItem: BestSellerItem): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: BestSellerItem, newItem: BestSellerItem): Boolean {
            return oldItem == newItem
        }
    }

}