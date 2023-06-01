//package com.abdallah.data.model.converters
//
//import androidx.room.TypeConverter
//import com.abdallah.data.model.Meta
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//
//class MetaTypeConverter {
//
//    @TypeConverter
//    fun fromMeta(value: Meta): String {
//        return Gson().toJson(value)
//    }
//
//    @TypeConverter
//    fun toMeta(value: String): Meta {
//        val type = object : TypeToken<Meta>() {}.type
//        return Gson().fromJson(value, type)
//    }
//}