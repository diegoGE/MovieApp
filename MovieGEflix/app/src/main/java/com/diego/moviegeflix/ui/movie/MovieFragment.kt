package com.diego.moviegeflix.ui.movie

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.diego.moviegeflix.R
import com.diego.moviegeflix.core.Resource
import com.diego.moviegeflix.data.local.AppDatabase
import com.diego.moviegeflix.data.local.LocalMovieDataSource
import com.diego.moviegeflix.data.model.Movie
import com.diego.moviegeflix.data.remote.RemoteMovieDataSource
import com.diego.moviegeflix.databinding.FragmentMovieBinding
import com.diego.moviegeflix.presentation.MovieViewModel
import com.diego.moviegeflix.presentation.MovieViewModelFactory
import com.diego.moviegeflix.repository.MovieRepositoryImpl
import com.diego.moviegeflix.repository.RetrofitClient
import com.diego.moviegeflix.ui.movie.adapters.MovieAdapter
import com.diego.moviegeflix.ui.movie.adapters.concat.PopularConcatAdapter
import com.diego.moviegeflix.ui.movie.adapters.concat.TopRatedConcatAdapter
import com.diego.moviegeflix.ui.movie.adapters.concat.UpcomingConcatAdapter


class MovieFragment : Fragment(R.layout.fragment_movie), MovieAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentMovieBinding
    private val viewModel by viewModels<MovieViewModel> {
        MovieViewModelFactory(
            MovieRepositoryImpl(
                RemoteMovieDataSource(RetrofitClient.webservice),
                LocalMovieDataSource(AppDatabase.getDatabase(requireContext()).movieDao())
            )
        )
    }

    private lateinit var concatAdapter: ConcatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieBinding.bind(view)
        concatAdapter = ConcatAdapter()
        viewModel.fetchMainScreenMovies().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    //Log.d("LiveData", "Loading... ")
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    concatAdapter.apply {
                        addAdapter(
                            0,
                            UpcomingConcatAdapter(
                                MovieAdapter(
                                    result.data.first.results,
                                    this@MovieFragment
                                )
                            )
                        )
                        addAdapter(
                            1,
                            TopRatedConcatAdapter(
                                MovieAdapter(
                                    result.data.second.results,
                                    this@MovieFragment
                                )
                            )
                        )
                        addAdapter(
                            2,
                            PopularConcatAdapter(
                                MovieAdapter(
                                    result.data.third.results,
                                    this@MovieFragment
                                )
                            )
                        )
                    }
                    //Log.d("LiveData", "Upcoming:  ${result.data.first}")
                    //Log.d("LiveData", "TopRated:  ${result.data.second}")
                    //Log.d("LiveData", "Popular:  ${result.data.third}")
                    binding.rvMovies.adapter = concatAdapter
                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("Error", "Error: ${result.exception} ")
                }
            }
        })

    }

    override fun onMovieClick(movie: Movie) {
        val action = MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(
            movie.poster_path,
            movie.backdrop_path,
            movie.vote_average.toFloat(),
            movie.vote_count,
            movie.overview,
            movie.title,
            movie.original_language,
            movie.release_date
        )
        findNavController().navigate(action)
    }

}