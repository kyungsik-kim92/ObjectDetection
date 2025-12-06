package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.databinding.ItemRecognizedWordBinding

class RecognizedWordAdapter(private val onWordClick: (String) -> Unit) :
    ListAdapter<String, RecognizedWordAdapter.RecognizedWordViewHolder>(WordDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecognizedWordViewHolder {
        val binding = ItemRecognizedWordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecognizedWordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecognizedWordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RecognizedWordViewHolder(
        private val binding: ItemRecognizedWordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(word: String) {
            binding.tvWord.text = word
            binding.root.setOnClickListener {
                onWordClick(word)
            }
        }
    }

    private class WordDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}