package com.vanard.ui.base

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vanard.common.UIState
import com.vanard.common.helpers.ViewModelEvent
import com.vanard.resources.R
import kotlinx.coroutines.flow.StateFlow

/**
 * Composable helpers for common UI patterns
 */

/**
 * Render content based on UIState with loading, error, and success states
 */
@Composable
fun <T> UIStateContent(
    state: UIState<T>,
    onRetry: (() -> Unit)? = null,
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    errorContent: @Composable (String) -> Unit = { message ->
        DefaultErrorContent(message, onRetry)
    },
    idleContent: @Composable () -> Unit = {},
    successContent: @Composable (T) -> Unit
) {
    when (state) {
        is UIState.Idle -> idleContent()
        is UIState.Loading -> loadingContent()
        is UIState.Error -> errorContent(state.errorMessage)
        is UIState.Success -> successContent(state.data)
    }
}

/**
 * Collect StateFlow as State and render based on UIState
 */
@Composable
fun <T> UIStateContent(
    stateFlow: StateFlow<UIState<T>>,
    onRetry: (() -> Unit)? = null,
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    errorContent: @Composable (String) -> Unit = { message ->
        DefaultErrorContent(message, onRetry)
    },
    idleContent: @Composable () -> Unit = {},
    successContent: @Composable (T) -> Unit
) {
    val state by stateFlow.collectAsState()
    UIStateContent(
        state = state,
        onRetry = onRetry,
        loadingContent = loadingContent,
        errorContent = errorContent,
        idleContent = idleContent,
        successContent = successContent
    )
}

/**
 * Default loading content
 */
@Composable
fun DefaultLoadingContent(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = colorResource(R.color.paint_01)
            )
            message?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

/**
 * Default error content with retry button
 */
@Composable
fun DefaultErrorContent(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "😕",
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )

            onRetry?.let {
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = it) {
                    Text("Retry")
                }
            }
        }
    }
}

/**
 * Default empty state content
 */
@Composable
fun DefaultEmptyContent(
    message: String = "No data available",
    emoji: String = "📭",
    onAction: (() -> Unit)? = null,
    actionLabel: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            if (onAction != null && actionLabel != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onAction) {
                    Text(actionLabel)
                }
            }
        }
    }
}

/**
 * Handle ViewModel events with Toast and common patterns
 */
@Composable
fun BaseViewModel.HandleEvents(
    onNavigateTo: ((String) -> Unit)? = null,
    onNavigateBack: (() -> Unit)? = null
) {
    val context = LocalContext.current

    ObserveEvents { event ->
        when (event) {
            is ViewModelEvent.MessageEvent.ShowToast -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            is ViewModelEvent.MessageEvent.ShowError -> {
                Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }

            is ViewModelEvent.NavigationEvent.NavigateTo -> {
                onNavigateTo?.invoke(event.route)
            }

            is ViewModelEvent.NavigationEvent.NavigateBack -> {
                onNavigateBack?.invoke()
            }

            else -> { /* Other events handled elsewhere */
            }
        }
    }
}

/**
 * Show loading overlay
 */
@Composable
fun LoadingOverlay(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = colorResource(R.color.paint_01)
            )
        }
    }
}

/**
 * Observe SessionState and show appropriate UI
 */
@Composable
fun SessionStateContent(
    sessionViewModel: SessionViewModel,
    onNotAuthenticated: () -> Unit = {},
    onError: (String) -> Unit = {},
    authenticatedContent: @Composable () -> Unit
) {
    val sessionState by sessionViewModel.sessionState.collectAsState()

    when (sessionState) {
        is SessionState.Loading -> {
            DefaultLoadingContent(message = "Checking session...")
        }

        is SessionState.Authenticated -> {
            authenticatedContent()
        }

        is SessionState.NotAuthenticated -> {
            LaunchedEffect(Unit) {
                onNotAuthenticated()
            }
        }

        is SessionState.SessionExpired -> {
            LaunchedEffect(Unit) {
                onNotAuthenticated()
            }
        }

        is SessionState.Error -> {
            val errorMessage = (sessionState as SessionState.Error).message
            LaunchedEffect(errorMessage) {
                onError(errorMessage)
            }
        }
    }
}

/**
 * Conditional content based on authentication
 */
@Composable
fun AuthenticatedContent(
    sessionViewModel: SessionViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    authenticatedContent: @Composable (com.vanard.domain.model.User) -> Unit,
    unauthenticatedContent: @Composable () -> Unit = {}
) {
    val userState by sessionViewModel.userState.collectAsState()

    when (val state = userState) {
        is UIState.Success -> {
            state.data?.let { user ->
                authenticatedContent(user)
            } ?: unauthenticatedContent()
        }

        is UIState.Loading -> {
            DefaultLoadingContent()
        }

        else -> {
            unauthenticatedContent()
        }
    }
}

/**
 * Pull to refresh wrapper for UIState
 */
@Composable
fun <T> RefreshableUIStateContent(
    state: UIState<T>,
    onRefresh: () -> Unit,
    successContent: @Composable (T) -> Unit
) {
    // Note: Implement with actual pull-to-refresh component
    // This is a simplified version
    UIStateContent(
        state = state,
        onRetry = onRefresh,
        successContent = successContent
    )
}

/**
 * Helper to check if list is empty in UIState
 */
fun <T> UIState<List<T>>.isEmpty(): Boolean {
    return when (this) {
        is UIState.Success -> data.isEmpty()
        else -> false
    }
}

/**
 * Helper to show empty state for list UIState
 */
@Composable
fun <T> ListUIStateContent(
    state: UIState<List<T>>,
    onRetry: (() -> Unit)? = null,
    emptyMessage: String = "No items found",
    emptyEmoji: String = "📭",
    itemContent: @Composable (List<T>) -> Unit
) {
    UIStateContent(
        state = state,
        onRetry = onRetry
    ) { items ->
        if (items.isEmpty()) {
            DefaultEmptyContent(
                message = emptyMessage,
                emoji = emptyEmoji,
                onAction = onRetry,
                actionLabel = if (onRetry != null) "Refresh" else null
            )
        } else {
            itemContent(items)
        }
    }
}
