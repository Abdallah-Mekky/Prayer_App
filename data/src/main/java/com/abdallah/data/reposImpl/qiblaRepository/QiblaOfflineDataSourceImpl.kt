package com.abdallah.data.reposImpl.qiblaRepository

import com.abdallah.data.database.MyDataBase
import com.abdallah.data.model.Data
import com.abdallah.data.utils.Utils.convertTo
import com.abdallah.domain.model.DataDTO
import com.abdallah.domain.repos.qiblaRepository.QiblaOfflineDataSource
import javax.inject.Inject

class QiblaOfflineDataSourceImpl @Inject constructor(private val myDataBase: MyDataBase) :
    QiblaOfflineDataSource {

    override suspend fun getQiblaDirections(): DataDTO? {

        try {
            return myDataBase.getQiblaDirectionsDao().getQiblaDirection().convertTo(DataDTO::class.java)
        } catch (ex: Exception) {
            throw ex
        }
    }

    override suspend fun updateQiblaDirections(data: DataDTO?) {
        try {

            return myDataBase.getQiblaDirectionsDao().updateQiblaDirection(data?.convertTo(Data::class.java))
        } catch (ex: Exception) {
            throw ex
        }
    }
}