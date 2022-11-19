package com.abing.letak.profilesetupactivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.abing.letak.MainMenuActivity
import com.abing.letak.R
import com.abing.letak.databinding.ActivityProfileSetupBinding
import com.abing.letak.utils.lightStatusBar

class ProfileSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileSetupBinding
    private var profilePicUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //utils
        lightStatusBar(window, true, true)

        //binding listeners
        binding.addPhotoBtn.setOnClickListener { importPhoto() }
        binding.finishSetupBtn.setOnClickListener { finishSetup() }
    }

    private fun finishSetup() {
        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()
        if (firstName.isEmpty() || lastName.isEmpty()){
            Toast.makeText(this, R.string.fill_out_details, Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, MainMenuActivity::class.java)
        intent.putExtra("profileImageUri", profilePicUri)
        intent.putExtra("firstName", firstName)
        intent.putExtra("lastName", lastName)
        startActivity(intent)
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