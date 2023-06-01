package com.abdallah.data.model.converters

import androidx.room.TypeConverter
import com.abdallah.data.model.Timings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TimingsTypeConverter {

    @TypeConverter
    fun fromTimings(value: Timings): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toTimings(value: String): Timings {
        val type = object : TypeToken<Timings>() {}.type
        return Gson().fromJson(value, type)
    }
}