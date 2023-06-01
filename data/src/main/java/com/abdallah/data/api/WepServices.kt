package com.abdallah.data.api

import com.abdallah.data.model.PrayerTimesResponse
import com.abdallah.data.model.QiblaResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface WepServices {


    @GET("calendar/{year}/{month}")
    suspend fun getPrayerTimesByMonth(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") method: Int
    ): PrayerTimesResponse

    @GET("qibla/{latitude}/{longitude}")
    suspend fun getQiblaByCurrentLocation(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double
    ): QiblaResponse
}