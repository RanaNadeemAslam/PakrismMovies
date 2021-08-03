package com.fhatechnology.pakrismmovies.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.fhatechnology.pakrismmovies.repositories.RepositoryMovieDetailsData
import com.fhatechnology.pakrismmovies.remoteSource.MovieResponseDetails

class MovieDetailsVM(application: Application) : AndroidViewModel(application) {

    fun getMovieDetails(movieId : String) : LiveData<MovieResponseDetails> {
        return RepositoryMovieDetailsData.getMovieDetail(movieId)
    }
}