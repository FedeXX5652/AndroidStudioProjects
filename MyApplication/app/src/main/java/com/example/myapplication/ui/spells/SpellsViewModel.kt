package com.example.myapplication.ui.spells

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpellsViewModel : ViewModel() {

    var level: Int = 0
    var form: String = ""
    var school: String = ""
    var effect: String = ""
}