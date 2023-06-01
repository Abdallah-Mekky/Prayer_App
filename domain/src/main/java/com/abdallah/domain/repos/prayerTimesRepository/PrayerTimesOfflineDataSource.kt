package com.abdallah.domain.repos.prayerTimesRepository

import com.abdallah.domain.model.DataItemDTO

interface PrayerTimesOfflineDataSource {


    suspend fun getPrayerTimes(): List<DataItemDTO?>?

    suspend fun updatePrayerTimes(prayerTimesList: List<DataItemDTO?>?)

}