package com.example.movdvd.repository

import com.example.movdvd.data.Movie
import com.example.movdvd.data.MovieDao
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val movieDao: MovieDao) {

    val allMovies: Flow<List<Movie>> = movieDao.getAllMovies()
    val favoriteMovies: Flow<List<Movie>> = movieDao.getFavoriteMovies()


    fun getMovieById(movieId: Int): Flow<Movie?> {
        return movieDao.getMovieById(movieId)
    }

    suspend fun addMovie(movie: Movie) {
        movieDao.insertMovie(movie)
    }

    suspend fun update(movie: Movie) {
        movieDao.updateMovie(movie)
    }


    suspend fun delete(movie: Movie) {
        movieDao.deleteMovie(movie)
    }

}
