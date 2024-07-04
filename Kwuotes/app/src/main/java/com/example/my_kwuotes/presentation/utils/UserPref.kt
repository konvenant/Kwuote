package com.example.my_kwuotes.presentation.utils

import android.graphics.Bitmap
import android.net.Uri

data class UserPref (
    val name : String,
    val image: Bitmap? = null
)