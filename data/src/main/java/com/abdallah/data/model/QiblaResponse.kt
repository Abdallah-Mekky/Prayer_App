package com.abdallah.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abdallah.domain.model.DataDTO
import com.abdallah.domain.model.QiblaResponseDTO
import com.google.gson.annotations.SerializedName
import javax.annotation.Nonnull

data class QiblaResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null
){
	fun toQiblaResponse():QiblaResponseDTO{

		return QiblaResponseDTO(code,data?.toDataDTO(),status)
	}
}

@Entity
data class Data(

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo
	@Nonnull
	val id:Int,

	@ColumnInfo
	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@ColumnInfo
	@field:SerializedName("longitude")
	val longitude: Double? = null,

	@ColumnInfo
	@field:SerializedName("direction")
	val direction: Double? = null
){
	fun toDataDTO ():DataDTO{
		return DataDTO(id, latitude, longitude, direction)
	}
}
