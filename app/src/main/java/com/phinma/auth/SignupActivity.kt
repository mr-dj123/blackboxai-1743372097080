package com.phinma.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.phinma.R
import com.phinma.databinding.ActivitySignupBinding
import com.phinma.models.UserProfile
import com.phinma.utils.PreferenceHelper
import com.phinma.utils.AnalyticsHelper
import com.phinma.utils.showToast

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        AnalyticsHelper.logSignupStep("signup_started")
        db = FirebaseFirestore.getInstance()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnSignup.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            val fullName = binding.etFullName.text.toString().trim()

            if (validateInputs(email, password, confirmPassword, fullName)) {
                AnalyticsHelper.logSignupStep("signup_form_completed")
                registerUser(email, password, fullName)
            }
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateInputs(
        email: String,
        password: String,
        confirmPassword: String,
        fullName: String
    ): Boolean {
        if (fullName.isEmpty()) {
            binding.etFullName.error = getString(R.string.full_name_required)
            return false
        }

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

        if (password != confirmPassword) {
            binding.etConfirmPassword.error = getString(R.string.password_mismatch)
            return false
        }

        return true
    }

    private fun registerUser(email: String, password: String, fullName: String) {
        val progressDialog = ProgressDialog(this).apply {
            setMessage(getString(R.string.creating_account))
            setCancelable(false)
            show()
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    saveUserProfile(fullName, email, progressDialog)
                } else {
                    progressDialog.dismiss()
                    val error = when (task.exception) {
                        is FirebaseAuthUserCollisionException -> getString(R.string.email_in_use)
                        is FirebaseAuthWeakPasswordException -> getString(R.string.weak_password)
                        else -> getString(R.string.registration_failed)
                    }
                    showToast(error)
                }
            }
    }

    private fun saveUserProfile(fullName: String, email: String, progressDialog: ProgressDialog) {
        progressDialog.setMessage(getString(R.string.saving_profile))
        
        val user = auth.currentUser ?: run {
            progressDialog.dismiss()
            showToast(getString(R.string.user_not_found))
            return
        }

        // Send email verification
        user.sendEmailVerification()
            .addOnCompleteListener { verificationTask ->
                if (verificationTask.isSuccessful) {
                    val userProfile = UserProfile(
                        name = fullName,
                        email = email,
                        school = "",
                        studentId = "",
                        department = "",
                        isEmailVerified = false
                    )

                    db.collection("users").document(user.uid)
                        .set(userProfile)
                        .addOnSuccessListener {
                            AnalyticsHelper.logSignupStep("profile_created", user.uid)
                            progressDialog.dismiss()
                            showToast(getString(R.string.verification_email_sent))
                            PreferenceHelper(this).apply {
                                saveLoginState(false) // Don't allow login until verified
                                saveUserEmail(email)
                            }
                            startActivity(Intent(this, VerifyEmailActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            progressDialog.dismiss()
                            showToast(getString(R.string.profile_save_failed))
                        }
                } else {
                    progressDialog.dismiss()
                    showToast(getString(R.string.failed_to_send_verification))
                }
            }
    }
}