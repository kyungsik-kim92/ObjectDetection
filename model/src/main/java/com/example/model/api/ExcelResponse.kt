package com.example.model.api

import com.example.model.WordItem

data class ExcelResponse(
    val word: String,
    val mean: String
) {
    fun toWordItem(): WordItem =
        WordItem(word, mean)
}