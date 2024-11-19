package com.example.data.network

import com.example.model.api.DictionaryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    companion object {
        private const val URL_DICTIONARY = "api/v2/entries/en/{word}"
    }

    @GET(URL_DICTIONARY)
    suspend fun getDictionaryMean(
        @Path("word") word: String
    ): DictionaryResponse


}