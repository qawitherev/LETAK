package com.abing.letak.ordernowactivity.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimeLeftViewModel: ViewModel() {
    private val _timeLeft = MutableLiveData<Long>()
    val timeLeft: LiveData<Long> = _timeLeft

    private var timer: CountDownTimer? = null


    fun startTimer() {
        timer = object: CountDownTimer(300000, 1000){
            override fun onTick(milisUntilFinish: Long) {
                _timeLeft.value = milisUntilFinish / 1000
            }

            override fun onFinish() {
                _timeLeft.value = 0
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