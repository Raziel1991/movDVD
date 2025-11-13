package com.example.movdvd.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.movdvd.data.Movie
import com.example.movdvd.data.MovieDatabase
import com.example.movdvd.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

// Enum to represent the filter state
enum class MovieFilter {
    ALL,
    FAVORITES
}

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository

    // A StateFlow to hold the current filter state
    private val _filterState = MutableStateFlow(MovieFilter.ALL)

    // A LiveData that transforms based on the filter state.
    // 'flatMapLatest' ensures that when the filter changes, we switch to observing the new Flow.
    val movies: LiveData<List<Movie>> = _filterState.flatMapLatest { filter ->
        when (filter) {
            MovieFilter.ALL -> repository.allMovies
            MovieFilter.FAVORITES -> repository.favoriteMovies
        }
    }.asLiveData()

    // The old allMovies is no longer needed
    // val allMovies: LiveData<List<Movie>>

    init {
        val movieDao = MovieDatabase.getDatabase(application).movieDao()
        repository = MovieRepository(movieDao)
        // The init block is simpler now
    }

    // Function to be called from the UI to change the filter
    fun setFilter(filter: MovieFilter) {
        _filterState.value = filter
    }

    fun getMovieById(movieId: Int): Flow<Movie?> {
        return repository.getMovieById(movieId)
    }

    fun addMovie(movie: Movie) {
        viewModelScope.launch {
            repository.addMovie(movie)
        }
    }

    fun updateMovie(movie: Movie) {
        viewModelScope.launch {
            repository.update(movie)
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            repository.delete(movie)
        }
    }
}
