package com.example.movdvd.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    val id: Int, // 2 digits long (11-99)
    val title: String,
    val director: String,
    val price: Double,
    val releaseDate: String,
    val duration: Int,
    val genre: String,
    var isFavorite: Boolean = false
)