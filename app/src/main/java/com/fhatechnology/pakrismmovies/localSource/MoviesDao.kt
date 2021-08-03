package com.fhatechnology.pakrismmovies.localSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fhatechnology.pakrismmovies.model.Movies

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllMovies(movies : List<Movies>)

    @Query("SELECT * FROM Movies ORDER BY id ASC")
    fun getAllPopularMovies() : List<Movies>

}