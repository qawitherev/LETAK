package com.abing.letak.ordernowactivity.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimeLeftViewModel: ViewModel() {

    private val _elapsedTime = MutableLiveData<String>()
    val elapsedTime: LiveData<String> = _elapsedTime

    private var timer: CountDownTimer? = null


    fun startTimer() {
        timer = object: CountDownTimer(300000, 1000){
            override fun onTick(milisUntilFinish: Long) {
                var diff = milisUntilFinish
                val secondsInMilis: Long = 1000
                val minutesInMilis = secondsInMilis * 60

                val minuteLeft = diff / minutesInMilis
                diff %= minutesInMilis
                val secondsLeft = diff / secondsInMilis
                _elapsedTime.value = "$minuteLeft:$secondsLeft"
            }

            override fun onFinish() {
                timer?.cancel()
            }
        }
        timer?.start()
    }

    fun stopTimer(){
        timer?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}