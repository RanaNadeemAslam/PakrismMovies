package com.fhatechnology.pakrismmovies.ui

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhatechnology.pakrismmovies.R
import com.fhatechnology.pakrismmovies.adapter.MoviesAdapter
import com.fhatechnology.pakrismmovies.model.Movies
import com.fhatechnology.pakrismmovies.ui.viewModel.MoviesVM
import kotlinx.android.synthetic.main.activity_main.*

private lateinit var moviesViewModel : MoviesVM

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moviesViewModel = ViewModelProvider(this).get(MoviesVM::class.java)

        movieRecyclerView.layoutManager = LinearLayoutManager(this)

        getRecyclerViewData()
    }

    private fun getRecyclerViewData(){
        moviesViewModel.getMovies("top_rated", haveNetworkConnection()).observe(this, Observer {
            populateMovieRecycler(it)
        })
    }

    private fun populateMovieRecycler(moviesList: List<Movies>) {

        movieRecyclerView.adapter =
            MoviesAdapter(moviesList)
    }

    private fun haveNetworkConnection(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val netInfo = cm!!.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals(
                    "WIFI",
                    ignoreCase = true
                )
            ) if (ni.isConnected) haveConnectedWifi = true
            if (ni.typeName.equals(
                    "MOBILE",
                    ignoreCase = true
                )
            ) if (ni.isConnected) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }
}