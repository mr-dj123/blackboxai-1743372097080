package com.phinma.models

data class Event(
    val title: String,
    val date: String,
    val location: String,
    val description: String = "",
    val imageUrl: String = ""
)