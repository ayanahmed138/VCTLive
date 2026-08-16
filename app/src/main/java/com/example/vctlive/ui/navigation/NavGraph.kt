package com.example.vctlive.ui.navigation

import android.graphics.drawable.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vctlive.ui.screens.FollowingScreen
import com.example.vctlive.ui.screens.HomeScreen
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.BlendMode.Companion.Color

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route
    ) {

        composable(BottomNavItem.Home.route) {
            HomeScreen()
        }

        composable(BottomNavItem.Following.route) {
            FollowingScreen()
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Following
    )

    NavigationBar {

        val currentRoute =
            navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label

                    )
                },
                label = {
                    Text(item.label)
                }
            )
        }
    }
}