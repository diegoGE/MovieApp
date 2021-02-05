package com.diego.moviegeflix.ui.moviedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.diego.moviegeflix.R
import com.diego.moviegeflix.application.AppConstants
import com.diego.moviegeflix.databinding.FragmentMovieDetailBinding

class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private lateinit var binding: FragmentMovieDetailBinding
    private val args by navArgs<MovieDetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailBinding.bind(view)

        Glide.with(requireContext()).load("${AppConstants.BASE_URL_IMAGE}${args.posterImageUrl}").centerCrop().into(binding.imgMovie)
        Glide.with(requireContext()).load("${AppConstants.BASE_URL_IMAGE}${args.backgroundImageUrl}").centerCrop().into(binding.imgBackground)
        binding.txtDescription.text = args.overview
        binding.txtTitle.text = args.title
        binding.txtLanguage.text = "Language ${args.language}"
        binding.txtRating.text = "${args.voteAverage} (${args.voteCount} Reviews)"
        binding.txtReleased.text = "Relased ${args.releaseDate}"

    }

}