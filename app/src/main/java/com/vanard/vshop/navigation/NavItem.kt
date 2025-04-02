package com.vanard.vshop.navigation

import androidx.compose.ui.graphics.painter.Painter

data class NavItem(
//    val title: String,
    val icon: Painter,
    val selectedIcon: Painter,
    val screen: Screen
)
