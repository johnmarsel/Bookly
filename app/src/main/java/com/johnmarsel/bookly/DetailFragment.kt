package com.johnmarsel.bookly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.*
import com.johnmarsel.bookly.databinding.BestSellerItemBinding
import com.johnmarsel.bookly.databinding.FragmentDetailBinding
import com.johnmarsel.bookly.databinding.RecItemBinding
import com.johnmarsel.bookly.model.BestSellerItem
import com.johnmarsel.bookly.model.recItem

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
        viewModel.selectedBook.observe(
            viewLifecycleOwner
        ) { bestsellers ->
            var image: String
            for (i in bestsellers) {
                if (i.id == bookId) {
                    binding.detailImage.loadImage(i.image)
                    binding.selectedBookAuthor.text = i.author
                    binding.selectedBookTitle.text = i.title
                    binding.amount.text = i.rate.amount.toString()
                    binding.score.text = i.rate.score.toString()
                }
            }

        }
        viewModel.similar.observe(
            viewLifecycleOwner
        ) { similar ->
            adapter.submitList(similar)
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

        fun bind(book: recItem) {

            binding.bookImage.loadImage(book.image)
        }

        override fun onClick(v: View) {
        }
    }

    private inner class recAdapter: ListAdapter<recItem, RecHolder>(DiffCallback()) {

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

    private class DiffCallback: DiffUtil.ItemCallback<recItem>() {

        override fun areItemsTheSame(oldItem: recItem, newItem: recItem): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: recItem, newItem: recItem): Boolean {
            return oldItem == newItem
        }
    }

}