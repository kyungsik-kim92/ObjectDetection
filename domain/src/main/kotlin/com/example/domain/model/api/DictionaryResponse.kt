package com.example.domain.model.api

import kotlinx.serialization.Serializable

@Serializable
data class DictionaryResponse(
    val items: List<DictionaryResponseItem> = emptyList()
)

@Serializable
data class DictionaryResponseItem(
    val meanings: List<Meaning> = emptyList(),
    val origin: String = "",
    val phonetic: String = "",
    val phonetics: List<Phonetic> = emptyList(),
    val word: String = ""
)

@Serializable
data class Meaning(
    val definitions: List<Definition> = emptyList(),
    val partOfSpeech: String = ""
)

@Serializable
data class Phonetic(
    val audio: String = "",
    val text: String = ""
)

@Serializable
data class Definition(
    val antonyms: List<String> = emptyList(),
    val definition: String = "",
    val example: String = "",
    val synonyms: List<String> = emptyList()
)