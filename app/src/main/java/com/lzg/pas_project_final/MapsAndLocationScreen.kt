package com.lzg.pas_project_final

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import com.google.maps.android.compose.GoogleMap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.maps.android.compose.*
import kotlinx.coroutines.tasks.await
import java.io.BufferedReader
import java.io.InputStreamReader
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController){
    var showSnackbar by remember { mutableStateOf(false) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var isMapLoaded by remember { mutableStateOf(false) }
    var defibrillatorLocations by remember { mutableStateOf(emptyList<LatLng>()) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    val context = LocalContext.current
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
    }

    // Fetch CSV content from Firebase Storage when the screen is loaded
    LaunchedEffect(Unit) {
        try {
            val storage = Firebase.storage
            val storageRef = storage.reference.child("defibrillators.csv")

            val tempFile = createTempFile()
            storageRef.getFile(tempFile).await()

            val csvContent = BufferedReader(InputStreamReader(tempFile.inputStream())).readText()
            val locations = parseCsv(csvContent)
            defibrillatorLocations = locations
        } catch (e: Exception) {
            // Handle exceptions (e.g., file not found, parsing errors)
            Log.e("MapScreen", "Error fetching or parsing CSV", e)
        }
    }

    // Request location permission if not granted
    LaunchedEffect(key1 = true) {
        if (!hasLocationPermission) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Get user location if permission is granted
    LaunchedEffect(key1 = hasLocationPermission) {
        if (hasLocationPermission) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                val location = fusedLocationClient.lastLocation.await()
                if (location != null) {
                    userLocation = LatLng(location.latitude, location.longitude)
                }
            } catch (e: SecurityException) {
                // Handle exception if location access is denied
                Log.e("MapScreen", "Error getting user location", e)
            }
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Busqueda de desfibriladores")
                },
                actions = {
                    IconButton(onClick = { navController.navigate("ProfileScreen/$userId") }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Profile"
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { sendUserLocationToFirestore(context, userId)
                            showSnackbar = true
                }) {

                Icon(
                    painter = painterResource(id = R.drawable.heart_broken_24dp_fill0_wght400_grad0_opsz24),
                    contentDescription = "Emergency Heart Icon"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        snackbarHost = {
            if (showSnackbar) {
                Snackbar(
                    modifier = Modifier.padding(8.dp), // Add padding if needed
                    action = {
                        TextButton(onClick = { showSnackbar = false }) {
                            Text("OK")
                        }
                    }
                ) { Text("Ubicación enviada") }
            }
        }

    ){
        paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                onMapLoaded = { isMapLoaded = true },
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission
                ),
                cameraPositionState = rememberCameraPositionState {
                    if (userLocation != null) {
                        position = CameraPosition.fromLatLngZoom(userLocation!!, 200f) // Adjust zoom as needed
                    }
                }
            ) {

                // Add markers for each defibrillator location
                defibrillatorLocations.forEach { location ->
                    Marker(
                        state = MarkerState(position = location),
                        title = "Desfibrilador"
                    )
                }
            }
        }

        LaunchedEffect(key1 = showSnackbar) {
            if (showSnackbar) {
                delay(2000)
                showSnackbar = false // Hide Snackbar
            }
        }
    }
}

// Function to send user location to Firestore
@SuppressLint("MissingPermission")
private fun sendUserLocationToFirestore(context: Context, userId: String) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                val geoPoint = GeoPoint(location.latitude, location.longitude)
                // Firestore path
                FirebaseFirestore.getInstance()
                    .collection("data_ubicacion")
                    .document("latlog")
                    .update(mapOf(userId to geoPoint))
                    .addOnSuccessListener {
                        Log.d("MapScreen", "Location successfully added to Firestore for user: $userId")
                    }
                    .addOnFailureListener { e ->
                        Log.e("MapScreen", "Error adding location to Firestore", e)
                    }
            }
        }
        .addOnFailureListener { e ->
            Log.e("MapScreen", "Error getting user location", e)
        }
}

// Function to parse the CSV content into a list of LatLng objects
fun parseCsv(csvContent: String): List<LatLng> {
    val locations = mutableListOf<LatLng>()
    val lines = csvContent.split("\n")

    // Assuming the first line is the header, find the indices of latitude and longitude columns
    val header = lines[0].split(";")
    val latitudeIndex = header.indexOf("latitude")
    val longitudeIndex = header.indexOf("longitude\r")

    if (latitudeIndex == -1 || longitudeIndex == -1) {
        // Handle the case where latitude or longitude columns are not found
        return emptyList() // Or throw an exception, log an error, etc.
    }

    for (line in lines.drop(1)){ // Skip the header row
        val fields = line.split(";")
        if (fields.size > latitudeIndex && fields.size > longitudeIndex) {
            val latitudeString = fields[latitudeIndex]
            val longitudeString = fields[longitudeIndex]

            // Replace comma with dot for parsing as doubles
            val latitude = latitudeString.replace(",", ".").toDoubleOrNull()
            val longitude = longitudeString.replace(",", ".").toDoubleOrNull()

            if (latitude != null && longitude != null) {
                locations.add(LatLng(latitude, longitude))
            }
        }
    }
    return locations
}