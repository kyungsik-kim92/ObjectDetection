package com.example.presentation.ui.search.word

import com.example.model.WordItem
import com.example.presentation.base.ViewState

data class WordContentViewState(
    val searchList: List<WordItem> = emptyList(),
) : ViewState