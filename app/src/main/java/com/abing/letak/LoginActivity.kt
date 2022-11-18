package com.abing.letak

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.abing.letak.databinding.ActivityLoginBinding
import com.abing.letak.phoneauth.PhoneAuthActivity
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.utils.setFullScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        binding.loginButton.setOnClickListener { userLogin() }
        binding.cancelButton.setOnClickListener { cancelButton() }
        binding.continueWithGoogleButton.setOnClickListener { loginWithGoogle() }
        binding.continueWithPhone.setOnClickListener { continueWithPhone()}
    }

    private fun continueWithPhone() {
        val intent = Intent(this, PhoneAuthActivity::class.java)
        startActivity(intent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){
                val account: GoogleSignInAccount = task.result
                authFirebaseWithCredential(account)
            }
        }
    }

    private fun authFirebaseWithCredential(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
            }else {
                val stringRes = getString(R.string.login_failed) + " "
                Toast.makeText(this, stringRes.plus(it.exception), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private fun cancelButton() {
        finish()
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }

    private fun userLogin() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        if (isFieldBlank(email, password)){
            Toast.makeText(this, R.string.fill_out_details, Toast.LENGTH_SHORT).show()
        }else {
            authLogin(email, password)
        }
    }

    private fun authLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
            }else {
                val stringRes = getString(R.string.login) + " "
                Toast.makeText(this, stringRes.plus(it.exception), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isFieldBlank(email: String, password: String): Boolean {
        return email.isBlank() || password.isBlank()
    }
}