package com.example.my_kwuotes.presentation.utils

import android.graphics.Bitmap
import android.net.Uri

data class UserSettings (
    val isDarkTheme: Boolean,
    val currentLanguage: String,
    val notificationsEnabled: Boolean,
    val updatesEnabled: Boolean
)