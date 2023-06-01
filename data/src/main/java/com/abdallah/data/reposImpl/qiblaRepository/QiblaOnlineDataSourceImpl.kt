package com.abdallah.data.reposImpl.qiblaRepository

import com.abdallah.data.api.WepServices
import com.abdallah.data.utils.Utils.convertTo
import com.abdallah.domain.model.DataDTO
import com.abdallah.domain.repos.qiblaRepository.QiblaOnlineDataSource
import javax.inject.Inject

class QiblaOnlineDataSourceImpl @Inject constructor(private val wepServices: WepServices) :
    QiblaOnlineDataSource {
    override suspend fun getQiblaDirectionsByCurrentLocation(
        latitude: Double,
        longitude: Double
    ): DataDTO? {

        try {

            return wepServices.getQiblaByCurrentLocation(latitude, longitude).toQiblaResponse().dataDTO
        } catch (ex: Exception) {
            throw ex
        }
    }
}