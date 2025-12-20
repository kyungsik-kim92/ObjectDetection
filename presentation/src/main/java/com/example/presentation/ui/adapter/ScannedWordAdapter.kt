package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.databinding.ItemScannedWordBinding

class ScannedWordAdapter(private val onWordClick: (String) -> Unit) :
    ListAdapter<String, ScannedWordAdapter.RecognizedWordViewHolder>(WordDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecognizedWordViewHolder {
        val binding = ItemScannedWordBinding.inflate(
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
        private val binding: ItemScannedWordBinding
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