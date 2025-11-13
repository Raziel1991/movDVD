package com.example.movdvd.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // This callback will pre-populate the database with initial movies the first time it's created.
        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val movieDao = database.movieDao()
                        movieDao.insertMovie(
                            Movie(
                                11,
                                "Inception",
                                "Christopher Nolan",
                                15.99,
                                "2010-07-16",
                                148,
                                "Sci-Fi",
                                false
                            )
                        )
                        movieDao.insertMovie(
                            Movie(
                                12,
                                "The Matrix",
                                "Wachowskis",
                                12.50,
                                "1999-03-31",
                                136,
                                "Action",
                                true
                            )
                        )
                        movieDao.insertMovie(
                            Movie(
                                13,
                                "Parasite",
                                "Bong Joon Ho",
                                19.99,
                                "2019-05-30",
                                132,
                                "Thriller",
                                false
                            )
                        )
                    }
                }
            }
        }
    }
}