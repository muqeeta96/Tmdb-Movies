package com.example.tmdb.ui.fragments.movieList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tmdb.databinding.FragmentMovieBinding
import com.example.tmdb.ui.activities.main.MainViewModel
import com.example.tmdb.ui.fragments.movieList.adapter.MovieAdapter
import com.example.tmdb.utils.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieFragment : Fragment() {
    private lateinit var binding: FragmentMovieBinding

    @Inject
    lateinit var movieAdapter: MovieAdapter
    private val movieViewModel: MovieViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private var query: String = ""
    var created = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onQueryListener()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerViewAdapter()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = movieViewModel

        onMovieClickListener()
        addLoadMoreListener()
        onFavoriteClickListener()

    }


    private fun onQueryListener() {
        mainViewModel.queryLiveData.observe(requireActivity()) {
            movieAdapter.clearData()
            movieViewModel.apply {

                page = 1
                isLoading.postValue(true)
                isResultFound.postValue(true)
                getSearchMovieResult(it)
            }

        }
    }

    private fun initializeRecyclerViewAdapter() {
        binding.rvMovies.adapter = movieAdapter
    }

    private fun addLoadMoreListener() {
        movieAdapter.loadMore = {
            if (NetworkUtils.isNetworkConnected(requireActivity())) {
                if (query.isEmpty()) {
                    movieViewModel.loadMoreMovies()
                } else {
                    movieViewModel.loadMoreWithSearch(query)
                }
            }
        }
    }

    private fun onMovieClickListener() {
        movieAdapter.onItemClick = { movieId ->
            val detailFragmentDirection =
                MovieFragmentDirections.actionMovieFragmentToDetailFragment(movieId)

            findNavController().navigate(detailFragmentDirection)
        }
    }

    private fun onFavoriteClickListener() {
        movieAdapter.onFavoriteClick = {
            if (it != null) {
                movieViewModel.addToFavorite(it)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "This movie has already added in favorite",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }
    }

}

