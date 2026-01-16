package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.model.BookmarkWord
import com.example.presentation.databinding.ItemWordBinding

class BookmarkAdapter(
    private val onDelete: (BookmarkWord) -> Unit,
    private val onItemClick: (BookmarkWord) -> Unit
) : ListAdapter<BookmarkWord, BookmarkViewHolder>(diffUtil) {

    private var toggleMean: Boolean = true

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkViewHolder {
        val binding =
            ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding, onDelete, onItemClick)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(getItem(position), toggleMean)
    }


    fun toggleMean(isShow: Boolean) {
        if (toggleMean != isShow) {
            toggleMean = isShow
            notifyItemRangeChanged(0, itemCount)
        }
    }

    fun delete(item: BookmarkWord) {
        val newList = currentList.toMutableList()
        newList.remove(item)
        submitList(newList)
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<BookmarkWord>() {
            override fun areItemsTheSame(oldItem: BookmarkWord, newItem: BookmarkWord): Boolean =
                oldItem.word == newItem.word


            override fun areContentsTheSame(oldItem: BookmarkWord, newItem: BookmarkWord): Boolean =
                oldItem == newItem
        }
    }
}

class BookmarkViewHolder(
    private val binding: ItemWordBinding,
    private val onDelete: (BookmarkWord) -> Unit,
    private val onItemClick: (BookmarkWord) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: BookmarkWord,
        toggleMean: Boolean
    ) {
        binding.item = item.toWordItem()
        binding.mean.isVisible = toggleMean

        itemView.setOnClickListener {
            onItemClick(item)
        }
        binding.deleteBookmark.setOnClickListener {
            onDelete(item)
        }
    }
}



