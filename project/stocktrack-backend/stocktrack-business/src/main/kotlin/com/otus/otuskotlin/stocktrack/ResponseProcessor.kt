package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.Context

interface ResponseProcessor <I, O, C : Context<I, O>>{

    suspend fun execute(context: C): C
}
