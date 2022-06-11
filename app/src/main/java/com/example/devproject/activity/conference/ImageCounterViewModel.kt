package com.example.devproject.activity.conference

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageCounterViewModel : ViewModel() {
    private val _imageCounterLiveData = MutableLiveData<Int>()

    val imageCounterValue: LiveData<Int>
        get() = _imageCounterLiveData

    init {
        _imageCounterLiveData.value = 0
    }

    fun updateValue(input: Int){
        _imageCounterLiveData.value = input
    }

    fun deleteValue(input: Int){
        _imageCounterLiveData.value = input
    }
}