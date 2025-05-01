package com.vanard.vshop.persentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vanard.ui.theme.VShopTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var navControllerProvider: (() -> NavHostController)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            VShopTheme {
                val navController = navControllerProvider?.invoke() ?: rememberNavController()
                val appState = remember(navController) {
                    VShopState(navController)
                }
                VShopApp(appState)
            }
        }
    }
}
