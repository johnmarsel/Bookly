package com.johnmarsel.bookly.detailpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.*
import com.johnmarsel.bookly.R
import com.johnmarsel.bookly.databinding.FragmentDetailBinding
import com.johnmarsel.bookly.databinding.SimilarItemBinding
import com.johnmarsel.bookly.model.SimilarItem
import com.johnmarsel.bookly.util.loadImage
import dagger.hilt.android.AndroidEntryPoint

const val BOOK_ID = "book_id"

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private var bookId: Int? = null
    private var adapter: SimilarAdapter = SimilarAdapter()

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookId = it.getInt(BOOK_ID)
        }
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
                button1.text = "${bestseller.price}€"
                score.text = bestseller.rate.score.toString()
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
        binding.toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.x)
    }

    private inner class SimilarHolder(private val binding: SimilarItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(book: SimilarItem) {
            binding.bookImage.loadImage(book.image)
        }
    }

    private inner class SimilarAdapter: ListAdapter<SimilarItem, SimilarHolder>(DiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarHolder {
            val binding = SimilarItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return SimilarHolder(binding)
        }

        override fun onBindViewHolder(holder: SimilarHolder, position: Int) {
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