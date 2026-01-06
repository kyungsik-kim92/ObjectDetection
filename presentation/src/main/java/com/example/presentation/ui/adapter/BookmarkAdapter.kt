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
        android.util.Log.d("BookmarkAdapter", "onBindViewHolder: position=$position")
        holder.bind(getItem(position), onDelete, onItemClick, toggleMean)
    }


    fun toggleMean(isShow: Boolean) {
        if (toggleMean != isShow) {
            toggleMean = isShow
            notifyDataSetChanged()
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
        onDelete: (BookmarkWord) -> Unit,
        onItemClick: (BookmarkWord) -> Unit,
        toggleMean: Boolean
    ) {
        android.util.Log.d("BookmarkAdapter", "bind: ${item.word}")
        binding.item = item.toWordItem()

        itemView.setOnClickListener {
            onItemClick(item)
        }
        binding.mean.isVisible = toggleMean
        binding.deleteBookmark.setOnClickListener {
            onDelete(item)
        }
    }
}



