package com.example.movdvd

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movdvd.ui.AddMovieScreen
import com.example.movdvd.ui.HomeScreen
import com.example.movdvd.viewmodel.MovieViewModel


import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.movdvd.ui.MovieDetailScreen


@Composable
fun AppNavigation(
    movieViewModel: MovieViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(
                movieViewModel = movieViewModel,
                onAddMovie = { navController.navigate("addMovie") },
                // Define what happens when a movie is clicked
                onMovieClick = { movieId ->
                    navController.navigate("movieDetail/$movieId")
                }
            )
        }

        composable("addMovie") {
            AddMovieScreen(navController = navController, movieViewModel = movieViewModel)
        }


        composable(
            route = "movieDetail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")
            MovieDetailScreen(
                navController = navController,
                movieViewModel = movieViewModel,
                movieId = movieId
            )
        }
    }
}
