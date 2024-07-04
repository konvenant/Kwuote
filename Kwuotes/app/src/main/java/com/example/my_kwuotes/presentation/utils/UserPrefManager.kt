package com.example.my_kwuotes.presentation.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap

class UserPrefManager (context: Context) {
    val PREFS_NAME = "image_prefs"
    val IMAGE_KEY = "image_key"
    val NAME_KEY = "name_key"
    val FIRST_TIME = "first_time_key"
    val HAS_SAVED_CATEGORIES = "HAS_SAVED_CATEGORIES"
    val LANGUAGE_SETTINGS = "language_setting"
    val THEME_SETTINGS = "theme_setting"
    val NOTIFICATION_SETTINGS = "notification_setting"
    val UPDATE_SETTINGS = "update_setting"
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


    fun saveUserToPreferences(context: Context, bitmap: Bitmap,name:String)  {
        with(sharedPreferences.edit()){
        val encodedImage = encodeToBase64(bitmap)
        putString(IMAGE_KEY, encodedImage)
        putString(NAME_KEY, name)
            apply()
        }
    }

    fun saveUserSettingsPreferences(userSettings: UserSettings)  {
        with(sharedPreferences.edit()){
            putString(LANGUAGE_SETTINGS, userSettings.currentLanguage)
            putBoolean(THEME_SETTINGS, userSettings.isDarkTheme)
            putBoolean(NOTIFICATION_SETTINGS, userSettings.notificationsEnabled)
            putBoolean(UPDATE_SETTINGS,userSettings.updatesEnabled)
            apply()
        }
    }

    fun getImageFromPreferences(context: Context): UserPref {
        val name = sharedPreferences.getString(NAME_KEY,"") ?: " "
        val encodedImage = sharedPreferences.getString(IMAGE_KEY, null)
        val image = encodedImage?.let { decodeBase64(it) }
        return  UserPref(
            name,
            image
        )
    }

    fun getUserSettings(): UserSettings {
        val language = sharedPreferences.getString(LANGUAGE_SETTINGS,"English") ?: "English"
        val isDarkTheme = sharedPreferences.getBoolean(THEME_SETTINGS,false)
        val update = sharedPreferences.getBoolean(UPDATE_SETTINGS,false)
        val notification =  sharedPreferences.getBoolean(NOTIFICATION_SETTINGS,true)
        return  UserSettings(
            isDarkTheme,language,notification,update
        )
    }

    fun getFirstTime() : Boolean{
        return sharedPreferences.getBoolean(FIRST_TIME,true)
    }

    fun setFirstTime(firstTime:Boolean){
        with(sharedPreferences.edit()){
            putBoolean(FIRST_TIME,firstTime)
            apply()
        }
    }

    fun getHasSavedCategories() : Boolean{
        return sharedPreferences.getBoolean(HAS_SAVED_CATEGORIES,false)
    }

    fun setHasSavedCategories(firstTime:Boolean){
        with(sharedPreferences.edit()){
            putBoolean(HAS_SAVED_CATEGORIES,firstTime)
            apply()
        }
    }
}