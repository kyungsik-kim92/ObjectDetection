package com.example.presentation.ui.search.word

import com.example.model.api.DictionaryResponseItem
import com.example.presentation.base.ViewState

data class WordDetailViewState(
    val item: DictionaryResponseItem? = null,
    val isBookmark: Boolean = false,
    val isLoading: Boolean = false,
) : ViewState