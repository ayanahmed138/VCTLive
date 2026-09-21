package com.example.vctlive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vctlive.data.repository.MatchRepository
import com.example.vctlive.model.LiveMatch
import com.example.vctlive.model.UpcomingMatch
import com.example.vctlive.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



    class HomeViewModel(
        private val repository: MatchRepository
    ) : ViewModel() {

        private val _liveMatches = MutableStateFlow<List<LiveMatch>>(emptyList())
        val liveMatches: StateFlow<List<LiveMatch>> = _liveMatches

        private val _upcomingMatches = MutableStateFlow<List<UpcomingMatch>>(emptyList())
        val upcomingMatches: StateFlow<List<UpcomingMatch>> = _upcomingMatches

        private val _loading = MutableStateFlow(true)
        val loading: StateFlow<Boolean> = _loading

        private val _error = MutableStateFlow<String?>(null)
        val error: StateFlow<String?> = _error

        private val _followedMatches = MutableStateFlow<Set<String>>(emptySet())
        val followedMatches: StateFlow<Set<String>> = _followedMatches

        init {
            loadMatches()

            viewModelScope.launch {
                repository.followedMatches.collect {
                    _followedMatches.value = it
                }
            }
        }

        fun loadMatches() {

            viewModelScope.launch {

                _loading.value = true
                _error.value = null

                try {

                    _liveMatches.value =
                        RetrofitInstance.api.getLiveMatches()

                    _upcomingMatches.value =
                        RetrofitInstance.api.getUpcomingMatches()

                } catch (e: Exception) {

                    _error.value = e.message

                } finally {

                    _loading.value = false

                }
            }
        }
        fun toggleFollow(
            matchId: String,
            isFollowed: Boolean
        ) {
            viewModelScope.launch {
                repository.toggleFollow(matchId, isFollowed)
            }
        }
    }
