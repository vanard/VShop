package com.vanard.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.vanard.resources.R

/**
 * Styled OutlinedTextField with consistent VShop theme
 */
@Composable
fun SignUpFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: Painter? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = colorResource(R.color.paint_02)) },
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    painter = it,
                    contentDescription = null,
                    tint = colorResource(R.color.paint_02)
                )
            }
        },
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(R.color.paint_01),
            unfocusedBorderColor = colorResource(R.color.paint_03),
            focusedLabelColor = colorResource(R.color.paint_01),
            unfocusedLabelColor = colorResource(R.color.paint_02),
            cursorColor = colorResource(R.color.paint_01),
            focusedTextColor = colorResource(R.color.paint_05),
            unfocusedTextColor = colorResource(R.color.paint_05)
        ),
        enabled = enabled,
        isError = isError
    )
}

/**
 * Password TextField with show/hide toggle
 */
@Composable
fun SignUpFormPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    passwordVisible: Boolean = false,
    onPasswordVisibilityToggle: () -> Unit = {},
    enabled: Boolean = true,
    isError: Boolean = false
) {
    SignUpFormTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        leadingIcon = painterResource(R.drawable.ic_lock),
        trailingIcon = {
            IconButton(onClick = onPasswordVisibilityToggle) {
                Icon(
                    painter = painterResource(
                        if (passwordVisible) R.drawable.ic_visibility_off
                        else R.drawable.ic_visibility
                    ),
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = colorResource(R.color.paint_02)
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardType = KeyboardType.Password,
        modifier = modifier,
        enabled = enabled,
        isError = isError
    )
}

/**
 * Email TextField
 */
@Composable
fun SignUpFormEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Email",
    enabled: Boolean = true,
    isError: Boolean = false
) {
    SignUpFormTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        leadingIcon = painterResource(R.drawable.ic_email),
        keyboardType = KeyboardType.Email,
        modifier = modifier,
        enabled = enabled,
        isError = isError
    )
}

/**
 * Phone TextField
 */
@Composable
fun SignUpFormPhoneField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Phone",
    enabled: Boolean = true,
    isError: Boolean = false
) {
    SignUpFormTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        leadingIcon = painterResource(R.drawable.ic_phone),
        keyboardType = KeyboardType.Phone,
        modifier = modifier,
        enabled = enabled,
        isError = isError
    )
}
