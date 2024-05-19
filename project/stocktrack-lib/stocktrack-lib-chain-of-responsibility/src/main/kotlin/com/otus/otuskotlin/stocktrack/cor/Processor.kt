package com.otus.otuskotlin.stocktrack.cor

interface Processor<T>: Executor<T>

class ProcessorImpl<T>(
    private val process: (T) -> T,
    private val invokeOn: (T) -> Boolean,
    private val handleException: (Throwable, T) -> Unit
): Processor<T> {

    override fun execute(context: T): T {
        return try {
            context.takeIf { invokeOn(it) }
                ?.let { process(it) }
                ?: context
        } catch (throwable: Throwable) {
            context.also { handleException(throwable, it) }
        }
    }
}