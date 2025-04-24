package com.vanard.common.util

fun String.firstWord(): String = this.split(" ").firstOrNull() ?: ""

fun String.firstWords(count: Int): String =
    this.split(" ").take(count).joinToString(" ")

fun String.maxDecimal(count: Int, number: Double): String =
    this.format("%.${count}f", number)