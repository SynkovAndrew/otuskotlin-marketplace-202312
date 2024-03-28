package com.otus.otuskotlin.stocktrack.model

import com.otus.otuskotlin.stocktrack.stub.Stub

data class Debug(
    val mode: Mode,
    val stub: Stub
) {
    enum class Mode {
        PROD, TEST, STUB, NONE
    }
}