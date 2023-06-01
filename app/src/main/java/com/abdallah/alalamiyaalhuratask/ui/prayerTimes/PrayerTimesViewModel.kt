package com.abdallah.alalamiyaalhuratask.ui.prayerTimes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abdallah.alalamiyaalhuratask.Constants
import com.abdallah.alalamiyaalhuratask.model.CustomPrayerTiming
import com.abdallah.domain.model.DataItemDTO
import com.abdallah.domain.repos.prayerTimesRepository.PrayerTimesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PrayerTimesViewModel @Inject constructor(
    private val prayerTimesRepository: PrayerTimesRepo
) : ViewModel() {


    private lateinit var customTimingList: ArrayList<DataItemDTO>

    private val PrayerTimingCurrentDayMutableLiveData: MutableLiveData<ArrayList<CustomPrayerTiming>> =
        MutableLiveData<ArrayList<CustomPrayerTiming>>()
    val PrayerTimingCurrenDayLiveData: LiveData<ArrayList<CustomPrayerTiming>> =
        PrayerTimingCurrentDayMutableLiveData

    private val hourOfNextPrayerMutableLiveData: MutableLiveData<Int> = MutableLiveData<Int>()
    val hourOfNextPrayerLiveData: LiveData<Int> = hourOfNextPrayerMutableLiveData

    private val minuteOfNextPrayerMutableLiveData: MutableLiveData<Int> = MutableLiveData<Int>()
    val minuteOfNextPrayerLiveData: LiveData<Int> = minuteOfNextPrayerMutableLiveData

    private val totalMillisOfNextPrayerMutableLiveData: MutableLiveData<Long> =
        MutableLiveData<Long>()
    val totalMillisOfNextPrayerLiveData: LiveData<Long> = totalMillisOfNextPrayerMutableLiveData


    fun extractHourAndMinuteFromTime(time: String) {
        val simpleDateFormat = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        val date = simpleDateFormat.parse(time)
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }

        hourOfNextPrayerMutableLiveData.value = calendar.get(Calendar.HOUR_OF_DAY)
        minuteOfNextPrayerMutableLiveData.value = calendar.get(Calendar.MINUTE)
    }

    fun calcTotalMillis(hour: Int, minute: Int) {
        val totalMinutes = (hour * 60) + minute
        totalMillisOfNextPrayerMutableLiveData.value =
            TimeUnit.MINUTES.toMillis(totalMinutes.toLong())
    }


    @Suppress("UNCHECKED_CAST")
    suspend fun getPrayerTimes(
        year: Int,
        month: Int,
        currentDay: Int,
        latitude: Double,
        longitude: Double,
        method: Int,
        nextMonth: Boolean = false
    ) {
        val result =
            prayerTimesRepository.getPrayerTimesByMonth(year, month, latitude, longitude, method)


        Log.e("TAG", "getPrayerTimes: " + result?.get(0)?.timings?.fajr )
        if (!nextMonth) {
            storeSubsetOfMonth(result as List<DataItemDTO>, currentDay)

        } else {
            storeSubsetOfNextMonth(result as List<DataItemDTO>)
        }
    }

    private fun storeSubsetOfMonth(prayerTimingOfMonth: List<DataItemDTO>, currentDay: Int) {

        customTimingList = ArrayList()

        val indexOfCurrentDay = currentDay.minus(1)

        for (i in 0..7) {

            if (indexOfCurrentDay.plus(i) <= prayerTimingOfMonth.lastIndex) {

                customTimingList.add(prayerTimingOfMonth[indexOfCurrentDay.plus(i)])
            }
        }
    }

    private fun storeSubsetOfNextMonth(prayerTimingOfMonth: List<DataItemDTO>) {

        val numberOfStoredDays = customTimingList.size
        val numberOfRemainingDays = 7 - numberOfStoredDays

        for (i in 0..numberOfRemainingDays) {
            customTimingList.add(prayerTimingOfMonth[i])
        }
    }


    fun getPrayerTimesOfCurrentDay(currentDay: Int) {

        val customTimingListForCurrentDay = ArrayList<CustomPrayerTiming>()

        customTimingListForCurrentDay.add(
            CustomPrayerTiming(
                Constants.FAJR_PRAYER,
                customTimingList[currentDay].timings?.fajr?.let { formatTime(it) }
            )
        )
        customTimingListForCurrentDay.add(
            CustomPrayerTiming(
                Constants.SUBRISE_PRAYER,
                customTimingList[currentDay].timings?.sunrise?.let { formatTime(it) }
            )
        )
        customTimingListForCurrentDay.add(
            CustomPrayerTiming(
                Constants.DHUHR_PRAYER,
                customTimingList[currentDay].timings?.dhuhr?.let { formatTime(it) }
            )
        )
        customTimingListForCurrentDay.add(
            CustomPrayerTiming(
                Constants.ASR_PRAYER,
                customTimingList[currentDay].timings?.asr?.let { formatTime(it) }
            )
        )
        customTimingListForCurrentDay.add(
            CustomPrayerTiming(
                Constants.MAGHRIB_PRAYER,
                customTimingList[currentDay].timings?.maghrib?.let { formatTime(it) }
            )
        )
        customTimingListForCurrentDay.add(
            CustomPrayerTiming(
                Constants.ISHA_PRAYER,
                customTimingList[currentDay].timings?.isha?.let { formatTime(it) }
            )
        )

        Log.e("TAG", "getPrayerTimesOfCurrentDay: ${customTimingListForCurrentDay.get(0).prayerTime}", )

        PrayerTimingCurrentDayMutableLiveData.value = customTimingListForCurrentDay
    }


    private fun formatTime(time: String): String? {

        val simpleDateFormat24Hour = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        val time = simpleDateFormat24Hour.parse(time)

        val simpleDateFormat12Hour = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        return time?.let { simpleDateFormat12Hour.format(it) }
    }
}