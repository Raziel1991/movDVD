package com.example.movdvd.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
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
fun AddMovieScreen(navController: NavController, movieViewModel: MovieViewModel) {
    // State variables for each input field
    var id by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var releaseDate by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("Family") } // Default genre
    var isFavorite by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Movie") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Make the column scrollable
        ) {
            // Input Fields
            OutlinedTextField(
                value = id,
                onValueChange = { if (it.length <= 2) id = it },
                label = { Text("ID (2 digits, 11-99)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = director, onValueChange = { director = it }, label = { Text("Director") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = releaseDate, onValueChange = { releaseDate = it }, label = { Text("Release Date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (minutes)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            // Genre Dropdown
            GenreDropdown(selectedGenre = genre, onGenreSelected = { genre = it })

            Spacer(Modifier.height(16.dp))

            // Favorite Toggle
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Mark as Favorite", modifier = Modifier.weight(1f))
                Switch(checked = isFavorite, onCheckedChange = { isFavorite = it })
            }

            Spacer(Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = {
                    // --- IMPROVED VALIDATION LOGIC ---
                    val idInt = id.toIntOrNull()
                    if (idInt == null || idInt !in 11..99) {
                        Toast.makeText(context, "ID must be a 2-digit number (11-99).", Toast.LENGTH_SHORT).show()
                        return@Button // Stop execution here
                    }

                    if (title.isBlank() || director.isBlank() || releaseDate.isBlank()) {
                        Toast.makeText(context, "Please fill all required text fields.", Toast.LENGTH_SHORT).show()
                        return@Button // Stop execution here
                    }

                    val priceDouble = price.toDoubleOrNull()
                    if (priceDouble == null || priceDouble <= 0) {
                        Toast.makeText(context, "Price must be a valid positive number.", Toast.LENGTH_SHORT).show()
                        return@Button // Stop execution here
                    }

                    val durationInt = duration.toIntOrNull()
                    if (durationInt == null || durationInt <= 0) {
                        Toast.makeText(context, "Duration must be a valid positive number.", Toast.LENGTH_SHORT).show()
                        return@Button // Stop execution here
                    }

                    // --- If we reach here, all data is valid ---
                    val newMovie = Movie(
                        id = idInt,
                        title = title,
                        director = director,
                        price = priceDouble,
                        releaseDate = releaseDate,
                        duration = durationInt,
                        genre = genre,
                        isFavorite = isFavorite
                    )
                    movieViewModel.addMovie(newMovie)
                    Toast.makeText(context, "Movie Saved!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack() // Go back to home screen
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SAVE MOVIE")
            }
        }
    }
}

@Composable
fun GenreDropdown(selectedGenre: String, onGenreSelected: (String) -> Unit) {
    val genres = listOf("Family", "Comedy", "Thriller", "Action", "Sci-Fi", "Drama", "Horror")
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedGenre,
            onValueChange = {},
            label = { Text("Genre") },
            readOnly = true, // Make it not editable by keyboard
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Genre")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            genres.forEach { genre ->
                DropdownMenuItem(
                    text = { Text(genre) },
                    onClick = {
                        onGenreSelected(genre)
                        expanded = false
                    }
                )
            }
        }
    }
}
