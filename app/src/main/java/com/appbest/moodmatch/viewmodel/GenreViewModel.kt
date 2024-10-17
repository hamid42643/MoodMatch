package com.appbest.moodmatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appbest.moodmatch.model.Genre
import com.appbest.moodmatch.repository.GenreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GenreViewModel : ViewModel() {
    private val repository = GenreRepository()
    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres

    init {
        loadGenres()
    }

    private fun loadGenres() {
        viewModelScope.launch {
            _genres.value = repository.getGenres()
        }
    }
}
