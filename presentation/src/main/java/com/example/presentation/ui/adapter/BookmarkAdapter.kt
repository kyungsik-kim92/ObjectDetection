package com.example.presentation.ui.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.BookmarkWord
import com.example.presentation.databinding.ItemWordBinding
import kotlinx.parcelize.Parcelize

class BookmarkAdapter(private val onDelete: (BookmarkWord) -> Unit) :
    RecyclerView.Adapter<BookmarkViewHolder>() {

    private val bookmarkList = mutableListOf<BookmarkWord>()

    private var toggleMean: Boolean = true


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkViewHolder {
        val binding =
            ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(bookmarkList[position], onDelete, toggleMean)
    }

    override fun getItemCount(): Int =
        bookmarkList.size

    fun addAll(list: List<BookmarkWord>) {
        bookmarkList.clear()
        bookmarkList.addAll(list)
        notifyDataSetChanged()
    }

    fun delete(item: BookmarkWord) {
        if (bookmarkList.contains(item)) {
            val index = bookmarkList.indexOf(item)
            bookmarkList.removeAt(index)
            notifyItemChanged(index)
        }
    }

    fun add(item: BookmarkWord) {
        bookmarkList.add(item)
        notifyItemChanged(bookmarkList.lastIndex)
    }


    fun toggleMean(isShow: Boolean) {
        toggleMean = isShow
        notifyDataSetChanged()
    }
}

class BookmarkViewHolder(private val binding: ItemWordBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: BookmarkWord,
        onDelete: (BookmarkWord) -> Unit,
        toggleMean: Boolean
    ) {
        binding.item = item.foWordItem()

        itemView.setOnClickListener {
            onDelete(item)
        }

        binding.mean.isVisible = toggleMean
    }
}

@Parcelize
data class WordItem(
    val word: String,
    val mean: String
) : Parcelable {
    fun toBookmarkWord(): BookmarkWord =
        BookmarkWord(
            word = word,
            mean = mean
        )

}
