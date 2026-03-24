package com.example.domain.model.api

import kotlinx.serialization.Serializable

@Serializable
data class ExcelResponse(
    val word: String,
    val mean: String
)