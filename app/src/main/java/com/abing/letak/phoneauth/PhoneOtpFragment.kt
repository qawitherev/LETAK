package com.abing.letak.phoneauth

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abing.letak.MainMenuActivity
import com.abing.letak.R
import com.abing.letak.databinding.FragmentPhoneNumberBinding
import com.abing.letak.databinding.FragmentPhoneOtpBinding
import com.abing.letak.profilesetupactivity.ProfileSetupActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class PhoneOtpFragment : Fragment() {
    private var _binding: FragmentPhoneOtpBinding? = null
    private val binding get() = _binding!!
    private val args: PhoneOtpFragmentArgs by navArgs()
    private lateinit var verificationId: String
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneOtpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init
        auth = FirebaseAuth.getInstance()

        //receiving verification id from phoneNumberFragment
        verificationId = args.verificationId

        binding.registerWithPhoneBtn.setOnClickListener { registerWithOtp() }
    }

    private fun registerWithOtp() {
        val otpNumber = binding.otpNumber.text.trim().toString()
        val credential = PhoneAuthProvider.getCredential(verificationId, otpNumber)
        signInFirebaseWithCredential(credential)
    }

    private fun signInFirebaseWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(requireContext(), R.string.registration_success, Toast.LENGTH_SHORT).show()
                val userId = auth.currentUser?.uid.toString()
                val intent = Intent(requireContext(), ProfileSetupActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
            }else {
                Toast.makeText(requireContext(), "Error: ${it.exception}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}