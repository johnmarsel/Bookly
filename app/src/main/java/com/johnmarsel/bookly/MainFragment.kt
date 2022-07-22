package com.johnmarsel.bookly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
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
import com.johnmarsel.bookly.model.BestSellerItem
import com.johnmarsel.bookly.databinding.BestSellerItemBinding
import com.johnmarsel.bookly.databinding.FragmentMainBinding
import java.lang.Math.abs

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainFragmentViewModel
    private var bestSellerAdapter: BestSellerAdapter = BestSellerAdapter()
    private var carouselAdapter: CarouselAdapter = CarouselAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.recyclerViewBestSeller.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewBestSeller.adapter = bestSellerAdapter
        binding.viewPager2.adapter = carouselAdapter

        binding.viewPager2.offscreenPageLimit = 3
        binding.viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(30))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }

        binding.viewPager2.setPageTransformer(compositePageTransformer)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        viewModel.bestSellers.observe(
            viewLifecycleOwner
        ) { bestSellers ->
            bestSellerAdapter.submitList(bestSellers)
        }
        viewModel.carousel.observe(
            viewLifecycleOwner
        ) { carouselItems ->
            carouselAdapter.submitList(carouselItems)
        }
    }

    private fun setUpToolbar() {
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
            binding.bookImage.loadImage(book.image)
            binding.bookTitle.text = book.title
            binding.bookAuthor.text = book.author
            binding.bookPrice.text = book.price.toString()
            binding.bookRateScore.text = book.rate.score.toString()
            binding.bookRateAmount.text = book.rate.amount.toString()
        }

        override fun onClick(v: View) {
            val args = Bundle().apply {
                putInt(BOOK_ID, book.id)
            }
            findNavController().navigate(R.id.action_mainFragment_to_detailFragment, args)
        }
    }

    private inner class BestSellerAdapter: ListAdapter<BestSellerItem, BestSellerHolder>(DiffCallback()) {

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