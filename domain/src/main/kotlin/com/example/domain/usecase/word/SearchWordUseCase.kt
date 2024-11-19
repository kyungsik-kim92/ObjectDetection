package com.example.domain.usecase.word

import com.example.domain.repo.SearchWordRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchWordUseCase @Inject constructor(
    private val searchWordRepository: SearchWordRepository
) {
    operator fun invoke(keyword: String) = flow {
        emit(searchWordRepository.searchMeanWord(keyword))
    }
}