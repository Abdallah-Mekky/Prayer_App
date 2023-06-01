package com.abdallah.domain.model


data class PrayerTimesResponseDTO(

    val code: Int? = null,
    val data: List<DataItemDTO?>? = null,
    val status: String? = null
)

data class DataItemDTO(
    val id: Int,
    var timings: TimingsDTO? = null
)

data class TimingsDTO(

    val sunset: String? = null,
    val asr: String? = null,
    val isha: String? = null,
    val fajr: String? = null,
    val dhuhr: String? = null,
    val maghrib: String? = null,
    val lastthird: String? = null,
    val firstthird: String? = null,
    val sunrise: String? = null,
    val midnight: String? = null,
    val imsak: String? = null
)
