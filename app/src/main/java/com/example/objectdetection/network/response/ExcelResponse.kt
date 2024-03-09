package com.example.objectdetection.network.response

import com.example.objectdetection.ui.adapter.WordItem

data class ExcelResponse(
    val word: String,
    val mean: String
) {
    fun toWordItem(): WordItem =
        WordItem(word, mean)
}