package com.lzg.pas_project_final

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun SignInScreen(navController: NavController) {
    var email by remember {mutableStateOf("")}
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val auth = Firebase.auth

//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Bottom,
//        horizontalAlignment = Alignment.Start
//    ){
//        OutlinedIconButton(
//            onClick = { /*TODO*/ }, //Agregar fingerprint sensing
//            modifier = Modifier.padding(15.dp))
//        {
//            Icon(
//                painter = painterResource(id = R.drawable.fingerprint_24dp_fill0_wght400_grad0_opsz24),
//                contentDescription = "Fingerprint Icon")
//        }
//    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoo),
            contentDescription = "Logo Desfibrilador",
            modifier = Modifier.padding(30.dp)
        )
        Text(
            text = "Buscador de Desfibriladores",
            modifier = Modifier.padding(16.dp),
            fontSize = 20.sp,
        )
        Text(
            text = "Inicie sesión para continuar",
            modifier = Modifier.padding(5.dp),
            fontSize = 15.sp,
        )
        OutlinedTextField(
            value = email ,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "Email Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Password Icon"
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (showError) {
            Text(
                text = "Invalid email or password",
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        Button(
            onClick = {
                showError = false
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (email.isNotEmpty() && password.isNotEmpty() && task.isSuccessful){
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignInScreen", "signInWithEmail:success")
                            // Navigate to the next screen or update UI accordingly
                            navController.navigate("MapScreen"){
                                popUpTo("SignInScreen") {inclusive = true}
                            }
                        }

                        else if (email.isEmpty() || password.isEmpty() && !task.isSuccessful) {
                            showError = true
                        }

                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignInScreen", "signInWithEmail:failure", task.exception)
                            showError = true
                        }
                    }
            },
            enabled = email.isNotEmpty() && password.isNotEmpty(),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Sign In")
        }

//        // Google Sign In Button
//        OutlinedIconButton(
//            onClick = { /*TODO*/ },
//            modifier = Modifier.padding(16.dp)) {
//            Icon(
//                imageVector = Icons.Filled.Email, /////Cambiar por el icono de google
//                contentDescription = "Google Sign In"
//            )
//            Spacer(Modifier.padding(8.dp)) // Add some space between icon and text
//            Text("Sign in with Google")
//        }
    }
}



