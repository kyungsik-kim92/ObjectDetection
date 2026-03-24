package com.example.domain.model

import kotlinx.serialization.Serializable
import java.util.Calendar

@Serializable
data class BookmarkWord(
    val word: String,
    val mean: String,
    val year: String = Calendar.getInstance().get(Calendar.YEAR).toString(),
    val month: String = (Calendar.getInstance().get(Calendar.MONTH) + 1).toString(),
    val day: String = (Calendar.getInstance().get(Calendar.DATE)).toString()
) {
    fun toWordItem(): WordItem =
        WordItem(word, mean)

}

fun HashMap<String, String>.toBookmarkWord(): BookmarkWord =
    BookmarkWord(
        word = getValue("word"),
        mean = getValue("mean"),
        year = getValue("year"),
        month = getValue("month"),
        day = getValue("day")
    )