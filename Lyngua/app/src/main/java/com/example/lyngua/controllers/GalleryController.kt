package com.example.lyngua.controllers

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

class GalleryController {

    fun savePhoto(imageBitmap: Bitmap?, photoFile: File) {
        val outputStream = FileOutputStream(photoFile)
        imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    }
}