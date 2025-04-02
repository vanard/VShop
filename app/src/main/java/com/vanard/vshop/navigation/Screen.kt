package com.vanard.vshop.navigation

sealed class Screen(val route: String) {
    data object Onboard: Screen("onboard")
    data object Home: Screen("home")
    data object Whishlist: Screen("whishlist")
    data object Cart: Screen("cart")
    data object Profile: Screen("profile")
    data object Detail: Screen("detail")
}