package com.abdallah.domain.repos.prayerTimesRepository

import com.abdallah.domain.model.DataItemDTO

interface PrayerTimesOnlineDataSource {

    suspend fun getPrayerTimesByMonth(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double,
        method: Int
    ): List<DataItemDTO?>?
}