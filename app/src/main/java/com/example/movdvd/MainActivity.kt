package com.example.movdvd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.movdvd.ui.theme.MovDVDTheme
import com.example.movdvd.viewmodel.MovieViewModel

class MainActivity : ComponentActivity() {

    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovDVDTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Use our new AppNavigation composable
                    AppNavigation(
                        movieViewModel = movieViewModel,
                        modifier = Modifier.padding(innerPadding) // Apply padding from the Scaffold
                    )
                }
            }
        }
    }
}
