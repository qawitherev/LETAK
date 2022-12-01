package com.abing.letak.managevehicle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.abing.letak.MainMenuActivity
import com.abing.letak.R
import com.abing.letak.databinding.ActivityManageProfileBinding
import com.abing.letak.databinding.ActivityManageVehicleBinding
import com.abing.letak.model.Vehicle
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import org.w3c.dom.Document

class ManageVehicleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageVehicleBinding
    private lateinit var db: FirebaseFirestore
    private val viewModel: UserIdViewModel by viewModels()
    private lateinit var userRef: DocumentReference
    private lateinit var vecCurrent: List<Vehicle>
    private lateinit var vecId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lightStatusBar(window, false, true)
        setSupportActionBar(binding.manageVehicleToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //init
        db = FirebaseFirestore.getInstance()
        userRef = db.collection("users").document(viewModel.userId)

        getVehicleDetails()

        //listener
        binding.makeChangesBtn.setOnClickListener { makeChanges() }
//        binding.deleteVehicleBtn.setOnClickListener { deleteVehicle() }
    }

    private fun makeChanges() {
        if (inputInvalid()) {
            Toast.makeText(this, R.string.enter_vehicle_details, Toast.LENGTH_SHORT).show()
        }
        val vecRef = userRef.collection("vehicles").document(vecId)
        val theVec = Vehicle(
            binding.vecPlate.text.toString(),
            binding.vecCol.text.toString(),
            binding.vecName.text.toString()
        )
        vecRef.set(theVec)
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun inputInvalid(): Boolean {
        return binding.vecName.text.isEmpty() || binding.vecPlate.text.isEmpty() || binding.vecCol.text.isEmpty()
    }

    private fun getVehicleDetails() {
        val vecPlate = intent.extras?.get("vecPlate").toString()
        Log.d("vecPlate extra", vecPlate)
        userRef.collection("vehicles").whereEqualTo("vecPlate", vecPlate).get()
            .addOnSuccessListener {
                for (document in it){
                    Log.d("queried vehicle", "${document.data}")
                    vecCurrent = it.toObjects()
                    vecId = document.id
                    fillUpEditable()
                }
            }
    }

    private fun fillUpEditable() {
        Log.d("list vehicle object", "${vecCurrent[0]}")
        val theVec = vecCurrent[0]
        binding.vecName.setText(theVec.vecName)
        binding.vecPlate.setText(theVec.vecPlate)
        binding.vecCol.setText(theVec.vecColour)
    }
}