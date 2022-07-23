package com.johnmarsel.bookly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.*
import com.johnmarsel.bookly.databinding.FragmentDetailBinding
import com.johnmarsel.bookly.databinding.RecItemBinding
import com.johnmarsel.bookly.model.BestSellerItem
import com.johnmarsel.bookly.model.SimilarItem

const val BOOK_ID = "book_id"

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var bookId: Int? = null
    private var adapter: recAdapter = recAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookId = it.getInt(BOOK_ID)
        }
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        bookId?.let { viewModel.loadBestSeller(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        binding.recRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.recRecycler.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        viewModel.bookLiveData.observe(
            viewLifecycleOwner
        ) { bestseller ->
            binding.apply {
                detailImage.loadImage(bestseller.image)
                selectedBookTitle.text = bestseller.title
                selectedBookAuthor.text = bestseller.author
                score.text = bestseller.rate.score.toString()
                amount.text = bestseller.rate.amount.toString()
            }
        }
        viewModel.similarBooks.observe(
            viewLifecycleOwner
        ) { similarBooks ->
            adapter.submitList(similarBooks.data)
        }

    }

    private fun setUpToolbar() {
        val navController = NavHostFragment.findNavController(this)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private inner class RecHolder(private val binding: RecItemBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var book: BestSellerItem

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(book: SimilarItem) {

            binding.bookImage.loadImage(book.image)
        }

        override fun onClick(v: View) {
        }
    }

    private inner class recAdapter: ListAdapter<SimilarItem, RecHolder>(DiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {
            val binding = RecItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return RecHolder(binding)
        }

        override fun onBindViewHolder(holder: RecHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    private class DiffCallback: DiffUtil.ItemCallback<SimilarItem>() {

        override fun areItemsTheSame(oldItem: SimilarItem, newItem: SimilarItem): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: SimilarItem, newItem: SimilarItem): Boolean {
            return oldItem == newItem
        }
    }

}