package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.model.BookmarkWord
import com.example.presentation.databinding.ItemWordBinding

class BookmarkAdapter(
    private val onDelete: (BookmarkWord) -> Unit,
    private val onItemClick: (BookmarkWord) -> Unit
) : RecyclerView.Adapter<BookmarkViewHolder>() {
    private val bookmarkList = mutableListOf<BookmarkWord>()

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
        holder.bind(bookmarkList[position], onDelete, onItemClick, toggleMean)
    }

    override fun getItemCount(): Int =
        bookmarkList.size

    fun submitList(list: List<BookmarkWord>) {
        bookmarkList.clear()
        bookmarkList.addAll(list)
        notifyDataSetChanged()
    }

    fun toggleMean(isShow: Boolean) {
        if (toggleMean != isShow) {
            toggleMean = isShow
            notifyDataSetChanged()
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



