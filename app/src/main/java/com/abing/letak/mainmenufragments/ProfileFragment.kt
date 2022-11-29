package com.abing.letak.mainmenufragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.accessibility.AccessibilityViewCommand
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.abing.letak.data.vehicles
import com.abing.letak.databinding.FragmentProfileBinding
import com.abing.letak.registervehicle.RegisterVehicleActivity
import com.abing.letak.sampleadapters.VehicleAdapter
import com.abing.letak.showprofileactivity.ShowProfileActivity
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val dataset = vehicles
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val viewModel: UserIdViewModel by viewModels()
    private lateinit var userRef: DocumentReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init firebase stuff
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        userRef = db.collection("users").document(viewModel.userId)

        //setting the LETAK card
        initLetakCard()

        //binding listener
        binding.userVehicleRv.adapter = VehicleAdapter(dataset)
        binding.userVehicleRv.layoutManager = LinearLayoutManager(requireContext())
        binding.registerVehicleButton.setOnClickListener { registerVehicle() }
        binding.showProfileButton.setOnClickListener { showProfile() }
    }

    private fun initLetakCard() {
        val firstName = activity?.intent?.extras?.getString("firstName")
        val lastName = activity?.intent?.extras?.getString("lastName")
        val profilePicUri = activity?.intent?.extras?.getParcelable<Uri>("profileImageUri")
        binding.userName.text = firstName + " " + lastName
        // TODO: get the first and last name from database 
        binding.userProfilePicture.setImageURI(profilePicUri)
    }

    private fun showProfile() {
        val intent = Intent(requireContext(), ShowProfileActivity::class.java)
        startActivity(intent)
    }

    private fun registerVehicle() {
        val intent = Intent(requireContext(), RegisterVehicleActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}