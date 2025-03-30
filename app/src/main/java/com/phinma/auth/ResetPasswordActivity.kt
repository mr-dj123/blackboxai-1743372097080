package com.phinma.auth

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.phinma.R
import com.phinma.databinding.ActivityResetPasswordBinding
import com.phinma.utils.showToast

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnResetPassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (validateInput(email)) {
                sendPasswordResetEmail(email)
            }
        }

        binding.tvBackToLogin.setOnClickListener {
            finish()
        }
    }

    private fun validateInput(email: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = getString(R.string.email_required)
            return false
        }

        if (!email.isValidEmail()) {
            binding.etEmail.error = getString(R.string.invalid_email)
            return false
        }

        return true
    }

    private fun sendPasswordResetEmail(email: String) {
        val progressDialog = ProgressDialog(this).apply {
            setMessage(getString(R.string.sending_reset_email))
            setCancelable(false)
            show()
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    showToast(getString(R.string.reset_email_sent))
                    finish()
                } else {
                    val error = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> getString(R.string.email_not_registered)
                        else -> getString(R.string.reset_email_failed)
                    }
                    showToast(error)
                }
            }
    }
}