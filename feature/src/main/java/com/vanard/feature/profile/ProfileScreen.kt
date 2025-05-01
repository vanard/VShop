package com.vanard.feature.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.vanard.resources.R
import com.vanard.ui.components.AvatarImage
import com.vanard.ui.theme.VShopTheme

@Composable
fun ProfileScreen(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderProfileScreen({
            navController.navigateUp()
        })
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            AvatarImage(
                painter = painterResource(R.drawable.avatar_image),
                Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(32.dp))
            Text(
                stringResource(R.string.profile_name),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.profile_email),
                fontSize = 18.sp,
                color = Color.Magenta,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            //TODO
//            Spacer(Modifier.height(0.dp))
//            LazyColumn {  }

        }
    }
}

@Composable
fun HeaderProfileScreen(navigateBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(top = 16.dp)
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
            "About",
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