package com.example.vctlive.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.vctlive.service.LiveMatchService
import com.example.vctlive.ui.components.HomeHeader
import com.example.vctlive.ui.components.LiveMatchCard
import com.example.vctlive.ui.components.ScreenState
import com.example.vctlive.ui.components.SectionHeader
import com.example.vctlive.ui.components.UpcomingMatchCard
import com.example.vctlive.ui.util.extractMatchId
import com.example.vctlive.viewmodel.HomeViewModel
import androidx.compose.material3.Button
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.NotificationManagerCompat
import com.example.vctlive.notification.NotificationHelper
import com.example.vctlive.service.TeamsIconBuilder
import com.example.vctlive.ui.util.toTeamIconUrl
import kotlinx.coroutines.launch
import com.example.vctlive.service.LogoCache
import com.example.vctlive.service.MatchCardImageBuilder
import com.example.vctlive.network.RetrofitInstance
@Composable
fun FollowingScreen(viewModel: HomeViewModel) {

    val context = LocalContext.current

    val liveMatches by viewModel.liveMatches.collectAsState()
    val upcomingMatches by viewModel.upcomingMatches.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val followedMatches by viewModel.followedMatches.collectAsState()

    // Whenever something is followed, make sure the Live Update service is running.
    // It checks the followed list itself on every poll and stops on its own once
    // nothing followed is live anymore — nothing to do here to stop it.
    LaunchedEffect(followedMatches) {
        if (followedMatches.isNotEmpty()) {
            ContextCompat.startForegroundService(
                context,
                Intent(context, LiveMatchService::class.java)
            )
        }
    }

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


            )
            {
                Text(
                    text = "⭐ No followed matches yet",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Follow a live match and its score will show up here as a pill",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                val scope = rememberCoroutineScope()

                Button(onClick = {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        scope.launch {
                            val team1LogoUrl = "${RetrofitInstance.BASE_URL.trimEnd('/')}/team-icons/2.webp"
                            val team2LogoUrl = "${RetrofitInstance.BASE_URL.trimEnd('/')}/team-icons/624.webp"

                            val logo1 = LogoCache.get(team1LogoUrl)
                            val logo2 = LogoCache.get(team2LogoUrl)

                            val smallIcon = TeamsIconBuilder.build(
                                "Sentinels", team1LogoUrl,
                                "Paper Rex", team2LogoUrl
                            )

                            val card = MatchCardImageBuilder.build(
                                team1Name = "Sentinels",
                                team1Logo = logo1,
                                team2Name = "Paper Rex",
                                team2Logo = logo2,
                                score = "5 - 3",
                                mapLine = "Map 2 • Haven",
                                event = "Masters Toronto"
                            )

                            NotificationManagerCompat.from(context).notify(
                                LiveMatchService.NOTIFICATION_ID,
                                NotificationHelper.buildLiveUpdateNotification(
                                    context = context,
                                    title = "Sentinels vs Paper Rex",
                                    score = "5 - 3",
                                    detail = "Map 2 • Haven",
                                    event = "Masters Toronto",
                                    largeIcon = smallIcon

                                )
                            )
                        }
                    }
                }) {
                    Text("Test Pill")
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
