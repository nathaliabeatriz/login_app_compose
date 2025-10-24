package com.example.loginapp_compose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loginapp_compose.ui.theme.LoginAppComposeTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity: ComponentActivity() {
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
                    LoginScreen(
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

    private fun redirectToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToRegister(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("EmailPassword", "signInWithEmail:success")
                    val user = auth.currentUser
                    redirectToMain()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("EmailPassword", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }
    }

    @Composable
    //@Preview
    fun LoginScreen(modifier: Modifier = Modifier) {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.dark_blue)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.sign_in),
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
                    onValueChange = { email = it },
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
                        unfocusedLeadingIconColor = colorResource(id = R.color.light_blue)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.width(320.dp),
                    placeholder = { Text(stringResource(id = R.string.password_input)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.password_icon),
                            contentDescription = "Ícone de senha",
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
                        unfocusedLeadingIconColor = colorResource(id = R.color.light_blue)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { signIn(email, password) },
                    modifier = Modifier
                        .width(320.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.light_blue)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_in),
                        color = colorResource(id = R.color.dark_blue),
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(id = R.string.no_account),
                    color = colorResource(id = R.color.light_blue),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(320.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 7. Botão Register
                Button(
                    onClick = { redirectToRegister() },
                    modifier = Modifier
                        .width(320.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.light_orange)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.register),
                        color = colorResource(id = R.color.dark_blue),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}