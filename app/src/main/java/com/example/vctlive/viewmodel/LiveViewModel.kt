package com.example.vctlive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vctlive.model.LiveMatch
import com.example.vctlive.repository.LiveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LiveViewModel : ViewModel() {

    private val repository = LiveRepository()

    private val _matches = MutableStateFlow<List<LiveMatch>>(emptyList())
    val matches: StateFlow<List<LiveMatch>> = _matches

    init {
        loadMatches()
    }

    fun loadMatches() {
        viewModelScope.launch {
            try {
                _matches.value = repository.getLiveMatches()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}