package com.otus.otuskotlin.stocktrack.model

data class Debug(
    val mode: Mode,
    val stub: Stub
) {
    enum class Mode {
        PROD, TEST, STUB, NONE
    }

    enum class Stub {
        SUCCESS,
        NOT_FOUND,
        BAD_REQUEST,
        DATABASE_ERROR,
        NONE
    }

    companion object {
        val NONE = Debug(mode = Mode.NONE, stub = Stub.NONE)
    }
}