package com.example.vctlive.ui.screens


import android.content.Intent
import android.graphics.Bitmap
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vctlive.data.datastore.UserPreferences
import com.example.vctlive.data.repository.MatchRepository
import com.example.vctlive.notification.NotificationHelper
import com.example.vctlive.service.LiveMatchService
import com.example.vctlive.viewmodel.HomeViewModel
import com.example.vctlive.viewmodel.HomeViewModelFactory
import android.graphics.BitmapFactory
import androidx.compose.runtime.rememberCoroutineScope

import com.example.vctlive.ui.components.LiveMatchCard
import com.example.vctlive.ui.components.UpcomingMatchCard
import com.example.vctlive.ui.util.extractMatchId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

suspend fun bitmapFromUrl(url: String): Bitmap =
    withContext(Dispatchers.IO) {
        URL(url).openStream().use {
            BitmapFactory.decodeStream(it)
        }
    }
@Composable
fun FollowingScreen() {

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

    val liveMatches by viewModel.liveMatches.collectAsState()
    val upcomingMatches by viewModel.upcomingMatches.collectAsState()
    val followedMatches by viewModel.followedMatches.collectAsState()

    val followedLive = liveMatches.filter {
        followedMatches.contains(it.matchId)
    }

    val followedUpcoming = upcomingMatches.filter {
        followedMatches.contains(extractMatchId(it.matchPage))
    }

    if (followedLive.isEmpty() && followedUpcoming.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("⭐ No followed matches yet")
        }
        Column {

//            Button(
//                onClick = {
//                    NotificationHelper.showTestNotification(context)
//                }
//            ) {
//                Text("Score 1")
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Button(
//                onClick = {
//                    NotificationHelper.showLiveNotification(
//                        context,
//                        "🔴 LIVE",
//                        "Sentinels 2 - 0 PRX",
//                        "Match Finished",
//                        "Masters Toronto"
//                    )
//                }
//            ) {
//                Text("Score 2")
//            }

            Spacer(modifier = Modifier.height(8.dp))
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (followedLive.isNotEmpty()) {

                item {
                    Text(
                        "🔴 Live",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                items(followedLive) { match ->

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
                    Text(
                        "📅 Upcoming",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                items(followedUpcoming) { match ->

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