package com.phinma.models

data class School(
    val id: Int,
    val name: String,
    val logoRes: Int,
    val isActive: Boolean = false
)