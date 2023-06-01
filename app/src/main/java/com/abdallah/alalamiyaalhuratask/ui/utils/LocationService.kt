package com.abdallah.alalamiyaalhuratask.ui.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.abdallah.alalamiyaalhuratask.model.CalendarCardViewDetails

class LocationService constructor(val context: Context) {

    lateinit var calendarCardViewDetails: CalendarCardViewDetails

    private val calendarCardViewDetailsMutableLiveData = MutableLiveData<CalendarCardViewDetails>()
    val calendarCardViewDetailsLiveData = calendarCardViewDetailsMutableLiveData

    fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
                )
    }

    fun getAddressesListFromGeocoder(geocoder: Geocoder, location: Location) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                location.latitude, location.longitude, 1
            ) { addresses ->

                calendarCardViewDetails = CalendarCardViewDetails(
                    addresses[0].longitude,
                    addresses[0].latitude, addresses[0].locality, addresses[0].countryName,
                    addresses[0].thoroughfare
                )

                calendarCardViewDetailsMutableLiveData.postValue(calendarCardViewDetails)
            }

        } else {
            val addressList: List<Address> = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            ) as List<Address>

            calendarCardViewDetails = CalendarCardViewDetails(
                addressList[0].longitude,
                addressList[0].latitude, addressList[0].locality, addressList[0].countryName,
                addressList[0].thoroughfare
            )

            calendarCardViewDetailsMutableLiveData.postValue(calendarCardViewDetails)
        }
    }
}