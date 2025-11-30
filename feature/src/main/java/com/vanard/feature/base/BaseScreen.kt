package com.vanard.feature.base

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vanard.common.Screen
import com.vanard.common.UIState
import com.vanard.common.helpers.ViewModelEvent
import com.vanard.domain.model.User
import com.vanard.resources.R
import com.vanard.ui.base.ObserveEvents
import com.vanard.ui.base.SessionState
import com.vanard.ui.base.SessionViewModel

/**
 * Enhanced BaseScreen with better session handling and event support
 *
 * @param navController Navigation controller for handling navigation
 * @param requireAuth Whether authentication is required for this screen
 * @param redirectToOnboard Whether to redirect to onboard screen instead of login
 * @param showLoading Whether to show loading indicator during session check
 * @param sessionViewModel Session view model (injected by default)
 * @param snackbarHostState Snackbar host state for showing messages
 * @param onSessionExpired Custom callback for session expiration (optional)
 * @param content Content to display when authenticated/loaded
 */
@Composable
fun BaseScreen(
    navController: NavController,
    requireAuth: Boolean = false,
    redirectToOnboard: Boolean = false,
    showLoading: Boolean = true,
    sessionViewModel: SessionViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onSessionExpired: (() -> Unit)? = null,
    content: @Composable (user: User?) -> Unit
) {
    val sessionState by sessionViewModel.sessionState.collectAsState()
    val userState by sessionViewModel.userState.collectAsState()

    Log.d("BaseScreen", "BaseScreen sessionState: $sessionState")
    Log.d("BaseScreen", "BaseScreen userState: $userState")

    // Handle session state changes
    LaunchedEffect(sessionState) {
        when (sessionState) {
            is SessionState.NotAuthenticated -> {
                navController.unauthorized(
                    requireAuth = requireAuth,
                    redirectToOnboard = redirectToOnboard
                )
            }

            is SessionState.SessionExpired -> {
                onSessionExpired?.invoke()
                navController.unauthorized(
                    requireAuth = requireAuth,
                    redirectToOnboard = redirectToOnboard
                )
            }

            is SessionState.Error -> {
                // Show error if not requiring auth, otherwise redirect
                if (requireAuth) {
                    navController.unauthorized(
                        requireAuth = true,
                        redirectToOnboard = redirectToOnboard
                    )
                }
            }

            is SessionState.Authenticated -> navController.isAuthorized()

            else -> { /* Continue with normal flow */
            }
        }
    }

    // Observe events from SessionViewModel
    sessionViewModel.ObserveEvents { event ->
        when (event) {
            is ViewModelEvent.SessionEvent.SessionExpired -> {
                onSessionExpired?.invoke()
                if (requireAuth) {
                    navController.unauthorized(
                        requireAuth = true,
                        redirectToOnboard = redirectToOnboard
                    )
                }
            }

            is ViewModelEvent.SessionEvent.Unauthorized -> {
                if (requireAuth) {
                    navController.unauthorized(
                        requireAuth = true,
                        redirectToOnboard = redirectToOnboard
                    )
                }
            }

            is ViewModelEvent.SessionEvent.LoggedOut -> {
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            }

            // Snackbar messages would need to be handled outside this callback
            // since we can't use LaunchedEffect here

            else -> { /* Handle other events if needed */
            }
        }
    }

    // Show loading or content based on state
    when {
        showLoading && sessionState is SessionState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colorResource(R.color.paint_01)
                )
            }
        }

        else -> {
            val currentUser = when (val state = userState) {
                is UIState.Success -> state.data
                else -> null
            }

            Log.d("BaseScreen", "BaseScreen: $currentUser")

            content(currentUser)
        }
    }
}

/**
 * Navigate to login/onboard when unauthorized
 */
private fun NavController.unauthorized(requireAuth: Boolean, redirectToOnboard: Boolean) {
    if (requireAuth) {
        val destination =
            if (redirectToOnboard) Screen.Onboard.route else Screen.Login.route

        // Only navigate if we're not already at the destination
        navigateToDestination(destination)
    }
}

private fun NavController.isAuthorized() {
    val destination = Screen.Home.route
//    navigateToDestination(destination)

    if (currentDestination?.route in unauthorizedScreen) {
        this.navigate(destination) {
            popUpTo(0) { inclusive = true }
        }
    }
}

private val authorizedScreen = listOf(Screen.Home.route, Screen.Wishlist.route, Screen.Cart.route, Screen.Profile.route, Screen.Detail.route)
private val unauthorizedScreen = listOf(Screen.Onboard.route, Screen.Login.route, Screen.SignUp.route)

private fun NavController.navigateToDestination(destination: String) {
    if (currentDestination?.route != destination) {
        this.navigate(destination) {
            popUpTo(0) { inclusive = true }
        }
    }
}

/**
 * Extension function to get SessionViewModel easily
 */
@Composable
fun rememberSessionViewModel(): SessionViewModel = hiltViewModel()