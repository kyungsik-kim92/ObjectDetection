package com.example.data.network.response

class DictionaryResponse : ArrayList<DictionaryResponseItem>()

data class DictionaryResponseItem(
    var meanings: List<Meaning> = emptyList(),
    var origin: String = "",
    var phonetic: String = "",
    var phonetics: List<Phonetic> = emptyList(),
    var word: String = ""
)

data class Meaning(
    var definitions: List<Definition> = emptyList(),
    var partOfSpeech: String = ""
)

data class Phonetic(
    var audio: String = "",
    var text: String = ""
)

data class Definition(
    var antonyms: List<Any> = emptyList(),
    var definition: String = "",
    var example: String = "",
    var synonyms: List<Any> = emptyList()
)