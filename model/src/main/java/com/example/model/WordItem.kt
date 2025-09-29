package com.example.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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