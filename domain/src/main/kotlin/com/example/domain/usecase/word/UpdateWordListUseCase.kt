package com.example.domain.usecase.word

import com.example.domain.repo.SearchWordRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateWordListUseCase @Inject constructor(private val searchWordRepository: SearchWordRepository) {
    operator fun invoke() = flow {
        emit(searchWordRepository.updateExcelList())
    }
}