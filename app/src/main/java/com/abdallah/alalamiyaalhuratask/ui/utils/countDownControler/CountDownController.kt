package com.abdallah.alalamiyaalhuratask.ui.utils.countDownControler

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CountDownController {

    private var timer: CountDownTimer? = null

    //To update count down timer by cancling first timer and create anthor one
    private var flag: Boolean = false

    private val remainingHoursMutableLiveData: MutableLiveData<Long> = MutableLiveData<Long>()
    val remainingHoursLiveData: LiveData<Long> = remainingHoursMutableLiveData

    private val remainingMinutesMutableLiveData: MutableLiveData<Long> = MutableLiveData<Long>()
    val remainingMinutesLiveData: LiveData<Long> = remainingMinutesMutableLiveData


    fun startCountDownTimer(totalMillis: Long, subscribeToLiveData: () -> Unit) {

        flag = true

        timer = object : CountDownTimer(totalMillis, 10000) {

            override fun onTick(millisUntilFinished: Long) {

                val minutes = (millisUntilFinished / 1000) / 60
                val hours = minutes / 60
                val remainingMinutes = minutes % 60

                Log.e("TAG", "startCountDownTimer: " + minutes + " re " + remainingMinutes)

                this@CountDownController.remainingHoursMutableLiveData.value = hours
                this@CountDownController.remainingMinutesMutableLiveData.value = remainingMinutes
            }

            override fun onFinish() {
                subscribeToLiveData()
            }

        }.start()
    }

    fun updateTimer(totalMillis: Long, subscribeToLiveData: () -> Unit) {

        timer?.cancel()

        timer = object : CountDownTimer(totalMillis, 10000) {

            override fun onTick(millisUntilFinished: Long) {

                val minutes = (millisUntilFinished / 1000) / 60
                val hours = minutes / 60
                val remainingMinutes = minutes % 60

                Log.e("TAG", "startCountDownTimer8888: " + minutes + " re " + remainingMinutes)

                this@CountDownController.remainingHoursMutableLiveData.value = hours
                this@CountDownController.remainingMinutesMutableLiveData.value = remainingMinutes
            }

            override fun onFinish() {
                subscribeToLiveData()
            }
        }.start()
    }

    fun getFlag(): Boolean {
        return flag
    }
}