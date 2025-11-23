package com.vanard.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.vanard.resources.R
import com.vanard.ui.theme.VShopTheme

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val signUpState by viewModel.signUpState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Handle sign up state changes
    LaunchedEffect(signUpState) {
        when (val state = signUpState) {
            is UIState.Success -> {
                context.toastMsg("Account created successfully!")
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Onboard.route) { inclusive = true }
                }
            }

            is UIState.Error -> {
                context.toastMsg(state.errorMessage)
            }

            else -> Unit
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Logo
        Image(
            painter = painterResource(R.drawable.vshop_logo),
            contentDescription = "VShop Logo",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Welcome Text
        Text(
            text = "Create Account",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.paint_05),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Sign up to start shopping",
            fontSize = 16.sp,
            color = colorResource(R.color.paint_02),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Sign Up Form Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.paint_04)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // First Name Field
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name", color = colorResource(R.color.paint_02)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.profile),
                            contentDescription = null,
                            tint = colorResource(R.color.paint_02)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.paint_01),
                        unfocusedBorderColor = colorResource(R.color.paint_03),
                        focusedLabelColor = colorResource(R.color.paint_01),
                        unfocusedLabelColor = colorResource(R.color.paint_02),
                        cursorColor = colorResource(R.color.paint_01),
                        focusedTextColor = colorResource(R.color.paint_05),
                        unfocusedTextColor = colorResource(R.color.paint_05)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Last Name Field
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name", color = colorResource(R.color.paint_02)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.profile),
                            contentDescription = null,
                            tint = colorResource(R.color.paint_02)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.paint_01),
                        unfocusedBorderColor = colorResource(R.color.paint_03),
                        focusedLabelColor = colorResource(R.color.paint_01),
                        unfocusedLabelColor = colorResource(R.color.paint_02),
                        cursorColor = colorResource(R.color.paint_01),
                        focusedTextColor = colorResource(R.color.paint_05),
                        unfocusedTextColor = colorResource(R.color.paint_05)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = colorResource(R.color.paint_02)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_email),
                            contentDescription = null,
                            tint = colorResource(R.color.paint_02)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.paint_01),
                        unfocusedBorderColor = colorResource(R.color.paint_03),
                        focusedLabelColor = colorResource(R.color.paint_01),
                        unfocusedLabelColor = colorResource(R.color.paint_02),
                        cursorColor = colorResource(R.color.paint_01),
                        focusedTextColor = colorResource(R.color.paint_05),
                        unfocusedTextColor = colorResource(R.color.paint_05)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Phone Field (Optional)
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone (Optional)", color = colorResource(R.color.paint_02)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_phone),
                            contentDescription = null,
                            tint = colorResource(R.color.paint_02)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.paint_01),
                        unfocusedBorderColor = colorResource(R.color.paint_03),
                        focusedLabelColor = colorResource(R.color.paint_01),
                        unfocusedLabelColor = colorResource(R.color.paint_02),
                        cursorColor = colorResource(R.color.paint_01),
                        focusedTextColor = colorResource(R.color.paint_05),
                        unfocusedTextColor = colorResource(R.color.paint_05)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = colorResource(R.color.paint_02)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_lock),
                            contentDescription = null,
                            tint = colorResource(R.color.paint_02)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.paint_01),
                        unfocusedBorderColor = colorResource(R.color.paint_03),
                        focusedLabelColor = colorResource(R.color.paint_01),
                        unfocusedLabelColor = colorResource(R.color.paint_02),
                        cursorColor = colorResource(R.color.paint_01),
                        focusedTextColor = colorResource(R.color.paint_05),
                        unfocusedTextColor = colorResource(R.color.paint_05)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password Field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password", color = colorResource(R.color.paint_02)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_lock),
                            contentDescription = null,
                            tint = colorResource(R.color.paint_02)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                painter = painterResource(
                                    if (confirmPasswordVisible) R.drawable.ic_visibility_off
                                    else R.drawable.ic_visibility
                                ),
                                contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                                tint = colorResource(R.color.paint_02)
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.paint_01),
                        unfocusedBorderColor = colorResource(R.color.paint_03),
                        focusedLabelColor = colorResource(R.color.paint_01),
                        unfocusedLabelColor = colorResource(R.color.paint_02),
                        cursorColor = colorResource(R.color.paint_01),
                        focusedTextColor = colorResource(R.color.paint_05),
                        unfocusedTextColor = colorResource(R.color.paint_05)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Sign Up Button
                Button(
                    onClick = {
                        when {
                            firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank() -> {
                                context.toastMsg("Please fill in all required fields")
                            }

                            password != confirmPassword -> {
                                context.toastMsg("Passwords do not match")
                            }

                            password.length < 6 -> {
                                context.toastMsg("Password must be at least 6 characters")
                            }

                            else -> {
                                viewModel.signUp(
                                    email = email,
                                    password = password,
                                    firstName = firstName,
                                    lastName = lastName,
                                    phone = phone.takeIf { it.isNotBlank() }
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.paint_01)
                    ),
                    enabled = signUpState !is UIState.Loading
                ) {
                    if (signUpState is UIState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "Create Account",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login Link
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account? ",
                color = colorResource(R.color.paint_02)
            )
            Text(
                text = "Login",
                color = colorResource(R.color.paint_01),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back to Onboard
        TextButton(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text(
                text = "Back",
                color = colorResource(R.color.paint_02)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    VShopTheme {
        SignUpScreen(navController = rememberNavController())
    }
}