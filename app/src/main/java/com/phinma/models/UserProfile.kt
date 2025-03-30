package com.phinma.models

data class UserProfile(
    val name: String,
    val email: String,
    val school: String,
    val studentId: String,
    val department: String,
    val isEmailVerified: Boolean = false,
    val verificationSentAt: Long = System.currentTimeMillis(),
    val verificationCompletedAt: Long? = null
)