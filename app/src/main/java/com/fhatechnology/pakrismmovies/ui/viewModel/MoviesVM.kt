package com.fhatechnology.pakrismmovies.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.fhatechnology.pakrismmovies.repositories.RepositoryData
import com.fhatechnology.pakrismmovies.model.Movies

class MoviesVM(application: Application) : AndroidViewModel(application) {

    init {
        RepositoryData.initDatabase(application)
    }

    fun getMovies(type: String, haveNetworkConnection: Boolean) : LiveData<List<Movies>>{
        return RepositoryData.getMovies(type, haveNetworkConnection)
    }

}