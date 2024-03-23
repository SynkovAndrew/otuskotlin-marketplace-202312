package com.otus.otuskotlin.lesson.hw.easy

fun findNumberInList(toFind: Int, numbers: List<Int>): Int {
    Thread.sleep(2000L)
    return numbers.firstOrNull { it == toFind } ?: -1
}