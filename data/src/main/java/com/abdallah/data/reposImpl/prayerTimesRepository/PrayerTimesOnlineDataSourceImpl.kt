package com.abdallah.data.reposImpl.prayerTimesRepository

import com.abdallah.data.api.WepServices
import com.abdallah.domain.model.DataItemDTO
import com.abdallah.domain.repos.prayerTimesRepository.PrayerTimesOnlineDataSource
import javax.inject.Inject

class PrayerTimesOnlineDataSourceImpl @Inject constructor(private val wepServices: WepServices) :
    PrayerTimesOnlineDataSource {


    override suspend fun getPrayerTimesByMonth(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double,
        method: Int
    ): List<DataItemDTO?>? {

        try {
            val result = wepServices.getPrayerTimesByMonth(year, month, latitude, longitude, method)
            return result.toConvertDTO().data
        } catch (ex: Exception) {
            throw ex
        }
    }
}