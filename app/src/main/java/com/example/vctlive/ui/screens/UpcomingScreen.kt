package com.example.vctlive.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vctlive.ui.components.HomeHeader
import com.example.vctlive.ui.components.ScreenState
import com.example.vctlive.ui.components.UpcomingMatchCard
import com.example.vctlive.ui.util.extractMatchId
import com.example.vctlive.viewmodel.HomeViewModel

@Composable
fun UpcomingScreen(viewModel: HomeViewModel) {

    val matches by viewModel.upcomingMatches.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val followedMatches by viewModel.followedMatches.collectAsState()

    ScreenState(
        loading = loading,
        error = error,
        onRetry = { viewModel.loadMatches() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, start = 12.dp, end = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                HomeHeader("Upcoming", "Never miss a Valorant match")
            }

            if (matches.isEmpty()) {
                item {
                    Text(
                        text = "No upcoming matches",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(matches, key = { it.matchPage }) { match ->

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
