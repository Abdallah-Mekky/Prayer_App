package com.abdallah.domain.repos.qiblaRepository

import com.abdallah.domain.model.DataDTO

interface QiblaRepo {


    suspend fun getQiblaDirections(
        latitude: Double,
        longitude: Double
    ): DataDTO?
}