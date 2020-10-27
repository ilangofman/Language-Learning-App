package com.example.lyngua.controllers

import android.app.Activity
import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

class GalleryController {

    fun savePhoto(imageBitmap: Bitmap?, photoFile: File) {
        val outputStream = FileOutputStream(photoFile)
        imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    }

    /*
     * Purpose: Get the directory for where photos will be stored
     * Input: None
     * Output: File object containing directory path
     */
    fun getOutputDirectory(activity: Activity?, appName: String): File? {
        return activity?.getExternalFilesDirs(Environment.DIRECTORY_PICTURES)?.firstOrNull()?.let {
            File(it, appName).apply { mkdirs() } }
    }
}