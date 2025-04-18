package com.vanard.common.util

fun String.firstWord(): String = this.split(" ").firstOrNull() ?: ""

fun String.firstWords(count: Int): String =
    this.split(" ").take(count).joinToString(" ")