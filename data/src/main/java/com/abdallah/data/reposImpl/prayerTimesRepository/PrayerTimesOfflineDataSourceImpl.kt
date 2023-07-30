package com.abdallah.data.reposImpl.prayerTimesRepository

import com.abdallah.data.database.MyDataBase
import com.abdallah.data.utils.Utils.toDataItem
import com.abdallah.domain.model.DataItemDTO
import com.abdallah.domain.repos.prayerTimesRepository.PrayerTimesOfflineDataSource
import javax.inject.Inject

class PrayerTimesOfflineDataSourceImpl @Inject constructor(private val myDataBase: MyDataBase) :
    PrayerTimesOfflineDataSource {


    override suspend fun getPrayerTimes(): List<DataItemDTO?>? {
        try {
            return myDataBase.getPrayerTimesDao().getPrayerTimes()?.map {
                it?.toDataItemDTO()
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    override suspend fun updatePrayerTimes(prayerTimesList: List<DataItemDTO?>?) {
        try {

            val result = prayerTimesList?.map {
                it?.toDataItem()
            }
            myDataBase.getPrayerTimesDao().deletePrayerTimes()
            myDataBase.getPrayerTimesDao().updatePrayerTimes(result)
        } catch (ex: Exception) {
            throw ex
        }
    }
}