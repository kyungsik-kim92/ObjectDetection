package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.databinding.ItemScannedWordBinding

class ScannedWordAdapter(private val onWordClick: (String) -> Unit) :
    ListAdapter<String, ScannedWordViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannedWordViewHolder {
        val binding = ItemScannedWordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ScannedWordViewHolder(binding, onWordClick)
    }

    override fun onBindViewHolder(holder: ScannedWordViewHolder, position: Int) {
        holder.bind(getItem(position), onWordClick)
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem


            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }

    }
}

class ScannedWordViewHolder(
    private val binding: ItemScannedWordBinding,
    onWordClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(word: String, onWordClick: (String) -> Unit) {
        binding.tvWord.text = word
        binding.root.setOnClickListener {
            onWordClick(word)
        }
    }
}