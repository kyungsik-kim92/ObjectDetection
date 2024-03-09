package com.example.objectdetection.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.objectdetection.databinding.ItemWordBinding

class WordAdapter : RecyclerView.Adapter<WordViewHolder>() {

    private val wordList = mutableListOf<WordItem>()

    private lateinit var onClick: (WordItem) -> Unit

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WordViewHolder {
        val binding =
            ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(wordList[position], onClick)
    }

    override fun getItemCount(): Int =
        wordList.size

    fun addAll(list: List<WordItem>) {
        wordList.clear()
        wordList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (WordItem) -> Unit) {
        onClick = listener
    }
}


class WordViewHolder(private val binding: ItemWordBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WordItem, onClick: (WordItem) -> Unit) {
        binding.item = item

        itemView.setOnClickListener {
            onClick(item)
        }
    }
}
