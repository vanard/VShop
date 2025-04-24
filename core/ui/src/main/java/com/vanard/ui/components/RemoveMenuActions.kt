package com.vanard.ui.components

data class RemoveMenuActions(
    val onMenuClick: () -> Unit,
    val showMenu: Boolean,
    val onDismissMenu: () -> Unit,
    val onRemoveItem: () -> Unit,
)