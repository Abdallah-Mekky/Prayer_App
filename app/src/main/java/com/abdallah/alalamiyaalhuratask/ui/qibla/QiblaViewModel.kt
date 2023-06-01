package com.abdallah.alalamiyaalhuratask.ui.qibla

import android.util.Log
import androidx.lifecycle.ViewModel
import com.abdallah.alalamiyaalhuratask.model.LatLang
import com.abdallah.domain.repos.qiblaRepository.QiblaRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

@HiltViewModel
class QiblaViewModel @Inject constructor(
    private val qiblaRepo: QiblaRepo) : ViewModel() {


    suspend fun getQiblaDirections(
        latitude: Double,
        longitude: Double
    ): Double? {

        val result = qiblaRepo.getQiblaDirections(latitude, longitude)?.direction
        Log.e("TAG", "getQiblaDirections: " + result )
        return result
    }


    /*
    get the half distance between myLocationMarker and kaabaMarker,
     to put qibla direction marker between them
     */
    fun halfDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): LatLang {
        val R = 6371 // Earth radius in kilometers

        // Step 1: Compute the distance between the two locations using the Haversine formula
        val dLat = (lat2 - lat1).toRadians()
        val dLon = (lon2 - lon1).toRadians()
        val a =
            sin(dLat / 2).pow(2) + sin(dLon / 2).pow(2) * cos(lat1.toRadians()) * cos(lat2.toRadians())
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distance = R * c // Distance in kilometers

        // Step 2: Compute the midpoint between the two locations using the midpoint formula
        val Bx = cos(lat2.toRadians()) * cos(dLon)
        val By = cos(lat2.toRadians()) * sin(dLon)
        val lat3 = Math.atan2(
            sin(lat1.toRadians()) + sin(lat2.toRadians()),
            Math.sqrt((cos(lat1.toRadians()) + Bx) * (cos(lat1.toRadians()) + Bx) + By * By)
        )
        val lon3 = lon1 + Math.atan2(By, cos(lat1.toRadians()) + Bx)

        // Step 3: Compute the bearing from the first location to the second location using the bearing formula
        val y = sin(lon2.toRadians() - lon1.toRadians()) * cos(lat2.toRadians())
        val x =
            cos(lat1.toRadians()) * sin(lat2.toRadians()) - sin(lat1.toRadians()) * cos(lat2.toRadians()) * cos(
                lon2.toRadians() - lon1.toRadians()
            )
        val bearing = Math.atan2(y, x)

        // Step 4: Compute the latitudinal and longitudinal distances (in kilometers) from the first location to the point that is halfway between the two locations using the distance formula
        val halfDistance = distance / 2
        val latDist = halfDistance * cos(bearing)
        val lonDist = halfDistance * sin(bearing)

        // Step 5: Compute the latitude and longitude of the point that is halfway between the two locations using the destination point formula
        val lat4 = lat1 + (latDist / R).toDegrees()
        val lon4 = lon1 + (lonDist / (R * cos(lat1.toRadians()))).toDegrees()

        return LatLang(lat4, lon4)
    }

    private fun Double.toDegrees(): Double {
        return this * 180 / Math.PI
    }

    private fun Double.toRadians(): Double {
        return this * Math.PI / 180
    }
}