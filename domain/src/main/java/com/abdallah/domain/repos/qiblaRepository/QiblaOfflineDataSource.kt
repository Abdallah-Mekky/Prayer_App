package com.abdallah.domain.repos.qiblaRepository

import com.abdallah.domain.model.DataDTO

interface QiblaOfflineDataSource {


    suspend fun getQiblaDirections(): DataDTO?

    suspend fun updateQiblaDirections(data: DataDTO?)
}