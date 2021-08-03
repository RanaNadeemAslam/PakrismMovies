package com.fhatechnology.pakrismmovies.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fhatechnology.pakrismmovies.localSource.AppDatabase
import com.fhatechnology.pakrismmovies.model.Movies
import com.fhatechnology.pakrismmovies.remoteSource.APIClient
import com.fhatechnology.pakrismmovies.remoteSource.ApiInterface
import com.fhatechnology.pakrismmovies.remoteSource.Movie
import com.fhatechnology.pakrismmovies.remoteSource.MovieResponse
import com.fhatechnology.pakrismmovies.utils.MyConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object RepositoryData {

    private val apiInterface = APIClient.getRetrofit().create(ApiInterface::class.java)

    private lateinit var moviesList: List<Movies>

    private lateinit var appDatabase: AppDatabase

    val movies: MutableLiveData<List<Movies>> by lazy { MutableLiveData<List<Movies>>() }

    fun getMovies(movieType: String, haveNetworkConnection: Boolean): LiveData<List<Movies>> {

        if(!this::moviesList.isInitialized) {
//        if (!haveNetworkConnection) {
            moviesList = getMoviesFromLocal()

            //If movies are exists in database, then post it to your liveData variable.
            if (moviesList.isNotEmpty()) {
                movies.postValue(moviesList)
            }
            loadMoviesRemote(movieType)
        }
        return movies
    }

    private fun getMoviesFromLocal(): List<Movies> {
        return appDatabase.getMoviesDao().getAllPopularMovies()
    }

    private fun loadMoviesRemote(movieType: String) {

        val call: Call<MovieResponse> = apiInterface.getMovie(movieType, MyConstants.API_KEY)

        call.enqueue(object : Callback<MovieResponse> {

            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {

                if (response.isSuccessful) {
                    moviesList = getMappedMovies(response.body()!!.results)
                    appDatabase.getMoviesDao().insertAllMovies(moviesList)
                    movies.postValue(moviesList)
                } else {
                    Log.e("MarvelResponseError", response.message())
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

                Log.e("Failure", t.message.toString())
            }

        })

    }

    fun initDatabase(context: Context) {
        if (!this::appDatabase.isInitialized)
            appDatabase = AppDatabase.getDatabase(context)
    }

    fun getMappedMovies(movies: List<Movie>): List<Movies> {

        val localMovies: MutableList<Movies> = mutableListOf()
        movies.forEach {
            localMovies.add(
                Movies(it.id, it.original_title, it.release_date, it.poster_path, it.vote_average)
            )
        }
        return localMovies
    }

}