package com.example.vctlive.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vctlive.ui.components.LiveMatchCard
import com.example.vctlive.model.LiveMatch
import com.example.vctlive.network.RetrofitInstance
import com.example.vctlive.model.UpcomingMatch
import com.example.vctlive.ui.components.SectionHeader
import com.example.vctlive.ui.components.UpcomingMatchCard
import com.example.vctlive.ui.components.HomeHeader
import com.example.vctlive.ui.util.extractMatchId
import com.example.vctlive.ui.state.AppState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.vctlive.viewmodel.HomeViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.vctlive.data.datastore.UserPreferences
import com.example.vctlive.data.repository.MatchRepository
import com.example.vctlive.viewmodel.HomeViewModelFactory

@Composable
    fun HomeScreen() {
    val context = LocalContext.current

    val repository = remember {
        MatchRepository(
            UserPreferences(context)
        )
    }

    val factory = remember {
        HomeViewModelFactory(repository)
    }

    val viewModel: HomeViewModel = viewModel(factory = factory)

    val matches by viewModel.liveMatches.collectAsState()
    val upcomingMatches by viewModel.upcomingMatches.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val followedMatches by viewModel.followedMatches.collectAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }


                error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: $error",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 40.dp, start = 12.dp, end = 12.dp, bottom = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            HomeHeader("VCT Live", "Follow your favorite teams")
                        }
                        item {
                            SectionHeader("\uD83D\uDD34 Live Now")
                        }

                        if (matches.isEmpty()) {
                            item {
                                Text(
                                    text = "No live matches currently",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }
                        } else {
                            items(matches) { match ->

                                val matchId = match.matchId
                                val isFollowed = followedMatches.contains(matchId)

                                LiveMatchCard(
                                    match = match,
                                    isFollowed = isFollowed,
                                    onFollowClick = {
                                        viewModel.toggleFollow(matchId, isFollowed)
                                    }
                                )
                            }
                        }


                        item {
                            HomeHeader("VCT Live", "Never miss a Valorant match")
                        }

                        item {
                            SectionHeader("\uD83D\uDCC5 Upcoming")
                        }

                        items(upcomingMatches) { match ->

                            val matchId = extractMatchId(match.matchPage)
                            val isFollowed = followedMatches.contains(matchId)

                            UpcomingMatchCard(
                                match = match,
                                isFollowed = isFollowed,
                                onFollowClick = {
                                    viewModel.toggleFollow(matchId, isFollowed)
                                }
                            )
                        }

                    }
                }
            }
        }
    }
