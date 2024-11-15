package com.example.domain.usecase.word

import com.example.domain.repo.SearchWordRepository
import com.example.model.common.Result
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchWordUseCase @Inject constructor(private val searchWordRepository: SearchWordRepository) {
    operator fun invoke(keyword: String) = flow {
        when (val result = searchWordRepository.searchMeanWord(keyword)) {
            is Result.Error -> {
                emit(null)
            }

            is Result.Success -> {
                emit(result.data[0])
            }
        }
        searchWordRepository.searchMeanWord(keyword)
    }
}