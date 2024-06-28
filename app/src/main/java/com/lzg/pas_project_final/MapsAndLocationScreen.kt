package com.lzg.pas_project_final

import androidx.compose.foundation.layout.Box
import com.google.maps.android.compose.GoogleMap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun MapScreen(){
    var isMapLoaded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Add GoogleMap here
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            onMapLoaded = { isMapLoaded = true }
        )

        // ...
    }

}