package com.abing.letak.mainmenufragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.abing.letak.databinding.FragmentProfileBinding
import com.abing.letak.model.Vehicle
import com.abing.letak.registervehicle.RegisterVehicleActivity
import com.abing.letak.sampleadapters.VehicleAdapter
import com.abing.letak.showprofileactivity.ShowProfileActivity
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val viewModel: UserIdViewModel by viewModels()
    private lateinit var userRef: DocumentReference
    //mutable list must be initialized first
    private var vehicleList: MutableList<Vehicle> = mutableListOf()

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

        //get vehicle data from firestore
        getVehicle()

        //button listener
        binding.registerVehicleButton.setOnClickListener { registerVehicle() }
        binding.showProfileButton.setOnClickListener { showProfile() }
    }

    private fun getVehicle() {
        userRef.collection("vehicles").get()
            .addOnSuccessListener { result ->
                for (document in result){
                    Log.d("user vehicles", "${document.id} => ${document.data}")
                    vehicleList.add(document.toObject())
                }
                Log.d("vehicle list", "${vehicleList.size}")
                initVehicleRecyclerView()

            }
            .addOnFailureListener {
                Log.d("user vehicles", "failed with ", it)
            }
    }

    private fun initVehicleRecyclerView() {
        binding.userVehicleRv.adapter = VehicleAdapter(vehicleList, requireContext())
        binding.userVehicleRv.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initLetakCard() {
        //from the profile setup
//        val firstName = activity?.intent?.extras?.getString("firstName")
//        val lastName = activity?.intent?.extras?.getString("lastName")
        val profilePicUri = activity?.intent?.extras?.getParcelable<Uri>("profileImageUri")

        userRef.get().addOnSuccessListener {
            val user = it.toObject<com.abing.letak.model.User>()
            val userName = user?.userFirstName + " " +user?.userLastName
            binding.userName.text = userName
        }
        binding.userProfilePicture.setImageURI(profilePicUri)
    }

    private fun showProfile() {
        activity?.finish()
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