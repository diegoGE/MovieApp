package com.diego.moviegeflix.repository

import com.diego.moviegeflix.data.model.MovieList

interface MovieRepository {
    suspend fun getUpcomingMovies(): MovieList
    suspend fun getTopRatedMovies(): MovieList
    suspend fun getPopularMovies(): MovieList
}