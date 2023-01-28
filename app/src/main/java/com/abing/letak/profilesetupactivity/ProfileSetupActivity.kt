package com.abing.letak.profilesetupactivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.abing.letak.MainMenuActivity
import com.abing.letak.R
import com.abing.letak.databinding.ActivityProfileSetupBinding
import com.abing.letak.model.User
import com.abing.letak.model.Vehicle
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Document

class ProfileSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileSetupBinding
    private var profilePicUri: Uri? = null
    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String
    private val viewModel: UserIdViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init
        db = FirebaseFirestore.getInstance()
        userId = viewModel.userId

        //utils
        lightStatusBar(window, true, true)

        //binding listeners
//        binding.addPhotoBtn.setOnClickListener { importPhoto() }
        binding.finishSetupBtn.setOnClickListener { finishSetup() }
    }

    private fun finishSetup() {
        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()
        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, R.string.fill_out_details, Toast.LENGTH_SHORT).show()
            return
        }
        addUserInFireStore(firstName, lastName)
        val intent = Intent(this, MainMenuActivity::class.java)
        intent.putExtra("profileImageUri", profilePicUri)
        intent.putExtra("firstName", firstName)
        intent.putExtra("lastName", lastName)
        startActivity(intent)
        finish()
    }

    private fun addUserInFireStore(firstName: String, lastName: String) {
        val user = User(
            userId,
            firstName,
            lastName,
            null,
            null,
            false,
            false,
            false,
            null,
            null,
        )
        //user doc ref
        val userRef = db.collection("users").document(userId)
        //add user
        userRef.set(user).addOnSuccessListener {
            Toast.makeText(this, "db success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "db fail", Toast.LENGTH_SHORT).show()
        }
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            profilePicUri = uri
            binding.userProfilePic.setImageURI(uri)
        }

    private fun importPhoto() {
        getContent.launch("image/*")
    }

}