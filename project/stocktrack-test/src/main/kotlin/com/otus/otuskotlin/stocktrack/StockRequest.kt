package com.otus.otuskotlin.stocktrack

enum class StockRequest(val value: String) {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete"),
    SEARCH("search"),
    FIND("find")
}