package com.vanard.feature.profile

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.vanard.common.Screen
import com.vanard.common.UIState
import com.vanard.common.util.toastMsg
import com.vanard.domain.model.User
import com.vanard.feature.base.BaseScreen
import com.vanard.resources.R
import com.vanard.ui.components.AvatarImage
import com.vanard.ui.theme.VShopTheme

@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Use BaseScreen to handle session management
    BaseScreen(
        navController = navController,
        requireAuth = false, // Don't require auth, show different UI based on state
        showLoading = true
    ) { user ->
        ProfileContent(
            navController = navController,
            user = user,
            modifier = modifier
        )
    }
}

@Composable
private fun ProfileContent(
    navController: NavController,
    user: User?,
    viewModel: ProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val logoutState by viewModel.logoutState.collectAsState()

    // Handle logout state changes
    LaunchedEffect(logoutState) {
        when (val state = logoutState) {
            is UIState.Success -> {
                context.toastMsg("Logged out successfully")
                navController.navigate(Screen.Onboard.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            }
            is UIState.Error -> {
                context.toastMsg(state.errorMessage)
            }
            else -> Unit
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderProfileScreen(
            navigateBack = { navController.navigateUp() }
        )

        Log.d(ProfileViewModel.TAG, "ProfileContent: ${user?.email}")

        if (user != null && user.id.isNotEmpty()) {
            AuthenticatedProfileContent(
                user = user,
                onLogout = { viewModel.logout() },
                isLoggingOut = logoutState is UIState.Loading,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            GuestProfileContent(
                onLogin = { navController.navigate(Screen.Login.route) },
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun AuthenticatedProfileContent(
    user: User,
    onLogout: () -> Unit,
    isLoggingOut: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        AvatarImage(
            painter = painterResource(R.drawable.avatar_image),
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // User Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = user.fullName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.paint_01),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = user.email,
                    fontSize = 16.sp,
                    color = colorResource(R.color.paint_02),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                user.phone?.let { phoneNumber ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = phoneNumber,
                        fontSize = 16.sp,
                        color = colorResource(R.color.paint_02),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Divider(color = colorResource(R.color.paint_05))

                Spacer(modifier = Modifier.height(20.dp))

                // Profile Options
                ProfileOption(
                    icon = R.drawable.setting_4,
                    title = "Edit Profile",
                    onClick = { /* TODO: Navigate to edit profile */ }
                )

                ProfileOption(
                    icon = R.drawable.shopping_bag,
                    title = "My Orders",
                    onClick = { /* TODO: Navigate to orders */ }
                )

                ProfileOption(
                    icon = R.drawable.heart,
                    title = "My Wishlist",
                    onClick = { /* TODO: Navigate to wishlist */ }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logout Button
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red.copy(alpha = 0.8f)
            ),
            enabled = !isLoggingOut
        ) {
            if (isLoggingOut) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "Logout",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun GuestProfileContent(
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
//            painter = painterResource(R.drawable.avatar_image),
            imageVector = Icons.Rounded.AccountCircle,
            contentDescription = "Avatar Guest",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Guest User",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.paint_01)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sign in to access your profile",
            fontSize = 16.sp,
            color = colorResource(R.color.paint_02),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.paint_01)
            )
        ) {
            Text(
                text = "Sign In",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProfileOption(
    icon: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = colorResource(R.color.paint_02),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            color = colorResource(R.color.paint_01),
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(R.drawable.arrow_right_1),
            contentDescription = null,
            tint = colorResource(R.color.paint_02),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun HeaderProfileScreen(navigateBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
//        modifier = modifier.padding(top = 16.dp)
    ) {
        IconButton(onClick = {
            navigateBack()
        }) {
            Icon(
                painter = painterResource(R.drawable.arrow_left_1),
                contentDescription = null,
                modifier = modifier
                    .border(
                        1.dp,
                        color = colorResource(R.color.paint_03),
                        shape = CircleShape
                    )
                    .padding(8.dp)
            )
        }

        Text(
            "Profile",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = modifier
                .weight(1f)
                .offset(x = (-24).dp)
        )
    }
}

@Preview
@Composable
fun ProfileScreenPreview(modifier: Modifier = Modifier) {
    VShopTheme {
        ProfileScreen(rememberNavController())
    }
}