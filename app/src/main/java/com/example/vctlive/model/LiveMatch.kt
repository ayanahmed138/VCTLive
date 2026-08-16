package com.example.vctlive.model

data class LiveMatch(
    val matchId: String,
    val team1: String,
    val team2: String,
    val team1Logo: String,
    val team2Logo: String,
    val seriesScore: String,
    val currentMapScore: String,
    val currentMap: String,
    val event: String,
    val status: String
)