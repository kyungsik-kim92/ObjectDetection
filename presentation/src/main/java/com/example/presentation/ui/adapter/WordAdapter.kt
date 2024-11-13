package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.model.WordItem
import com.example.presentation.databinding.ItemWordBinding

class WordAdapter(
    private val onItemClick: (WordItem) -> Unit
) : ListAdapter<WordItem, WordViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WordViewHolder {
        val binding =
            ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<WordItem>() {
            override fun areItemsTheSame(oldItem: WordItem, newItem: WordItem): Boolean =
                oldItem === newItem


            override fun areContentsTheSame(oldItem: WordItem, newItem: WordItem): Boolean =
                oldItem.word == newItem.word
        }
    }
}


class WordViewHolder(
    private val binding: ItemWordBinding,
    private val onItemClick: (WordItem) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WordItem) {
        binding.item = item

        itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}
