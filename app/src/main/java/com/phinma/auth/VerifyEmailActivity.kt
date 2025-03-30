package com.phinma.auth

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.phinma.MainActivity
import com.phinma.R
import com.phinma.databinding.ActivityVerifyEmailBinding
import com.phinma.utils.showToast

class VerifyEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyEmailBinding
    private lateinit var auth: FirebaseAuth
    private val handler = Handler(Looper.getMainLooper())
    private val verificationCheckInterval = 5000L // 5 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        setupClickListeners()
        startVerificationCheck()
        handleDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleDeepLink(it) }
    }

    private fun setupClickListeners() {
        binding.btnResendVerification.setOnClickListener {
            resendVerificationEmail()
        }

        binding.btnContinueWithoutVerification.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun startVerificationCheck() {
        handler.postDelayed(verificationRunnable, verificationCheckInterval)
    }

    private val verificationRunnable = object : Runnable {
        override fun run() {
            auth.currentUser?.reload()?.addOnCompleteListener { task ->
                if (task.isSuccessful && auth.currentUser?.isEmailVerified == true) {
                    updateVerificationStatus()
                    showToast(getString(R.string.email_verified_success))
                    startActivity(Intent(this@VerifyEmailActivity, MainActivity::class.java))
                    finish()
                } else {
                    handler.postDelayed(this, verificationCheckInterval)
                }
            }
        }
    }

    private fun resendVerificationEmail() {
        binding.btnResendVerification.isEnabled = false
        var secondsRemaining = 30
        
        val countdownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.btnResendVerification.text = 
                    getString(R.string.resend_in_seconds, secondsRemaining--)
            }

            override fun onFinish() {
                binding.btnResendVerification.isEnabled = true
                binding.btnResendVerification.text = getString(R.string.resend_verification)
            }
        }.start()

        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showToast(getString(R.string.verification_email_resent))
            } else {
                countdownTimer.cancel()
                binding.btnResendVerification.isEnabled = true
                binding.btnResendVerification.text = getString(R.string.resend_verification)
                showToast(getString(R.string.failed_to_send_verification))
            }
        }
    }

    private fun handleDeepLink(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            auth.currentUser?.reload()?.addOnCompleteListener { task ->
                if (task.isSuccessful && auth.currentUser?.isEmailVerified == true) {
                    AnalyticsHelper.logSignupStep("verification_completed", auth.currentUser?.uid)
                    updateVerificationStatus()
                    showToast(getString(R.string.email_verified_success))
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun updateVerificationStatus() {
        val userId = auth.currentUser?.uid ?: return
        val updates = hashMapOf<String, Any>(
            "isEmailVerified" to true,
            "verificationCompletedAt" to System.currentTimeMillis()
        )
        
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .update(updates)
            .addOnFailureListener { e ->
                Log.e("VerifyEmailActivity", "Failed to update verification status", e)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(verificationRunnable)
    }
}