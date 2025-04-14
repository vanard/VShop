package com.vanard.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanard.resources.R

@Composable
fun CustomSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search clothes.."
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.search_normal),
                tint = colorResource(R.color.paint_03),
//                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                modifier = modifier
                    .padding(start = 12.dp)
                    .size(24.dp)
            )
        },
        placeholder = {
            Text(text = placeholder, color = colorResource(R.color.paint_03), fontSize = 14.sp)
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            focusedBorderColor = Color.LightGray,
            unfocusedBorderColor = Color.LightGray,
            cursorColor = Color.Gray,
            focusedTextColor = Color.Black,
        )
    )
}
