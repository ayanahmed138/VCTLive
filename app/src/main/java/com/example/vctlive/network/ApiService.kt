package com.example.vctlive.network

import com.example.vctlive.model.LiveMatch
import com.example.vctlive.model.UpcomingMatch
import retrofit2.http.GET

interface ApiService {

    @GET("api/LiveAction")
    suspend fun getLiveMatches(): List<LiveMatch>

    @GET("api/LiveAction/upcoming")
    suspend fun getUpcomingMatches(): List<UpcomingMatch>
}