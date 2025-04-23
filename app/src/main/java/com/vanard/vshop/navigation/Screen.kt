package com.vanard.vshop.navigation

sealed class Screen(val route: String) {
    object Onboard: Screen("onboard")
    object Home: Screen("home")
    object Wishlist: Screen("wishlist")
    object Cart: Screen("cart")
    object Profile: Screen("profile")
    object Detail: Screen("home/{id}"){
        fun detailRoute(id: Long) = "home/$id"
    }
}