package com.vanard.vshop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.vanard.resources.R

data class NavItem(
//    val title: String,
    val icon: Painter,
    val selectedIcon: Painter,
    val screen: Screen
)

@Composable
fun getAllNavigationItem(): List<NavItem> {
    return listOf(
        NavItem(
            icon = painterResource(R.drawable.home_2),
            selectedIcon = painterResource(R.drawable.home_2_bold),
            screen = Screen.Home
        ),
        NavItem(
            icon = painterResource(R.drawable.heart),
            selectedIcon = painterResource(R.drawable.heart_bold),
            screen = Screen.Wishlist
        ),
        NavItem(
            icon = painterResource(R.drawable.shopping_bag),
            selectedIcon = painterResource(R.drawable.shopping_bag_bold),
            screen = Screen.Cart
        ),
        NavItem(
            icon = painterResource(R.drawable.profile),
            selectedIcon = painterResource(R.drawable.profile_bulk),
            screen = Screen.Profile
        ),
    )
}
