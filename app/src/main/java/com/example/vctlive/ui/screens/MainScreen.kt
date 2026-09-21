package com.example.vctlive.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vctlive.data.datastore.UserPreferences
import com.example.vctlive.data.repository.MatchRepository
import com.example.vctlive.ui.navigation.BottomBar
import com.example.vctlive.viewmodel.HomeViewModel
import com.example.vctlive.viewmodel.HomeViewModelFactory


@Composable
fun MainScreen() {

    val context = LocalContext.current
    val navController = rememberNavController()

    // ONE view model, created here and shared by all three tabs
    val factory = remember {
        HomeViewModelFactory(
            MatchRepository(UserPreferences(context))
        )
    }
    val viewModel: HomeViewModel = viewModel(factory = factory)

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Live.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(BottomNavItem.Live.route) {
                LiveScreen(viewModel)
            }

            composable(BottomNavItem.Upcoming.route) {
                UpcomingScreen(viewModel)
            }

            composable(BottomNavItem.Following.route) {
                FollowingScreen(viewModel)
            }
        }
    }
}
