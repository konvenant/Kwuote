package com.example.my_kwuotes.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_kwuotes.presentation.utils.UserPref

class SharedUserViewModel : ViewModel() {
    val userData: MutableLiveData<UserPref> = MutableLiveData()
}