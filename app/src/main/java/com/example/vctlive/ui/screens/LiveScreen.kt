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
import com.example.vctlive.ui.components.LiveMatchCard
import com.example.vctlive.ui.components.ScreenState
import com.example.vctlive.viewmodel.HomeViewModel

@Composable
fun LiveScreen(viewModel: HomeViewModel) {

    val matches by viewModel.liveMatches.collectAsState()
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
                HomeHeader("Live Now", "Matches happening right now")
            }

            if (matches.isEmpty()) {
                item {
                    Text(
                        text = "No live matches currently",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(matches, key = { it.matchId }) { match ->

                    val isFollowed = followedMatches.contains(match.matchId)

                    LiveMatchCard(
                        match = match,
                        isFollowed = isFollowed,
                        onFollowClick = {
                            viewModel.toggleFollow(match.matchId, isFollowed)
                        }
                    )
                }
            }
        }
    }
}
