package com.example.loginapp_compose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.loginapp_compose.ui.theme.LoginAppComposeTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class RegisterActivity: ComponentActivity() {
    val hostGroteskFamily = FontFamily(
        Font(R.font.host_grotesk_regular, FontWeight.Normal)
    )

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        setContent {
            LoginAppComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            redirectToMain()
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("EmailPassword", "createUserWithEmail:success")
                    val user = auth.currentUser
                    redirectToMain()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("EmailPassword", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }
    }

    private fun redirectToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    @Composable
    fun RegisterScreen(modifier: Modifier = Modifier) {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        var emailError by remember { mutableStateOf<String?>(null) }
        var passwordError by remember { mutableStateOf<String?>(null) }
        var confirmPasswordError by remember { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.dark_blue)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    color = Color.White,
                    fontSize = 40.sp,
                    fontFamily = hostGroteskFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(350.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    modifier = Modifier.width(320.dp),
                    placeholder = { Text(stringResource(id = R.string.user_input)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.user_icon),
                            contentDescription = "User icon",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedBorderColor = colorResource(id = R.color.light_blue),
                        focusedBorderColor = colorResource(id = R.color.light_blue),
                        focusedTextColor = colorResource(id = R.color.light_blue),
                        unfocusedTextColor = colorResource(id = R.color.light_blue),
                        focusedPlaceholderColor = colorResource(id = R.color.light_blue),
                        unfocusedPlaceholderColor = colorResource(id = R.color.light_blue),
                        focusedLeadingIconColor = colorResource(id = R.color.light_blue),
                        unfocusedLeadingIconColor = colorResource(id = R.color.light_blue),
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        errorLeadingIconColor = MaterialTheme.colorScheme.error,
                        errorTextColor = MaterialTheme.colorScheme.error,
                        errorPlaceholderColor = colorResource(id = R.color.light_blue)
                    ),
                    isError = emailError != null,
                    supportingText = {
                        if (emailError != null) {
                            Text(
                                text = emailError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(5.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                    },
                    modifier = Modifier.width(320.dp),
                    placeholder = { Text(stringResource(id = R.string.password_input)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.password_icon),
                            contentDescription = "Password icon",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedBorderColor = colorResource(id = R.color.light_blue),
                        focusedBorderColor = colorResource(id = R.color.light_blue),
                        focusedTextColor = colorResource(id = R.color.light_blue),
                        unfocusedTextColor = colorResource(id = R.color.light_blue),
                        focusedPlaceholderColor = colorResource(id = R.color.light_blue),
                        unfocusedPlaceholderColor = colorResource(id = R.color.light_blue),
                        focusedLeadingIconColor = colorResource(id = R.color.light_blue),
                        unfocusedLeadingIconColor = colorResource(id = R.color.light_blue),
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        errorLeadingIconColor = MaterialTheme.colorScheme.error,
                        errorTextColor = MaterialTheme.colorScheme.error,
                        errorPlaceholderColor = colorResource(id = R.color.light_blue)
                    ),
                    isError = passwordError != null,
                    supportingText = {
                        if (passwordError != null) {
                            Text(
                                text = passwordError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(5.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = null
                    },
                    modifier = Modifier.width(320.dp),
                    placeholder = { Text(stringResource(id = R.string.confirm_input)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.confirm_icon),
                            contentDescription = "Confirmation icon",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedBorderColor = colorResource(id = R.color.light_blue),
                        focusedBorderColor = colorResource(id = R.color.light_blue),
                        focusedTextColor = colorResource(id = R.color.light_blue),
                        unfocusedTextColor = colorResource(id = R.color.light_blue),
                        focusedPlaceholderColor = colorResource(id = R.color.light_blue),
                        unfocusedPlaceholderColor = colorResource(id = R.color.light_blue),
                        focusedLeadingIconColor = colorResource(id = R.color.light_blue),
                        unfocusedLeadingIconColor = colorResource(id = R.color.light_blue),
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        errorLeadingIconColor = MaterialTheme.colorScheme.error,
                        errorTextColor = MaterialTheme.colorScheme.error,
                        errorPlaceholderColor = colorResource(id = R.color.light_blue)
                    ),
                    isError = confirmPasswordError != null,
                    supportingText = {
                        if (confirmPasswordError != null) {
                            Text(
                                text = confirmPasswordError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        var isValid = true

                        if (email.isBlank()) {
                            emailError = "E-mail is required"
                            isValid = false
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            emailError = "Invalid e-mail format"
                            isValid = false
                        }

                        if (password.length < 6) {
                            passwordError =
                                "Password must have at least 6 characters"
                            isValid = false
                        }

                        if (password != confirmPassword) {
                            confirmPasswordError = "Passwords do not match"
                            isValid = false
                        }

                        if (isValid) {
                            createAccount(email, password)
                        }
                    },
                    modifier = Modifier
                        .width(320.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.light_blue)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.register),
                        color = colorResource(id = R.color.dark_blue),
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(id = R.string.has_account),
                    color = colorResource(id = R.color.light_blue),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(320.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { redirectToLogin() },
                    modifier = Modifier
                        .width(320.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.light_orange)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_in),
                        color = colorResource(id = R.color.dark_blue),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}