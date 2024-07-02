package com.lzg.pas_project_final

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(userID: String) {
    var userName by remember { mutableStateOf("") }
    var userLastName by remember { mutableStateOf("") }
    var userMobile by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = userID) {
        try {
            val documentSnapshot= FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document("usuarios_doc")
                .get()
                .await()

            if (documentSnapshot.exists()) {
                val userData = documentSnapshot.data?.get(userID) as? Map<*, *>
                if (userData != null) {
                    userName = userData["Nombre"] as? String ?: ""
                    userLastName = userData["Apellido"] as? String ?: ""
                    userMobile = userData["Movil"]?.toString() ?: ""
                }
            }
        } catch (e:Exception) {
            // Handle exceptions (e.g., document not found, data type mismatch)
            Log.e("ProfileScreen", "Error fetching user data", e)
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Perfil de Usuario")
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                OutlinedTextField(
                    value = userName,
                    onValueChange = { /* Do nothing, this field is read-only */ },
                    label = { Text("Nombre") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                OutlinedTextField(
                    value = userLastName,
                    onValueChange = { /* Do nothing, this field is read-only */ },
                    label = { Text("Apellido") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                OutlinedTextField(
                    value = userMobile,
                    onValueChange = { /* Do nothing, this field is read-only */ },
                    label = { Text("Móvil") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}