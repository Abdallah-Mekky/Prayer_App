package com.abdallah.data.utils

import com.abdallah.data.model.DataItem
import com.abdallah.data.model.Timings
import com.abdallah.domain.model.DataItemDTO
import com.abdallah.domain.model.TimingsDTO
import com.google.gson.Gson

object Utils {

    fun <T> Any.convertTo(clazz: Class<T>): T {

        val jsonString = Gson().toJson(this)

        return Gson().fromJson(jsonString, clazz)
    }


    fun DataItemDTO.toDataItem():DataItem{

        return DataItem(id,timings?.toTimings())
    }

    fun TimingsDTO.toTimings():Timings{

        return Timings(sunset, asr, isha, fajr, dhuhr, maghrib, lastthird, firstthird, sunrise, midnight, imsak)
    }
}