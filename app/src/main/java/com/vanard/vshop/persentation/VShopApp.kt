package com.vanard.vshop.persentation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vanard.common.Screen
import com.vanard.feature.cart.CartScreen
import com.vanard.feature.detail.DetailScreen
import com.vanard.feature.home.HomeScreen
import com.vanard.feature.profile.ProfileScreen
import com.vanard.feature.wishlist.WishlistScreen
import com.vanard.resources.R
import com.vanard.ui.theme.VShopTheme
import com.vanard.vshop.navigation.getAllNavigationItem
import com.vanard.vshop.persentation.onboard.OnboardScreen

@Composable
fun VShopApp(
    appState: VShopState = rememberVShopState(),
    modifier: Modifier = Modifier,
) {
//    val appState = rememberVShopState()

    Scaffold(
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                VShopBottomBar(
                    currentRoute = appState.currentRoute!!,
                    navigateToRoute = appState::navigateToBottomBarRoute,
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->

        NavHost(
            navController = appState.navController,
            startDestination = Screen.Onboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            onboardScreen(appState.navController)
            navigationScreens(appState.navController)
            detailScreens(appState.navController)
        }
    }
}

@Composable
private fun VShopBottomBar(
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = colorResource(R.color.paint_05),
        modifier = modifier,
    ) {
        getAllNavigationItem().map { item ->
            val selected = currentRoute == item.screen.route
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = if (selected) item.selectedIcon else item.icon,
                        contentDescription = null
                    )
                },
                selected = selected,
                onClick = {
                   navigateToRoute(item.screen.route)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(R.color.paint_04),
                    selectedTextColor = colorResource(R.color.paint_04),
                    indicatorColor = Color.Transparent,
                )
            )
        }
    }
}

private fun NavGraphBuilder.onboardScreen(navController: NavController) {
    composable(route = Screen.Onboard.route) {
        OnboardScreen(
            navController = navController
        )
    }
}

private fun NavGraphBuilder.navigationScreens(navController: NavController) {
    composable(Screen.Home.route) {
        HomeScreen(navController = navController)
    }
    composable(Screen.Wishlist.route) {
        WishlistScreen(navController = navController)
    }
    composable(Screen.Cart.route) {
        CartScreen(navController = navController)
    }
    composable(Screen.Profile.route) {
        ProfileScreen(navController = navController)
    }
}

private fun NavGraphBuilder.detailScreens(navController: NavController) {
    composable(
        route = Screen.Detail.route,
        arguments = listOf(navArgument("id") { type = NavType.LongType }),
    ) {
        val id = it.arguments?.getLong("id") ?: -1L
        Log.d(TAG, "detail: $id")

        DetailScreen(
            productId = id,
            navController = navController
        )
    }
}

fun NavController.navigateTo(route: String, from: String): () -> Unit = {
    this.navigate(route) {
        popUpTo(from) {
            inclusive = true
        }
    }
}

fun NavController.navigateToSaveState(route: String): () -> Unit = {
    this.navigate(route) {
        popUpTo(this@navigateToSaveState.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Preview(showBackground = true)
@Composable
fun VShopAppPreview() {
    VShopTheme {
        VShopApp()
    }
}

const val TAG = "VShopApp"