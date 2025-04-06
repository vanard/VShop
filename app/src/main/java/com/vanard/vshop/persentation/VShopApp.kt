package com.vanard.vshop.persentation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vanard.vshop.R
import com.vanard.vshop.navigation.NavItem
import com.vanard.vshop.navigation.Screen
import com.vanard.vshop.navigation.getAllNavigationItem
import com.vanard.vshop.persentation.screens.HomeScreen
import com.vanard.vshop.persentation.ui.theme.VShopTheme

@Composable
fun VShopApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Detail.route && currentRoute != Screen.Onboard.route) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Onboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Onboard.route) {
                OnboardScreen(navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboard.route) {
                            inclusive = true
                        }
                    }
                })
            }
            composable(Screen.Home.route) {
//                HomeScreen(
//                    navigateToDetail = { rewardId ->
//                        navController.navigate(Screen.DetailReward.createRoute(rewardId))
//                    }
//                )
                HomeScreen()
            }
            composable(Screen.Whishlist.route) {
                HomeScreen()
            }
            composable(Screen.Cart.route) {
                val context = LocalContext.current
//                CartScreen(
//                    onOrderButtonClicked = { message ->
//                        shareOrder(context, message)
//                    }
//                )
                HomeScreen()
            }
            composable(Screen.Profile.route) {
//                ProfileScreen()
                HomeScreen()
            }
//            composable(
//                route = Screen.Detail.route,
//                arguments = listOf(navArgument("rewardId") { type = NavType.LongType }),
//            ) {
//                val id = it.arguments?.getLong("rewardId") ?: -1L
//                DetailScreen(
//                    rewardId = id,
//                    navigateBack = {
//                        navController.navigateUp()
//                    },
//                    navigateToCart = {
//                        navController.popBackStack()
//                        navController.navigate(Screen.Cart.route) {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    }
//                )
//            }
        }
    }
}

//private fun shareOrder(context: Context, summary: String) {
//    val intent = Intent(Intent.ACTION_SEND).apply {
//        type = "text/plain"
//        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.dicoding_reward))
//        putExtra(Intent.EXTRA_TEXT, summary)
//    }
//
//    context.startActivity(
//        Intent.createChooser(
//            intent,
//            context.getString(R.string.dicoding_reward)
//        )
//    )
//}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = colorResource(R.color.paint_05),
        modifier = modifier,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
//        val navigationItems = listOf(
//            NavItem(
//                icon = painterResource(R.drawable.home_2),
//                selectedIcon = painterResource(R.drawable.home_2_bold),
//                screen = Screen.Home
//            ),
//            NavItem(
//                icon = painterResource(R.drawable.heart),
//                selectedIcon = painterResource(R.drawable.heart_bold),
//                screen = Screen.Whishlist
//            ),
//            NavItem(
//                icon = painterResource(R.drawable.shopping_bag),
//                selectedIcon = painterResource(R.drawable.shopping_bag_bold),
//                screen = Screen.Cart
//            ),
//            NavItem(
//                icon = painterResource(R.drawable.profile),
//                selectedIcon = painterResource(R.drawable.profile_bulk),
//                screen = Screen.Profile
//            ),
//        )

        getAllNavigationItem().map { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = if (currentRoute == item.screen.route) item.selectedIcon else item.icon,
//                        tint = colorResource(R.color.paint_04),
                        contentDescription = null
                    )
                },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
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

@Preview(showBackground = true)
@Composable
fun VShopAppPreview() {
    VShopTheme {
        VShopApp()
    }
}