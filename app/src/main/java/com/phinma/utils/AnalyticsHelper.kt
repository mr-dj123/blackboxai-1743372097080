package com.phinma.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object AnalyticsHelper {
    private lateinit var analytics: FirebaseAnalytics

    fun initialize(context: Context) {
        analytics = Firebase.analytics
    }

    fun logVerificationEvent(eventType: String, userId: String? = null, email: String? = null) {
        val bundle = Bundle().apply {
            putString("event_type", eventType)
            putLong("timestamp", System.currentTimeMillis())
            userId?.let { putString("user_id", it) }
            email?.let { putString("user_email", it) }
        }
        analytics.logEvent("email_verification", bundle)
    }

    fun logVerificationResend() {
        logVerificationEvent("resend")
    }

    fun logVerificationSuccess() {
        logVerificationEvent("success")
    }

    fun logVerificationSkip() {
        logVerificationEvent("skip")
    }

    fun logSignupStep(stepName: String, userId: String? = null) {
        val bundle = Bundle().apply {
            putString("step", stepName)
            putLong("timestamp", System.currentTimeMillis())
            userId?.let { putString("user_id", it) }
        }
        analytics.logEvent("signup_funnel", bundle)
    }
}