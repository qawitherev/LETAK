package com.abing.letak.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ParkingFeeViewModel: ViewModel() {

    val parkingFee: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }


    fun calculateFee(spaceType: String, hour: String, minute: String){
        parkingFee.value = 0.0
        if (spaceType == "Green"){
            var totalFee = hour.toDouble() * 0.5
            if (minute.toDouble() > 0.0){
                totalFee += 0.5
                parkingFee.value = totalFee
            }else {
                parkingFee.value = totalFee
            }

        }
        // TODO: do for red and yellow


    }
}