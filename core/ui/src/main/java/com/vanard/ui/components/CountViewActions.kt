package com.vanard.ui.components

data class CountViewActions(
    val onPlusClick: (Int) -> Unit,
    val onMinusClick: (Int) -> Unit,
)