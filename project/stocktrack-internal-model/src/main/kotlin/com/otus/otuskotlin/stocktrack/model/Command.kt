package com.otus.otuskotlin.stocktrack.model

enum class Command(val value: String) {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete"),
    FIND("find"),
    SEARCH("search")
}