package com.otus.otuskotlin.stocktrack.cor

interface Chain<T> : Executor<T>

class ChainImpl<T>(
    private val invokeOn: (T) -> Boolean,
    private val executors: List<Executor<T>>
) : Chain<T> {

    override suspend fun execute(context: T): T {
        return executors
            .fold(context) { acc, executor ->
                acc.takeIf { invokeOn(acc) }
                    ?.let { executor.execute(acc)  }
                    ?: acc
            }
    }

    override fun invokeOn(context: T): Boolean = this.invokeOn.invoke(context)

}