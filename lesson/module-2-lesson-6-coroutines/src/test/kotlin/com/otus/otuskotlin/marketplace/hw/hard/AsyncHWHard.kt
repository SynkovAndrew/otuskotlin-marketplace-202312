package com.otus.otuskotlin.marketplace.hw.hard

import com.otus.otuskotlin.marketplace.hw.hard.dto.Dictionary
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.test.Test

class AsyncHWHard {
    private val singleThreadContext = newSingleThreadContext("single-thread")
    private val threadPoolContext = newFixedThreadPoolContext(10, "thread-pool")
    private val anotherThreadPoolContext = newFixedThreadPoolContext(10, "thread-pool")

    @Test
    fun hardHw() {
        val dictionaryApi = DictionaryApi()
        val words = FileReader.readFile().split(" ", "\n").toSet()

        runBlocking {
            CoroutineScope(Dispatchers.Default).launch(SupervisorJob() + errorHandler()) {
                findWords(dictionaryApi, words, Locale.EN)
                    .map { dictionary -> printWord(dictionary) }
                    .joinAll()
            }.join()
        }
    }

    private fun printWord(dictionary: Dictionary): Job {
         return CoroutineScope(anotherThreadPoolContext).launch {
            print("For \"${dictionary.word}\": ")
            println(
                dictionary.meanings
                    .mapNotNull { definition ->
                        val r = definition.definitions
                            .mapNotNull { it.example.takeIf { it?.isNotBlank() == true } }
                            .takeIf { it.isNotEmpty() }
                        r
                    }
                    .takeIf { it.isNotEmpty() }
            )
        }
    }

    private suspend fun findWords(
        dictionaryApi: DictionaryApi,
        words: Set<String>,
        @Suppress("SameParameterValue") locale: Locale
    ): List<Dictionary> {
        // make some suspensions and async
        return words
            .map { dictionaryApi.findWord(locale, it) }
            .awaitAll()
    }

    object FileReader {
        fun readFile(): String {
            return File(
                this::class.java.classLoader.getResource("words.txt")?.toURI()
                    ?: throw RuntimeException("Can't read file")
            ).readText()
        }
    }

    private fun errorHandler() = CoroutineExceptionHandler { context, exception ->
        println("CAUGHT at ${context[CoroutineName]}: $exception")
    }
}