package com.abing.letak.registervehicle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.abing.letak.MainMenuActivity
import com.abing.letak.R
import com.abing.letak.databinding.ActivityRegisterVehicleBinding
import com.abing.letak.model.Vehicle
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.api.LogDescriptor
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

class RegisterVehicleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterVehicleBinding
    private lateinit var db: FirebaseFirestore
    private val viewModel: UserIdViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lightStatusBar(window, false, true)

        setSupportActionBar(findViewById(R.id.register_vehicle_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.register_vehicle)

        //init
        db = FirebaseFirestore.getInstance()

        //listener
        binding.registerVehicleBtn.setOnClickListener { registerVehicle() }
    }

    private fun registerVehicle() {
        val vecPlate = binding.vehiclePlate.text.toString()
        val vecCol = binding.vehicleColor.text.toString()
        val vecName = binding.vehicleName.text.toString()

        val vehicle = Vehicle(
            vecPlate,
            vecCol,
            vecName
        )
        val vecRef = db.collection("users").document(viewModel.userId).collection("vehicles")
        vecRef.add(vehicle).addOnSuccessListener {
            Log.d("add user vehicle", "success")
        }
            .addOnFailureListener {
                Log.d("add user vehicle", "failed because", it)
            }
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }

}