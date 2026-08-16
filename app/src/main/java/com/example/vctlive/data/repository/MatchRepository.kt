package com.example.vctlive.data.repository

import com.example.vctlive.data.datastore.UserPreferences

class MatchRepository(
    private val preferences: UserPreferences
) {


    val followedMatches =
        preferences.followedMatches


    suspend fun toggleFollow(
        matchId: String,
        isFollowed: Boolean
    ) {
        if (isFollowed)
            preferences.unfollowMatch(matchId)
        else
            preferences.followMatch(matchId)
    }

}