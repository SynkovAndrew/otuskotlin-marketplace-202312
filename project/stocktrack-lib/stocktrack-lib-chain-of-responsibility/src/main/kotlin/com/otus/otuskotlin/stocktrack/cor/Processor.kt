package com.otus.otuskotlin.stocktrack.cor

interface Processor<T>: Executor<T>

class ProcessorImpl<T>(
    private val name: String,
    private val process: (T) -> T,
    private val invokeOn: (T) -> Boolean,
    private val handleException: (Throwable, T) -> T
): Processor<T> {

    override suspend fun execute(context: T): T {
        return try {
            context.takeIf { invokeOn(context) }
                ?.let {
                    println("\"$name\" executing ...")
                    process(context)
                        .also { println("\"$name\" executed") }
                }
                ?: context
        } catch (throwable: Throwable) {
            println("\"$name\" failed")
            handleException(throwable, context)
        }
    }

    override fun invokeOn(context: T): Boolean = this.invokeOn.invoke(context)
}