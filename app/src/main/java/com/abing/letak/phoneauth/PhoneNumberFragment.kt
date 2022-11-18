package com.abing.letak.phoneauth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.abing.letak.MainMenuActivity
import com.abing.letak.R
import com.abing.letak.databinding.FragmentPhoneNumberBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneNumberFragment : Fragment() {
    private var _binding: FragmentPhoneNumberBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //init values
        auth = FirebaseAuth.getInstance()

        _binding = FragmentPhoneNumberBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.verifyBtn.setOnClickListener { verifyPhone() }
    }

    private fun verifyPhone() {
        val phoneNumber = "+6" + binding.phoneNumber.text.trim().toString()
        if (phoneNumber.isNotBlank()){
            if (phoneNumber.length == 12 || phoneNumber.length == 13){
                phoneAuthOptions(phoneNumber)
            }else {
                Toast.makeText(requireContext(), R.string.phone_number_incorrect, Toast.LENGTH_SHORT).show()
            }
        }else {
            Toast.makeText(requireContext(), R.string.fill_out_details, Toast.LENGTH_SHORT).show()
        }
    }

    private fun phoneAuthOptions(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            //this when for phone number can be instantly verified without sending verification code, OTP
            //or when device can automatically detect incoming sms and perform verification without user interference
            //when this happens, sign in to firebase with the credential
            signInFirebaseWithPhoneAuth(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            //this callback is invoked when the verification request failed e.g. phone number for isn't valid
            if (e is FirebaseAuthInvalidCredentialsException) {
                //invalid credential
                Toast.makeText(requireContext(), "${e.toString()}", Toast.LENGTH_SHORT).show()
            }
            if (e is FirebaseTooManyRequestsException){
                //sms quota exceeded
                Toast.makeText(requireContext(), "${e.toString()}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            //this callback is invoked when otp has been sent to user and ask user to enter the code
            //go to phoneOtpFragment
            val action = PhoneNumberFragmentDirections.actionPhoneNumberFragmentToPhoneOtpFragment(
                verificationId = verificationId
            )
            findNavController().navigate(action)
        }

    }

    private fun signInFirebaseWithPhoneAuth(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                //goto main menu activity
                Toast.makeText(requireContext(), R.string.registration_success, Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), MainMenuActivity::class.java)
                startActivity(intent)
            }else {
                Toast.makeText(requireContext(), "${it.exception}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}