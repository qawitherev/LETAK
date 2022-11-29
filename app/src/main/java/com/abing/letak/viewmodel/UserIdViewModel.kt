package com.abing.letak.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class UserIdViewModel: ViewModel() {

    private var auth = FirebaseAuth.getInstance()

    private var _userId = ""
    val userId: String
        get() = _userId

    init {
        getUid()
    }
    fun getUid(){
        val currentUser = auth.currentUser
        if (currentUser != null){
            _userId = currentUser.uid
        }
    }
}