package com.example.tmdb.ui.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tmdb.databinding.FragmentFavoriteBinding
import com.example.tmdb.ui.fragments.movieList.adapter.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding

    @Inject
    lateinit var movieAdapter: MovieAdapter
    val viewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        addMovieItemClickListener()
    }

    private fun addMovieItemClickListener() {
        movieAdapter.onItemClick = { movieId ->
            val detailFragmentDirection =
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(movieId)

            findNavController().navigate(detailFragmentDirection)
        }
    }

    private fun initializeRecyclerView() {
        movieAdapter.isFavorite = true
        binding.rvFavoriteMovies.adapter = movieAdapter
    }

}