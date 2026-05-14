package com.vanard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.vanard.resources.R
import com.vanard.ui.theme.VShopSoftSurface
import com.vanard.ui.theme.VShopTextPrimary
import com.vanard.ui.theme.VShopTextSecondary
import com.vanard.ui.theme.VShopTextTertiary

@Composable
fun CustomSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search Products",
    showVoiceIcon: Boolean = true,
    leadingIconRes: Int = R.drawable.search_normal,
    leadingIconContentDescription: String = "Search Icon",
    onLeadingIconClick: (() -> Unit)? = null,
    onSearch: () -> Unit = {},
) {
    val leadingIcon = @Composable {
        Icon(
            painter = painterResource(leadingIconRes),
            tint = VShopTextSecondary,
            contentDescription = leadingIconContentDescription,
            modifier = Modifier.size(20.dp)
        )
    }

    TextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(18.dp)),
        leadingIcon = {
            if (onLeadingIconClick == null) {
                leadingIcon()
            } else {
                IconButton(onClick = onLeadingIconClick, modifier = Modifier.size(40.dp)) {
                    leadingIcon()
                }
            }
        },
        trailingIcon = if (showVoiceIcon) {
            {
                Icon(
                    painter = painterResource(R.drawable.menu1),
                    tint = VShopTextTertiary,
                    contentDescription = "Voice Search",
                    modifier = Modifier.size(18.dp)
                )
            }
        } else null,
        placeholder = {
            Text(text = placeholder, color = VShopTextTertiary, fontSize = 13.sp)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = VShopSoftSurface,
            unfocusedContainerColor = VShopSoftSurface,
            disabledContainerColor = VShopSoftSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = VShopTextPrimary,
            focusedTextColor = VShopTextPrimary,
            unfocusedTextColor = VShopTextPrimary,
        )
    )
}

@Composable
fun SearchAndFilterBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search Products",
    isSearchMode: Boolean = false,
    onSearchClick: () -> Unit = {},
    onExitSearchClick: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomSearchBar(
            query = query,
            onQueryChanged = onQueryChanged,
            placeholder = placeholder,
            modifier = Modifier.weight(1f),
            leadingIconRes = if (isSearchMode) R.drawable.arrow_left_1 else R.drawable.search_normal,
            leadingIconContentDescription = if (isSearchMode) "Exit Search" else "Search Icon",
            onLeadingIconClick = if (isSearchMode) onExitSearchClick else null,
            onSearch = onSearchClick
        )
        IconButton(
            onClick = if (isSearchMode) onFilterClick else onSearchClick,
            modifier = Modifier
                .padding(start = 12.dp)
                .size(52.dp)
                .clip(CircleShape)
                .background(VShopSoftSurface)
        ) {
            Icon(
                painter = painterResource(if (isSearchMode) R.drawable.setting_4 else R.drawable.search_normal),
                tint = VShopTextPrimary,
                contentDescription = if (isSearchMode) "Filter" else "Search",
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
fun FilterChipButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            leadingIcon?.invoke()
            Text(
                text = text,
                color = VShopTextSecondary,
                fontSize = 12.sp,
                modifier = if (leadingIcon != null) Modifier.padding(start = 6.dp) else Modifier
            )
        }
    }
}
