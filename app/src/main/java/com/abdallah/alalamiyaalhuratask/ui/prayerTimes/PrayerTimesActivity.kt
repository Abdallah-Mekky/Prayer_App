package com.abdallah.alalamiyaalhuratask.ui.prayerTimes

import android.Manifest
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.abdallah.alalamiyaalhuratask.Constants
import com.abdallah.alalamiyaalhuratask.Constants.REQUEST_CODE
import com.abdallah.alalamiyaalhuratask.R
import com.abdallah.alalamiyaalhuratask.databinding.ActivityMainBinding
import com.abdallah.alalamiyaalhuratask.model.CustomPrayerTiming
import com.abdallah.alalamiyaalhuratask.ui.qibla.QiblaActivity
import com.abdallah.alalamiyaalhuratask.ui.utils.LocationService
import com.abdallah.alalamiyaalhuratask.ui.utils.countDownControler.CountDownController
import com.google.android.gms.location.FusedLocationProviderClient
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class PrayerTimesActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {


    private lateinit var binding: ActivityMainBinding
    private val prayerTimesViewModel: PrayerTimesViewModel by viewModels()

    //Data of today
    private lateinit var calendar: Calendar
    private var currentMonth: Int = 0
    private var currentDay: Int = 0
    private var currentYear: Int = 0

    //To control clicks on back ,forward and get prayer times of today
    private var counterOfDay: Int = 0

    //Calculation method of prayer time
    private var selectedCalculationMethod: Int = 2

    //Data of current address
    private lateinit var countryName: String
    private lateinit var locality: String
    private lateinit var streetName: String


    //Necessary data to calculate next prayer
    private var currentHour: Int = 0
    private var currentMinute: Int = 0
    private var hourOfNextPrayer: Int = 0
    private var minuteOfNextPrayer: Int = 0
    private var hourDifference: Int = 0
    private var minuteDifference: Int = 0
    private var totalMillis: Long = 0
    private lateinit var nameOfNextPrayer: String

    @Inject
    lateinit var prayerTimesAdapter: PrayerTimesAdapter

    @Inject
    lateinit var locationServices: LocationService

    @Inject
    lateinit var countDownController: CountDownController

    //To get current location
    @Inject
    lateinit var mFusedLocationClient :FusedLocationProviderClient


    //check the permission of call with launcher
    private var locationsPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it[Manifest.permission.ACCESS_FINE_LOCATION] == true && it[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            // Permissions granted, do something here
            getLocation()
        } else {
            // Permissions denied, handle this case
            requestLocationPermissions()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        locationsPermissions.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
        subscribeToLiveData()
        getLocation()
        hideBackArrow()
        initCalendarCardView()
        getNextPrayer()
        onForwardArrowClick()
        onBackArrowClick()
        onQiblaClick()
    }

    override fun onResume() {
        super.onResume()
        initDropDownMenu()

    }

    private fun initDropDownMenu() {
        val dropDownItemsList = resources.getStringArray(R.array.prayerTimesCalculationMethod)
        val arrayAdapter = ArrayAdapter(this, R.layout.drobdown_menu_item, dropDownItemsList)

        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        (binding.textInputLayout.editText as (AutoCompleteTextView)).setOnItemClickListener { parent, view, position, id ->

            //reset view and variables
            counterOfDay = 0
            initCalendarCardView()
            hideBackArrow()
            showForwardArrow()
            selectedCalculationMethod = position
            getLocation()
        }
    }

    private fun getNextPrayer() {
        currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        currentMinute = calendar.get(Calendar.MINUTE)
    }

    private fun initCalendarCardView() {
        calendar = Calendar.getInstance()
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        currentMonth = (calendar.get(Calendar.MONTH)).plus(1)
        currentYear = calendar.get(Calendar.YEAR)

        binding.apply {
            currentDayTextView.text = currentDay.toString()
            currentMonthTextView.text = formatMonth(currentMonth)
            currentYearTextView.text = currentYear.toString()
        }
    }

    private fun onBackArrowClick() {

        binding.backArrow.setOnClickListener {

            if (counterOfDay != 0) {
                counterOfDay = counterOfDay.minus(1)
                prayerTimesViewModel.getPrayerTimesOfCurrentDay(counterOfDay)
                implementBackArrow()
                showForwardArrow()
            } else if (counterOfDay == 7) {
                hideForwardArrow()
            } else {
                hideBackArrow()
            }
        }
    }

    private fun onForwardArrowClick() {
        binding.forwardArrow.setOnClickListener {

            if (counterOfDay != 7) {
                counterOfDay = counterOfDay.plus(1)
                prayerTimesViewModel.getPrayerTimesOfCurrentDay(counterOfDay)
                Log.e("counter", "$counterOfDay")
                implementForwardArrow()
                showBackArrow()
            } else if (counterOfDay == 0) {
                hideBackArrow()
            } else {
                hideForwardArrow()
            }
        }
    }

    private fun implementForwardArrow() {

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        currentMonth = (calendar.get(Calendar.MONTH)).plus(1)
        currentYear = calendar.get(Calendar.YEAR)

        binding.apply {
            currentDayTextView.text = currentDay.toString()
            currentMonthTextView.text = formatMonth(currentMonth)
            currentYearTextView.text = currentYear.toString()
        }
    }

    private fun implementBackArrow() {
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        currentMonth = (calendar.get(Calendar.MONTH)).plus(1)
        currentYear = calendar.get(Calendar.YEAR)

        binding.apply {
            currentDayTextView.text = currentDay.toString()
            currentMonthTextView.text = formatMonth(currentMonth)
            currentYearTextView.text = currentYear.toString()
        }
    }

    private fun hideBackArrow() {
        binding.backArrow.visibility = View.GONE
    }

    private fun showBackArrow() {
        binding.backArrow.visibility = View.VISIBLE
    }

    private fun showForwardArrow() {
        binding.forwardArrow.visibility = View.VISIBLE
    }

    private fun hideForwardArrow() {
        binding.forwardArrow.visibility = View.GONE
    }

    private fun hasLocationPermissions() =
        EasyPermissions.hasPermissions(
            this, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestLocationPermissions() {
        EasyPermissions.requestPermissions(
            this,
            "This Permissions Are required to this app",
            REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {


        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        } else {
            requestLocationPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

            getLocation()
    }



    private fun getLocation() {

        if (hasLocationPermissions()) {
            if (locationServices.isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.ENGLISH)
                        locationServices.getAddressesListFromGeocoder(geocoder,location)
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestLocationPermissions()
        }
    }

    private fun setLocationDetailsInCalendarCardView(
        longitude: Double, latitude: Double, locality: String?, countryName: String?,
        streetName: String?, method: Int
    ) {

        Constants.longitude = longitude
        Constants.latitude = latitude

        validateAddressDetails(countryName,locality,streetName)

        runOnUiThread {
            binding.currentLocation.text = "${this.locality}, ${this.countryName}, ${this.streetName}"
        }

        getPrayerTimesBasedOnDayOfMonth(latitude, longitude, method)
    }

    private fun getPrayerTimesBasedOnDayOfMonth(latitude: Double, longitude: Double, method: Int) {
        lifecycleScope.launch {


            if (currentDay < 25) {
                prayerTimesViewModel.getPrayerTimes(
                    currentYear,
                    currentMonth,
                    currentDay,
                    latitude,
                    longitude,
                    method
                )
            } else {
                prayerTimesViewModel.getPrayerTimes(
                    currentYear,
                    currentMonth,
                    currentDay,
                    latitude,
                    longitude,
                    method
                )
                prayerTimesViewModel.getPrayerTimes(
                    currentYear, currentMonth.plus(1), currentDay, latitude, longitude,
                    method, true
                )
            }
            prayerTimesViewModel.getPrayerTimesOfCurrentDay(0)
        }
    }


    private fun subscribeToLiveData() {
        prayerTimesViewModel.PrayerTimingCurrenDayLiveData.observe(
            this
        ) { prayerTimesForSelectedDay ->

            Log.e("TAG", "subscribeToLiveData: ${prayerTimesForSelectedDay.size}")

            if (prayerTimesForSelectedDay != null) {
                calcNextPrayer(prayerTimesForSelectedDay)
                prayerTimesAdapter.refreshAdapter(prayerTimesForSelectedDay)
            }
            binding.prayerTimesRecyclerView.adapter = prayerTimesAdapter
        }

        prayerTimesViewModel.hourOfNextPrayerLiveData.observe(this) {

            hourOfNextPrayer = it
        }

        prayerTimesViewModel.minuteOfNextPrayerLiveData.observe(this) {
            minuteOfNextPrayer = it
        }

        prayerTimesViewModel.totalMillisOfNextPrayerLiveData.observe(this) {
            totalMillis = it
        }

        countDownController.remainingHoursLiveData.observe(this) { remainingHours ->
            binding.hours.text = "$remainingHours hr"
        }

        countDownController.remainingMinutesLiveData.observe(this) { remainingMinutes->
            binding.minutes.text = "$remainingMinutes min"
        }

        locationServices.calendarCardViewDetailsLiveData.observe(this) {
            setLocationDetailsInCalendarCardView(it.longitude!!,
            it.latitude!!,it.locality,it.countryName,it.thoroughfare,selectedCalculationMethod)
        }
    }

    private fun calcNextPrayer(prayersOfToday: ArrayList<CustomPrayerTiming>) {

        for (i in 0..prayersOfToday.size.minus(1)) {

            prayersOfToday[i].prayerTime?.let { prayerTimesViewModel.extractHourAndMinuteFromTime(it) }

            if (hourOfNextPrayer > currentHour) {

                calculateHourDifferenceAndMinuteDifference(prayersOfToday[i])
                break

            } else if (hourOfNextPrayer == currentHour) {

                if (minuteOfNextPrayer > currentMinute) {
                    calculateHourDifferenceAndMinuteDifference(prayersOfToday[i])
                    break

                } else if (minuteOfNextPrayer == currentMinute) {
                    i.plus(1)
                }
            } else if (currentHour > 21){

                prayersOfToday[0].prayerTime?.let {
                    prayerTimesViewModel.extractHourAndMinuteFromTime(
                        it
                    )
                }
                calculateHourDifferenceAndMinuteDifferenceFajr(prayersOfToday[0])
                break
            }
        }

        startCountDownTimer()
    }

    private fun startCountDownTimer(){
        if (!countDownController.getFlag()) {
            countDownController.startCountDownTimer(totalMillis) { subscribeToLiveData() }

        } else {
            countDownController.updateTimer(totalMillis) {subscribeToLiveData()}
        }
    }

    private fun calculateHourDifferenceAndMinuteDifference(prayersOfToday:CustomPrayerTiming){
        nameOfNextPrayer = prayersOfToday.prayerName.toString()
        binding.nextPrayerTextView.text = nameOfNextPrayer
        hourDifference = hourOfNextPrayer - currentHour
        minuteDifference = minuteOfNextPrayer - currentMinute
        prayerTimesViewModel.calcTotalMillis(hourDifference, minuteDifference)
    }

    private fun calculateHourDifferenceAndMinuteDifferenceFajr(prayersOfToday:CustomPrayerTiming){
        nameOfNextPrayer = prayersOfToday.prayerName.toString()
        binding.nextPrayerTextView.text = nameOfNextPrayer
        hourDifference =     currentHour - hourOfNextPrayer
        minuteDifference = minuteOfNextPrayer - currentMinute
        prayerTimesViewModel.calcTotalMillis(hourDifference, minuteDifference)
    }



    private fun onQiblaClick() {

        binding.qiblaButton.setOnClickListener {
            val intent = Intent(this@PrayerTimesActivity, QiblaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateAddressDetails(countryName: String?,locality: String?,streetName: String?){

        if (countryName != null) {
            this.countryName = countryName
        }else{
            this.countryName = "Un named country"
        }

        if (locality != null) {
            this.locality = locality
        }else{
            this.locality = "Un named city"
        }

        if (streetName != null) {
            this.streetName = streetName
        } else {

            this.streetName = "Un named street"
        }
    }

    private fun formatMonth(numberOfMonth:Int):String{

        return when (numberOfMonth){

            1 -> return "JAN"
            2 -> return "FEB"
            3 -> return "MAR"
            4 -> return "APR"
            5 -> return "MAY"
            6 -> return "JUN"
            7 -> return "JUL"
            8 -> return "AUG"
            9 -> return "SEP"
            10 -> return "OCT"
            11 -> return "NOV"
            12 -> return "DEC"
            else -> throw IllegalArgumentException("Invalid month number: $numberOfMonth")
        }

    }
}