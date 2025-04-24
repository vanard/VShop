package com.vanard.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vanard.resources.R

@Composable
fun CountView(
    currentQuantity: Int,
    onPlusClick: (Int) -> Unit,
    onMinusClick: (Int) -> Unit,
    customIconSize: Int = 24,
    modifier: Modifier = Modifier,
) {
    val iconSize = customIconSize - 12
    var quantity by remember { mutableIntStateOf(currentQuantity) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        IconButton(
            onClick = {
                if (quantity > 1)
                    quantity--
                onMinusClick(quantity)
                Log.d(TAG, "Plus Click: $quantity")
            }, modifier = Modifier
                .padding(8.dp)
                .border(1.dp, color = colorResource(R.color.paint_03), shape = CircleShape)
                .clip(CircleShape)
                .background(color = Color.Transparent)
                .size(customIconSize.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.minus),
                tint = colorResource(R.color.paint_05),
                contentDescription = null,
                modifier = modifier.size(iconSize.dp)
            )
        }

        Text("$quantity", modifier = modifier.padding(start = 4.dp))

        IconButton(
            onClick = {
                quantity++
                onPlusClick(quantity)
                Log.d(TAG, "Plus Click: $quantity")
            }, modifier = Modifier
                .padding(8.dp)
                .border(1.dp, color = colorResource(R.color.paint_03), shape = CircleShape)
                .clip(CircleShape)
                .background(color = Color.Transparent)
                .size(customIconSize.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.add),
                tint = colorResource(R.color.paint_05),
                contentDescription = null,
                modifier = modifier.size(iconSize.dp)
            )
        }

    }
}

private const val TAG = "CountView"