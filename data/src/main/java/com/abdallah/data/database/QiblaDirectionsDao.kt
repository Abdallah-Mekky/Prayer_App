package com.abdallah.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abdallah.data.model.Data

@Dao
interface QiblaDirectionsDao {


    @Query("select * from Data")
    suspend fun getQiblaDirection(): Data

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateQiblaDirection(data: Data?)
}