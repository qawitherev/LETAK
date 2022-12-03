package com.abing.letak.showprofileactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.abing.letak.MainMenuActivity
import com.abing.letak.R
import com.abing.letak.databinding.ActivityShowProfileBinding
import com.abing.letak.model.User
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.utils.setFullScreen
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ShowProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val viewModel: UserIdViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lightStatusBar(window, true, true)
        setSupportActionBar(binding.showProfileToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //init
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        initEditable()
        fillUpUserDetails()

        //listener
        binding.saveProfileBtn.setOnClickListener { saveProfile() }
    }

    private fun saveProfile() {
        if (inputInvalid()){
            Toast.makeText(this, R.string.fill_up_user_detail, Toast.LENGTH_SHORT).show()
            return
        }
        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()
        val email = binding.email.text.toString()
        val phoneNum = binding.phoneNumber.text.toString()
        val updatedUser = User(
            viewModel.userId,
            firstName,
            lastName,
            email,
            phoneNum
        )
        val userRef = db.collection("users").document(viewModel.userId)
        userRef.set(updatedUser).addOnSuccessListener {
            Log.d("updated user", "success")
            finish()
        }
            .addOnFailureListener {
                Log.d("updated user", "failed with", it)
            }
        finish()
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }

    private fun inputInvalid(): Boolean {
        return binding.firstName.text.isEmpty() || binding.lastName.text.isEmpty() ||
                binding.email.text.isEmpty() || binding.phoneNumber.text.isEmpty()
    }

    private fun initEditable() {
        binding.firstName.hint = resources.getString(R.string.first_name)
        binding.lastName.hint = resources.getString(R.string.last_name)
    }

    private fun fillUpUserDetails() {
        val docRef = db.collection("users").document(viewModel.userId)
        docRef.get().addOnSuccessListener {
            val user  = it.toObject<User>()
            Log.d("convert to object", "success")
            binding.firstName.setText(user?.userFirstName)
            binding.lastName.setText(user?.userLastName)
            if (user?.userEmail != null){
                binding.email.setText(user?.userEmail)
            }else {
                binding.email.hint = resources.getString(R.string.email_not_set_yet)
            }
            if (user?.userPhoneNum != null){
                binding.phoneNumber.setText(user?.userPhoneNum)
            }else {
                binding.phoneNumber.hint = resources.getString(R.string.phone_not_set_yet)
            }

        }
    }


}