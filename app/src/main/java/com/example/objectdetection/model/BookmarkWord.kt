package com.example.objectdetection.model

import java.util.Calendar

data class BookmarkWord(
    val word: String,
    val mean: String,
    var year: String = Calendar.getInstance().get(Calendar.YEAR).toString(),
    var month: String = (Calendar.getInstance().get(Calendar.MONTH) + 1).toString(),
    var day: String = (Calendar.getInstance().get(Calendar.DATE)).toString()
) {
//    fun foWordItem(): WordItem =
//        WordItem(word, mean)

}

fun HashMap<String,String>.toBookmarkWord() : BookmarkWord =
    BookmarkWord(
        word = getValue("word"),
        mean = getValue("mean"),
        year = getValue("year"),
        month = getValue("month"),
        day = getValue("day")
    )