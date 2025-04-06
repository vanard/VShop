package com.vanard.vshop.persentation

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanard.vshop.R
import com.vanard.vshop.common.toastMsg
import com.vanard.vshop.persentation.ui.theme.VShopTheme

@Composable
fun OnboardScreen(modifier: Modifier = Modifier, navigateToHome: () -> Unit) {
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
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .align(Alignment.BottomEnd)
        ) {
            Text(text = "Quick Finds, Happy Times!", fontSize = 12.sp)
            Spacer(modifier = modifier.size(4.dp))
            Text(
                text = "Shop Instantly Just Enjoy Now!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
                    .padding(top = 28.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        context.toastMsg("Coming Soon!")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.paint_01)),
                    contentPadding = PaddingValues(vertical = 20.dp),
                    modifier = modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) { Text("Login") }
                Button(
                    onClick = {
                        context.toastMsg("Coming Soon!")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.paint_01)),
                    contentPadding = PaddingValues(vertical = 20.dp),
                    modifier = modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) { Text("Sign Up") }
            }
            Spacer(modifier = modifier.size(24.dp))
            Button(
                onClick = {
                    navigateToHome()
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.paint_01)),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
                modifier = modifier
                    .fillMaxWidth(),
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
                .offset(y = (-290).dp)
                .alpha(0.7f)

        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardPreview() {
    VShopTheme {
        VShopApp()
    }
}