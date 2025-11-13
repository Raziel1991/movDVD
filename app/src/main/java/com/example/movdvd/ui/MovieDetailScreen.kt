package com.example.movdvd.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movdvd.data.Movie
import com.example.movdvd.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    navController: NavController,
    movieViewModel: MovieViewModel,
    movieId: Int?
) {
    // Collect the movie as a state. It will be null initially, then update when the data loads.
    val movieState by movieViewModel.getMovieById(movieId ?: -1).collectAsState(initial = null)

    // A separate state for the movie data that the user can edit
    var movieData by remember { mutableStateOf<Movie?>(null) }

    // This effect updates the editable movieData only when the database fetch completes
    LaunchedEffect(movieState) {
        movieData = movieState
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(movieData?.title ?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {

                    IconButton(onClick = {
                        movieData?.let {
                            movieViewModel.deleteMovie(it)
                            Toast.makeText(context, "${it.title} deleted", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Movie")
                    }
                }
            )
        }
    ) { innerPadding ->
        // Only show the form if the movie data has been loaded
        movieData?.let { movie ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                OutlinedTextField(
                    value = movie.title,
                    onValueChange = { movieData = movie.copy(title = it) },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = movie.director,
                    onValueChange = { movieData = movie.copy(director = it) },
                    label = { Text("Director") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = movie.price.toString(),
                    onValueChange = { movieData = movie.copy(price = it.toDoubleOrNull() ?: movie.price) },
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )



                Spacer(Modifier.height(8.dp))
                GenreDropdown(selectedGenre = movie.genre, onGenreSelected = { movieData = movie.copy(genre = it) })
                Spacer(Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Favorite", modifier = Modifier.weight(1f))
                    Switch(
                        checked = movie.isFavorite,
                        onCheckedChange = { movieData = movie.copy(isFavorite = it) }
                    )
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        movieData?.let { updatedMovie ->

                            movieViewModel.updateMovie(updatedMovie)
                            Toast.makeText(context, "Movie Updated!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("UPDATE MOVIE")
                }
            }
        } ?: Box( // Show a loading indicator while movieData is null
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
