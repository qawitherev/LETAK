package com.abing.letak

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.abing.letak.databinding.ActivitySignUpBinding
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.utils.setFullScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreen(window)
        lightStatusBar(window, true, true)

        //init values
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //event listener
        binding.registerButton.setOnClickListener { registerEmailAddress() }
        binding.continueWithGoogle.setOnClickListener { registerWithGoogle() }
        binding.cancelButton.setOnClickListener { cancelButton() }
    }

    private fun cancelButton() {
        finish()
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }

    //once register with google button clicked
    private fun registerWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    //register the activity to know what google account is available for sign in
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            //check if the handled activity is the right activity
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleTask(task)
            }
        }

    private fun handleTask(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount = task.result
            authFirebaseWithCredential(account)
        }
    }

    private fun authFirebaseWithCredential(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, R.string.sign_in_google_success, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
            } else {
                val stringRes = getString(R.string.authentication_failed)
                Toast.makeText(this, "$stringRes ${it.exception}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //check if user already logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerEmailAddress() {
        if (detailsNotFilled()) {
            Toast.makeText(this, R.string.fill_out_details, Toast.LENGTH_SHORT).show()
        } else if (passwordNotConfirmed()) {
            Toast.makeText(this, R.string.password_not_confirmed, Toast.LENGTH_SHORT).show()
        } else {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            emailPasswordAuth(email, password)
        }
    }

    private fun emailPasswordAuth(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, R.string.registration_success, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainMenuActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, R.string.authentication_failed, Toast.LENGTH_SHORT).show()
                    Log.w("Sign up activity", "Sign up failed. ${it.exception}")
                }
            }
    }

    //method to check if password is the same
    private fun passwordNotConfirmed(): Boolean {
        return binding.password.text.toString() != binding.confirmPassword.text.toString()
    }

    //method to check if all details have been filled
    private fun detailsNotFilled(): Boolean {
        return binding.email.text.toString().isBlank() ||
                binding.password.text.toString().isBlank() ||
                binding.confirmPassword.text.toString().isBlank()
    }
}