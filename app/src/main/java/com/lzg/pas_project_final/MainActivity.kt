package com.lzg.pas_project_final

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.lzg.pas_project_final.ui.theme.PAS_Project_FinalTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PAS_Project_FinalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()){
                    SignInScreen()
                    //Text(text = "Hello World", modifier = Modifier.padding(it))

                }

                //Text(text = "Hello World", modifier = Modifier.padding(it))

            }
        }
    }
}

//@Composable
//fun ScaffoldExample() {
//    var presses by remember { mutableIntStateOf(0) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                colors = topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.primary,
//                ),
//                title = {
//                    Text("Top app bar")
//                }
//            )
//        },
//        bottomBar = {
//            BottomAppBar(
//                containerColor = MaterialTheme.colorScheme.primaryContainer,
//                contentColor = MaterialTheme.colorScheme.primary,
//            ) {
//                Text(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    textAlign = TextAlign.Center,
//                    text = "Bottom app bar",
//                )
//            }
//        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = { presses++ }) {
//                Icon(Icons.Default.Add, contentDescription = "Add")
//            }
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .padding(innerPadding),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//        ) {
//            Text(
//                modifier = Modifier.padding(8.dp),
//                text =
//                """
//                    This is an example of a scaffold. It uses the Scaffold composable's parameters to create a screen with a simple top app bar, bottom app bar, and floating action button.
//
//                    It also contains some basic inner content, such as this text.
//
//                    You have pressed the floating action button $presses times.
//                """.trimIndent(),
//            )
//        }
//    }
//}