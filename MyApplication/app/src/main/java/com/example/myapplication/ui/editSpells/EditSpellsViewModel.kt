package com.example.myapplication.ui.editSpells

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditSpellsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is edit spells Fragment"
    }
    val text: LiveData<String> = _text
}