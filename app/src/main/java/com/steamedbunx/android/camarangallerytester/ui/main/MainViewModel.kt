package com.steamedbunx.android.camarangallerytester.ui.main

import android.app.Application
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.steamedbunx.android.camarangallerytester.data.ImageStored

class MainViewModel(val app: Application) : AndroidViewModel(app) {
    var currentCount = 0
    private val _images = MutableLiveData<ArrayList<ImageStored>>()
    val images: LiveData<ArrayList<ImageStored>>
        get() = _images
    val changed = MutableLiveData<Boolean>()

    init {
        changed.value = false
    }

    fun addImage(image: Bitmap) {
        _images.value = _images.value ?: ArrayList()
        _images.value!!.add(ImageStored(currentCount, image))
        currentCount++
    }

    fun removeImage(item: ImageStored) {
        _images.value = _images.value ?: ArrayList()
        if (_images.value!!.size > 0){
            _images.value!!.remove(item)
        }
    }
}
