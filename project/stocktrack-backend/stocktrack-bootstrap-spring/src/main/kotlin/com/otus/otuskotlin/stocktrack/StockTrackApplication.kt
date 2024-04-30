package com.otus.otuskotlin.stocktrack

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StockTrackApplication

fun main(args: Array<String>) {
    runApplication<StockTrackApplication>(*args)
}
