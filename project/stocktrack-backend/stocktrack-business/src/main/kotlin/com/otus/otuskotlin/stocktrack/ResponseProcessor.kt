package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.Context


interface ResponseProcessor <C : Context<*, *, C>>{

    suspend fun execute(context: C): C
}
