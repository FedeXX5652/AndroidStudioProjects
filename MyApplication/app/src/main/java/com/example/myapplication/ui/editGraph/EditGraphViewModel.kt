package com.example.myapplication.ui.editGraph

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditGraphViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is edit graph Fragment"
    }
    val text: LiveData<String> = _text
}