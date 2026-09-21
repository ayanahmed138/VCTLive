package com.example.vctlive.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.vctlive.notification.NotificationHelper
import com.example.vctlive.ui.components.HomeHeader
import com.example.vctlive.ui.components.LiveMatchCard
import com.example.vctlive.ui.components.ScreenState
import com.example.vctlive.ui.components.SectionHeader
import com.example.vctlive.ui.components.UpcomingMatchCard
import com.example.vctlive.ui.util.extractMatchId
import com.example.vctlive.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun FollowingScreen(viewModel: HomeViewModel) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val liveMatches by viewModel.liveMatches.collectAsState()
    val upcomingMatches by viewModel.upcomingMatches.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val followedMatches by viewModel.followedMatches.collectAsState()

    val followedLive = liveMatches.filter {
        followedMatches.contains(it.matchId)
    }

    val followedUpcoming = upcomingMatches.filter {
        followedMatches.contains(extractMatchId(it.matchPage))
    }

    ScreenState(
        loading = loading,
        error = error,
        onRetry = { viewModel.loadMatches() }
    ) {

        if (followedLive.isEmpty() && followedUpcoming.isEmpty()) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⭐ No followed matches yet",
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Test button for the notification (remove when the real one is wired up)
                Button(
                    onClick = {
                        scope.launch {
                            NotificationHelper.showLiveNotification(
                                context,
                                "Sentinels",
                                "Paper Rex",
                                "5 - 7",
                                "Map 2 • Haven",
                                "Series 1 - 0",
                                "Masters Toronto"
                            )
                        }
                    }
                ) {
                    Text("Show Live Notification")
                }
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, start = 12.dp, end = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {
                    HomeHeader("Following", "Matches you're tracking")
                }

                if (followedLive.isNotEmpty()) {

                    item {
                        SectionHeader("\uD83D\uDD34 Live")
                    }

                    items(followedLive, key = { it.matchId }) { match ->
                        LiveMatchCard(
                            match = match,
                            isFollowed = true,
                            onFollowClick = {
                                viewModel.toggleFollow(match.matchId, true)
                            }
                        )
                    }
                }

                if (followedUpcoming.isNotEmpty()) {

                    item {
                        SectionHeader("\uD83D\uDCC5 Upcoming")
                    }

                    items(followedUpcoming, key = { it.matchPage }) { match ->
                        UpcomingMatchCard(
                            match = match,
                            isFollowed = true,
                            onFollowClick = {
                                viewModel.toggleFollow(
                                    extractMatchId(match.matchPage),
                                    true
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
