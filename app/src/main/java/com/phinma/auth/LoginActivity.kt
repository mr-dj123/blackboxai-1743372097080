package com.phinma.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.phinma.MainActivity
import com.phinma.R
import com.phinma.databinding.ActivityLoginBinding
import com.phinma.utils.PreferenceHelper
import com.phinma.utils.AnalyticsHelper
import com.phinma.utils.showToast

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateInputs(email, password)) {
                loginUser(email, password)
            }
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = getString(R.string.email_required)
            return false
        }

        if (!email.isValidEmail()) {
            binding.etEmail.error = getString(R.string.invalid_email)
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = getString(R.string.password_required)
            return false
        }

        if (!password.isValidPassword()) {
            binding.etPassword.error = getString(R.string.password_too_short)
            return false
        }

        return true
    }

    private fun loginUser(email: String, password: String) {
        val progressDialog = ProgressDialog(this).apply {
            setMessage(getString(R.string.logging_in))
            setCancelable(false)
            show()
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user?.isEmailVerified == true) {
                        val prefs = PreferenceHelper(this)
                        prefs.saveLoginState(true)
                        prefs.saveUserEmail(email)
                        AnalyticsHelper.setUserProperties(
                            userId = auth.currentUser?.uid,
                            email = email,
                            schoolId = prefs.getSchoolId().toString()
                        )
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        showVerificationDialog(user)
                    }
                } else {
                    val error = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> getString(R.string.user_not_found)
                        is FirebaseAuthInvalidCredentialsException -> getString(R.string.invalid_credentials)
                        else -> getString(R.string.login_failed)
                    }
                    showToast(error)
                }
            }
    }

    private fun showVerificationDialog(user: FirebaseUser?) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.email_not_verified_title))
            .setMessage(getString(R.string.email_not_verified_message))
            .setPositiveButton(getString(R.string.resend_verification)) { _, _ ->
                user?.sendEmailVerification()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast(getString(R.string.verification_email_resent))
                    } else {
                        showToast(getString(R.string.failed_to_send_verification))
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
}