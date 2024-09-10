package com.example.data.network.response

import com.example.presentation.ui.adapter.WordItem

data class ExcelResponse(
    val word: String,
    val mean: String
) {
    fun toWordItem(): WordItem =
        WordItem(word, mean)
}