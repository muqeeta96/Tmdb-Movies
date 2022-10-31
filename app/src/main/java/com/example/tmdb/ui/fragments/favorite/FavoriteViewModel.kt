package com.example.tmdb.ui.fragments.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.domain.dataService.FavoriteService
import com.example.tmdb.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val favoriteService: FavoriteService) :
    ViewModel() {
    var favoriteMovies: MutableLiveData<List<Movie>> = MutableLiveData(ArrayList())
    val error: MutableLiveData<String?> = MutableLiveData()
    val isResultFound: MutableLiveData<Boolean> = MutableLiveData(true)

    init {
        getFavoriteMovies()
    }

    private fun getFavoriteMovies() {

        viewModelScope.launch(Dispatchers.Main) {
            val resource = favoriteService.getFavoriteMovies()
            if (resource.data != null) {
                isResultFound.postValue(true)
                favoriteMovies.postValue(resource.data as List<Movie>?)
            } else {
                isResultFound.postValue(false)
                error.postValue(resource.error)
            }
        }
    }
}