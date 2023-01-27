package com.abing.letak.monthlypassactivity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PassBookingViewModel: ViewModel() {

    private val _passId = MutableLiveData<String>()
    val passId: LiveData<String> = _passId

    private val _lotId = MutableLiveData<String>()
    val lotId: LiveData<String> = _lotId

    private val _lotName = MutableLiveData<String>()
    val lotName: LiveData<String> = _lotName

    private val _startDate = MutableLiveData<String>()
    val startDate: LiveData<String> = _startDate

    private val _endDate = MutableLiveData<String>()
    val endDate: LiveData<String> = _endDate

    private val _eWalletType = MutableLiveData<String>()
    val eWalletType: LiveData<String> = _eWalletType

    fun setPassId(passId: String){
        _passId.value = passId
    }

    fun setLotId(lotId: String){
        _lotId.value = lotId
    }

    fun setLotName(lotName: String){
        _lotName.value = lotName
    }

    fun setStartDate(startDate: String){
        _startDate.value = startDate
    }

    fun setEndDate(endDate: String){
        _endDate.value = endDate
    }

    fun setEwalletType(eWalletType: String){
        _eWalletType.value = eWalletType
    }
}