package com.abdallah.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abdallah.data.model.DataItem

@Dao
interface PrayerTimesDao {


    @Query("select * from DataItem")
    suspend fun getPrayerTimes(): List<DataItem?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePrayerTimes(prayerTimesList: List<DataItem?>?)
}