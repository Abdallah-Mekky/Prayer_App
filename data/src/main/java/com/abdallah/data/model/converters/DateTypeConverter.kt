//package com.abdallah.data.model.converters
//
//import androidx.room.TypeConverter
//import com.abdallah.data.model.Date
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//
//class DateTypeConverter {
//
//    @TypeConverter
//    fun fromDate(value: Date): String {
//        return Gson().toJson(value)
//    }
//
//    @TypeConverter
//    fun toDate(value: String): Date {
//        val type = object : TypeToken<Date>() {}.type
//        return Gson().fromJson(value, type)
////        return Gson().fromJson(value, Date::class.java)
//    }
//}