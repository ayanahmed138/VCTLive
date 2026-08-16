package com.example.vctlive.model

data class UpcomingMatch(

    val team1: String,
    val team2: String,
    val startsIn: String,
    val unixTimestamp: String,
    val event: String,
    val series: String,
    val matchPage: String
)