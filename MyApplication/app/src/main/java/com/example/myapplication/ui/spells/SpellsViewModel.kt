package com.example.myapplication.ui.spells

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpellsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is spells Fragment"
    }
    val text: LiveData<String> = _text
}