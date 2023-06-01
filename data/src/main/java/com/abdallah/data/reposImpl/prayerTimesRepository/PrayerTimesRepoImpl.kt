package com.abdallah.data.reposImpl.prayerTimesRepository

import com.abdallah.domain.model.DataItemDTO
import com.abdallah.domain.repos.prayerTimesRepository.PrayerTimesOfflineDataSource
import com.abdallah.domain.repos.prayerTimesRepository.PrayerTimesOnlineDataSource
import com.abdallah.domain.repos.prayerTimesRepository.PrayerTimesRepo
import com.abdallah.domain.utils.networkHandler.NetworkHandler
import javax.inject.Inject

class PrayerTimesRepoImpl @Inject constructor(
    private val prayerTimesOnlineDataSource: PrayerTimesOnlineDataSource,
    private val prayerTimesOfflineDataSource: PrayerTimesOfflineDataSource,
    private val networkHandler: NetworkHandler
) :
    PrayerTimesRepo {


    override suspend fun getPrayerTimesByMonth(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double,
        method: Int
    ): List<DataItemDTO?>? {

        try {

            if (networkHandler.isOnline()) {

                val result = prayerTimesOnlineDataSource.getPrayerTimesByMonth(
                    year,
                    month,
                    latitude,
                    longitude,
                    method
                )
               prayerTimesOfflineDataSource.updatePrayerTimes(result)

                return result

            }

            return prayerTimesOfflineDataSource.getPrayerTimes()

        } catch (ex: Exception) {
            return prayerTimesOfflineDataSource.getPrayerTimes()
        }

    }
}