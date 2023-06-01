package com.abdallah.domain.model

data class QiblaResponseDTO(

    val code: Int? = null,
    val dataDTO: DataDTO? = null,
    val status: String? = null
)

data class DataDTO(
    val id: Int,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val direction: Double? = null
)
