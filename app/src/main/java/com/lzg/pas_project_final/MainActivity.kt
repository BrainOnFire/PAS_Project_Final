package com.lzg.pas_project_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lzg.pas_project_final.ui.theme.PAS_Project_FinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PAS_Project_FinalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Hello World", modifier = Modifier.padding(it))
                }
            }
        }
    }
}

