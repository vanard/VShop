package com.vanard.vshop.persentation.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.vanard.common.Screen
import com.vanard.common.util.toastMsg
import com.vanard.resources.R
import com.vanard.ui.theme.VShopTheme

object OnboardScreenTestTag {
    const val ONBOARD_TEXT = "onboard_text"
    const val LOGIN_BUTTON = "login_button"
    const val GUEST_BUTTON = "guest_button"
}

@Composable
fun OnboardScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        val painterRs = painterResource(R.drawable.product1)
        Image(
            painter = painterRs,
            contentDescription = "Onboard Image",
            contentScale = ContentScale.FillWidth,
            modifier = modifier.fillMaxWidth(),
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)
                )
                .background(color = colorResource(R.color.paint_04))
                .padding(horizontal = 20.dp, vertical = 30.dp)
                .align(Alignment.BottomEnd)
        ) {
            Text(text = "Quick Finds, Happy Times!", fontSize = 12.sp)
            Spacer(modifier = modifier.size(4.dp))
            Text(
                text = "Shop Instantly Just Enjoy Now!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag(OnboardScreenTestTag.ONBOARD_TEXT)
            )
            Spacer(modifier = modifier.height(30.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
                    .padding(top = 28.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        navController.navigate(Screen.Login.route)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.paint_01)),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    modifier = modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .testTag(OnboardScreenTestTag.LOGIN_BUTTON)
                ) { Text("Login") }
                Button(
                    onClick = {
                        navController.navigate(Screen.SignUp.route)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.paint_01)),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    modifier = modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) { Text("Sign Up") }
            }
            Spacer(modifier = modifier.size(20.dp))
            Button(
                onClick = {
                    navController.navigate(route = Screen.Home.route){
                        popUpTo(Screen.Onboard.route) {
                            inclusive = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.paint_01)),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .testTag(OnboardScreenTestTag.GUEST_BUTTON),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Continue as Guest",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Left,
                    modifier = modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(R.drawable.arrow_right_1),
                    tint = Color.White,
                    contentDescription = null,
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.vshop_logo),
            contentDescription = "Onboard Image",
            modifier = modifier
                .padding(end = 16.dp)
                .size(48.dp)
                .align(Alignment.BottomEnd)
                .offset(y = (-284).dp)
                .alpha(0.7f)

        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardPreview() {
    VShopTheme {
        OnboardScreen(rememberNavController())
    }
}