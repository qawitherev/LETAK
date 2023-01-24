package com.abing.letak.advancebookingactivity.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date

class AdvBookingViewModel: ViewModel() {
    private val _advBookingId = MutableLiveData<String>()
    val bookingId: LiveData<String> = _advBookingId

    private val _lotId = MutableLiveData<String>()
    val lotId: LiveData<String> = _lotId

    private val _spaceId = MutableLiveData<String>()
    val spaceId: LiveData<String> = _spaceId

    private val _spaceType = MutableLiveData<String>()
    val spaceType: LiveData<String> = _spaceType

    private val _parkingPeriodMinute = MutableLiveData<String>()
    val parkingPeriodMinute: LiveData<String> = _parkingPeriodMinute

    private val _parkingStart = MutableLiveData<String>()
    val parkingStart: LiveData<String> = _parkingStart

    private val _parkingEnd = MutableLiveData<String>()
    val parkingEnd: LiveData<String> = _parkingEnd

    private val _vecPlate = MutableLiveData<String>()
    val vecPlate: LiveData<String> = _vecPlate

    private val _eWalletType = MutableLiveData<String>()
    val eWalletType: LiveData<String> = _eWalletType

    private val _parkingFee = MutableLiveData<Double>()
    val parkingFee: LiveData<Double> = _parkingFee

    fun setAdvBookingId(advBookingId: String){
        _advBookingId.value = advBookingId
    }

    fun setLotId(lotId: String){
        _lotId.value = lotId
    }

    fun setSpaceId(spaceId: String){
        _spaceId.value = spaceId
    }

    fun setSpaceType(spaceType: String){
        _spaceType.value = spaceType
    }

    fun setParkingPeriodMinute(parkingPeriodMinute: String){
        _parkingPeriodMinute.value = parkingPeriodMinute
    }

    fun setParkingStart(parkingStart: String){
        _parkingStart.value = parkingStart
    }

    fun setParkingEnd(parkingEnd: String){
        _parkingEnd.value = parkingEnd
    }

    fun setVecPlate(vecPlate: String){
        _vecPlate.value = vecPlate
    }

    fun setEWalletType(eWalletType: String){
        _eWalletType.value = eWalletType
    }

    fun setParkingFee(parkingFee: Double){
        _parkingFee.value = parkingFee
    }
}