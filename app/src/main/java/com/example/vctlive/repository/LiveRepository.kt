package com.example.vctlive.repository

import com.example.vctlive.model.LiveMatch
import com.example.vctlive.network.RetrofitInstance

class LiveRepository {

    suspend fun getLiveMatches(): List<LiveMatch> {
        return RetrofitInstance.api.getLiveMatches()
    }

}