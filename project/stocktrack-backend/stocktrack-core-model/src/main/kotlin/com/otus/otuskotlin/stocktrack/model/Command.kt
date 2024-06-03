package com.otus.otuskotlin.stocktrack.model

enum class Command(val value: String) {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete"),
    FIND("find"),
    SEARCH("search"),
    FIND_SNAPSHOTS("find_snapshots"),
    PREDICT_SNAPSHOTS("predict_snapshots"),
    UPLOAD_SNAPSHOTS("upload_snapshots")
}