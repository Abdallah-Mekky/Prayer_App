package com.abdallah.domain.repos.qiblaRepository

import com.abdallah.domain.model.DataDTO

interface QiblaOnlineDataSource {


    suspend fun getQiblaDirectionsByCurrentLocation(
        latitude: Double,
        longitude: Double
    ): DataDTO?
}