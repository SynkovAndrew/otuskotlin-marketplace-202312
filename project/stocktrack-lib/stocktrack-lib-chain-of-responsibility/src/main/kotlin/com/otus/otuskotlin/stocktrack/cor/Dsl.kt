package com.otus.otuskotlin.stocktrack.cor

@ChainDslMarker
interface ProcessorDsl<T> {
    var name: String

    fun invokeOn(function: (T) -> Boolean)

    fun process(function: (T) -> T)

    fun handleException(function: (Throwable, T) -> T)

    fun build(): Processor<T>
}

@ChainDslMarker
interface ChainDsl<T> {

    fun processor(processor: ProcessorDsl<T>.() -> Unit)

    fun build(): Chain<T>
}

class ProcessorDslImpl<T>(override var name: String = "") : ProcessorDsl<T> {
    private var invokeOn: (T) -> Boolean = { true }
    private var process: (T) -> T = { it }
    private var handleException: (Throwable, T) -> T = { throwable, _ -> throw throwable }

    override fun invokeOn(function: (T) -> Boolean) {
        this.invokeOn = function
    }

    override fun process(function: (T) -> T) {
        this.process = function
    }

    override fun handleException(function: (Throwable, T) -> T) {
        this.handleException = function
    }

    override fun build(): Processor<T> {
        return ProcessorImpl(
            name = name,
            process = process,
            invokeOn = invokeOn,
            handleException = handleException
        )
    }
}

class ChainDslImpl<T> : ChainDsl<T> {
    private var processors: MutableList<ProcessorDsl<T>> = mutableListOf()

    override fun processor(processor: ProcessorDsl<T>.() -> Unit) {
        processors.add(ProcessorDslImpl<T>().apply(processor))
    }

    override fun build(): Chain<T> {
        return ChainImpl(processors = processors.map { it.build() }.toList())
    }
}

fun <T> chainBuilder(function: ChainDsl<T>.() -> Unit): Chain<T> {
    return ChainDslImpl<T>().apply(function).build()
}

@DslMarker
annotation class ChainDslMarker