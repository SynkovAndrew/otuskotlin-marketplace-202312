package com.otus.otuskotlin.marketplace.hw.hard

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.otus.otuskotlin.marketplace.hw.hard.dto.Dictionary
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.Response

class DictionaryApi(
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
) {

    fun findWord(locale: Locale, word: String): Deferred<Dictionary> { // make something with context
        return CoroutineScope(Dispatchers.IO)
            .async {
                val url = "$DICTIONARY_API/${locale.code}/$word"
                println("Searching: $word on thread: ${Thread.currentThread().name}")

                try {
                    load(url, word)
                } catch (ex: Throwable)  {
                    Dictionary("NOT_FOUND", listOf())
                }
            }
    }

    private fun load(url: String, word: String): Dictionary {
       return getBody(HttpClient.get(url).execute())?.firstOrNull()
            ?: throw IllegalStateException("\"$word\" not found")
    }


    private fun getBody(response: Response): List<Dictionary>? {
        if (!response.isSuccessful) {
            return emptyList()
        }

        return response.body?.let {
            objectMapper.readValue(it.string())
        }
    }
}

internal const val DICTIONARY_API = "https://api.dictionaryapi.dev/api/v2/entries"