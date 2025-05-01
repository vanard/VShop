package com.vanard.vshop.persentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vanard.vshop.navigation.getAllNavigationItem

@Composable
fun rememberVShopState(navController: NavHostController = rememberNavController()) = remember(navController) {
    VShopState(navController)
}

@Stable
class VShopState(val navController: NavHostController) {
    val bottomBarTabs @Composable get() = getAllNavigationItem()
    private val bottomBarRoutes @Composable get() = bottomBarTabs.map { it.screen.route }

    // Reading this attribute will cause recompositions when the bottom bar needs shown, or not.
    // Not all routes need to show the bottom bar.
    val shouldShowBottomBar: Boolean
        @Composable get() =
            navController
                .currentBackStackEntryAsState()
                .value
                ?.destination
                ?.route in bottomBarRoutes


    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun navigateToBottomBarRoute(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                // Pop up backstack to the first destination and save state. This makes going back
                // to the start destination when pressing back in any other bottom tab.
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }
}

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

/**
 * Copied from similar function in NavigationUI.kt
 *
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-ui/src/main/java/androidx/navigation/ui/NavigationUI.kt
 */
private tailrec fun findStartDestination(graph: NavDestination): NavDestination =
    if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph