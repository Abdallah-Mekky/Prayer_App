package com.abdallah.data.reposImpl.qiblaRepository

import com.abdallah.domain.model.DataDTO
import com.abdallah.domain.repos.qiblaRepository.QiblaOfflineDataSource
import com.abdallah.domain.repos.qiblaRepository.QiblaOnlineDataSource
import com.abdallah.domain.repos.qiblaRepository.QiblaRepo
import com.abdallah.domain.utils.networkHandler.NetworkHandler
import javax.inject.Inject

class QiblaRepoImpl @Inject constructor(
    private val qiblaOnlineDataSource: QiblaOnlineDataSource,
    private val qiblaOfflineDataSource: QiblaOfflineDataSource,
    private val networkHandler: NetworkHandler
) : QiblaRepo {


    override suspend fun getQiblaDirections(latitude: Double, longitude: Double): DataDTO? {
        try {

            if (networkHandler.isOnline()) {

                val result =
                    qiblaOnlineDataSource.getQiblaDirectionsByCurrentLocation(latitude, longitude)
                qiblaOfflineDataSource.updateQiblaDirections(result)
                return result
            }

            return qiblaOfflineDataSource.getQiblaDirections()
        } catch (ex: Exception) {
            return qiblaOfflineDataSource.getQiblaDirections()
        }
    }
}