package com.otus.otuskotlin.stocktrack.cor

interface Chain<T>: Executor<T>

class ChainImpl<T>(
    private val processors: List<Processor<T>>
): Chain<T> {

    override fun execute(context: T): T {
        return processors.fold(context) { acc, processor -> processor.execute(acc) }
    }
}