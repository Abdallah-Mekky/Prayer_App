package com.abdallah.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.abdallah.data.model.converters.TimingsTypeConverter
import com.abdallah.domain.model.DataItemDTO
import com.abdallah.domain.model.PrayerTimesResponseDTO
import com.abdallah.domain.model.TimingsDTO
import com.google.gson.annotations.SerializedName

data class PrayerTimesResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    var data: List<DataItem?>? = null,

    @field:SerializedName("status")
    val status: String? = null
){


    fun toConvertDTO():PrayerTimesResponseDTO{
        return PrayerTimesResponseDTO(code = code,data = data?.toDataListDTO(),status = status)
    }

    private fun List<DataItem?>.toDataListDTO(): List<DataItemDTO?> {

        val dataList:MutableList<DataItemDTO?>? = mutableListOf()

        this.map {
            dataList?.add(it?.toDataItemDTO())
        }

        return dataList?.toList() ?: emptyList<DataItemDTO>()
    }
}


@TypeConverters(TimingsTypeConverter::class)
@Entity
data class DataItem(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int,

    @ColumnInfo
    @field:SerializedName("timings")
    val timings: Timings? = null

){

    fun toDataItemDTO():DataItemDTO{
        return DataItemDTO(id = id, timings = timings?.toTimingsDTO())
    }
}

data class Timings(

    @field:SerializedName("Sunset")
    val sunset: String? = null,

    @field:SerializedName("Asr")
    val asr: String? = null,

    @field:SerializedName("Isha")
    val isha: String? = null,

    @field:SerializedName("Fajr")
    val fajr: String? = null,

    @field:SerializedName("Dhuhr")
    val dhuhr: String? = null,

    @field:SerializedName("Maghrib")
    val maghrib: String? = null,

    @field:SerializedName("Lastthird")
    val lastthird: String? = null,

    @field:SerializedName("Firstthird")
    val firstthird: String? = null,

    @field:SerializedName("Sunrise")
    val sunrise: String? = null,

    @field:SerializedName("Midnight")
    val midnight: String? = null,

    @field:SerializedName("Imsak")
    val imsak: String? = null
){

    fun toTimingsDTO():TimingsDTO{
        return TimingsDTO(sunset, asr, isha, fajr, dhuhr, maghrib, lastthird, firstthird, sunrise, midnight, imsak)
    }
}
