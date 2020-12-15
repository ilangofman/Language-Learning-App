package com.example.lyngua.controllers

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.lyngua.models.photos.*
import com.example.lyngua.models.Languages
import com.example.lyngua.models.categories.CategoryAPI
import com.example.lyngua.models.words.Word
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.concurrent.thread
import kotlin.random.Random

class GalleryController(private var context: Context,
                        private var activity: Activity,
                        private var appName: String) {

    var liveAlbumData = MutableLiveData<List<Album>>()
    private val repository: PhotoRepository
    private val categoryAPI = CategoryAPI

    init {
        liveAlbumData.value = getAlbums()
        val photoDao = PhotoDatabase.getDatabase(context).photoDao()
        repository = PhotoRepository(photoDao)
    }

    /*
     * Purpose: Get the directory for where photos will be stored
     * Input:   None
     * Output:  File object containing directory path
     */
    fun getOutputDirectory(): File {
        val photoDir = activity.getExternalFilesDirs(Environment.DIRECTORY_PICTURES)?.firstOrNull()?.let {
            File(it, appName).apply { mkdirs() } }
        return if (photoDir != null && photoDir.exists())
            photoDir else context.filesDir
    }

    /*
     * Purpose: Save a photo on the device and in the repository
     * Input:   imageBitmap - Bitmap object representing image
     *          albumName   - String representing album name
     *          fileName    - String representing file name
     *          word        - String representing image object
     * Output:  Boolean for if save was successful or not
     */
    fun savePhoto(imageBitmap: Bitmap?, albumName: String, fileName: String, word: String, options: List<String>, correctOption: Int): Boolean {
        val rootPath = getOutputDirectory()
        return if (File(rootPath, albumName).exists()) {
            val photoFile = File(File(rootPath, albumName), fileName)

            val outputStream = FileOutputStream(photoFile)
            imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            thread {
                repository.addPhoto(Photo(0, photoFile.absolutePath, word, options, correctOption))
            }

            true
        } else false
    }

    /*
     * Purpose: Get the photos in the given album or all photos if no album is specified
     * Input:   albumName   - String representing album name
     * Output:  A list of Photo objects
     */
    fun getPhotos(albumName: String = ""): MutableList<Photo> {
        val photoDir = File(getOutputDirectory().absolutePath, albumName)
        val photoUriStringList = arrayListOf<String>()
        val photos = arrayListOf<Photo>()

        photoDir.walkBottomUp()
            .filter { it.extension == "jpg" }
            .forEach { photoUriStringList.add(it.absolutePath) }


        thread {
            photos.addAll(repository.getPhotos(photoUriStringList))
        }.join()

        return photos
    }

    /*
     * Purpose: Get the most recent photos
     * Input:   numPhotos   - Integer for number of photos to get
     * Output:  A list of Photo objects
     */
    fun getRecentPhotos(numPhotos: Int = 5): MutableList<Photo> {
        val photoDir = getOutputDirectory()
        val photoUriStringList = arrayListOf<String>()
        val photos = arrayListOf<Photo>()

        photoDir.walkBottomUp()
            .filter { it.extension == "jpg" }
            .take(numPhotos)
            .forEach { photoUriStringList.add(it.absolutePath) }

        thread {
            photos.addAll(repository.getPhotos(photoUriStringList))
        }.join()

        return photos
    }

    /*
     * Purpose: Delete all photos provided
     * Input:   photoList   - List of photos to delete
     * Output:  None
     */
    fun deletePhotos(photoList: List<Photo>) {
        thread {
            repository.deletePhotos(photoList)
        }.join()
        photoList.forEach {
            File(it.uriString).delete()
        }
        Log.d(TAG, "Photos deleted")
        liveAlbumData.value = getAlbums()
    }

    /*
     * Purpose: Add a folder on the device representing a new album
     * Input:   albumName   - String for the album name
     * Output:  Boolean for if folder creation was successful or not
     */
    fun createAlbum(albumName: String): Boolean {
        val rootPath = getOutputDirectory()
        return if (File(rootPath, albumName).exists()) {
            Log.d(TAG, "Album already exists")
            false
        } else {
            val albumDir = rootPath.let { File(it, albumName.toUpperCase(Locale.getDefault())).apply { mkdirs() } }
            if (albumDir.exists()) {
                liveAlbumData.value = getAlbums()
                true
            } else false
        }
    }

    /*
     * Purpose: Get all the album folders
     * Input:   None
     * Output:  List of Album objects
     */
    fun getAlbums(): MutableList<Album> {
        val appDir = getOutputDirectory()
        val albumList = arrayListOf<Album>()

        appDir.walkBottomUp()
            .filter { it.extension == "" && it != appDir }
            .forEach { album ->
                val albumName = album.name.toUpperCase(Locale.getDefault())
                val coverPhoto :String?  = album.walkBottomUp()
                    .firstOrNull{ it.extension == "jpg" }?.absolutePath

                albumList.add(Album(albumName, coverPhoto))
            }

        return albumList
    }

    /*
     * Purpose: Get the most recent album
     * Input:   None
     * Output:  An album object
     */
    fun getRecentAlbum(): Album? {
        val appDir = getOutputDirectory()

        appDir.walkBottomUp()
            .firstOrNull { it.extension == "" && it != appDir }
            .let { album ->
                if (album == null) return null

                val albumName = album.name.toUpperCase(Locale.getDefault())
                val coverPhoto :String?  = album.walkBottomUp()
                    .firstOrNull{ it.extension == "jpg" }?.absolutePath

                return Album(albumName, coverPhoto)
            }
    }

    /*
     * Purpose: Set the name for an album folder
     * Input:   oldAlbumName    - String representing old album name
     *          newAlbumName    - String representing new album name
     * Output:  Boolean for if name change was successful or not
     */
    fun setAlbumName(oldAlbumName: String, newAlbumName: String): Boolean {
        val rootPath = getOutputDirectory()
        val currentAlbum: File? = File(rootPath, oldAlbumName.toUpperCase(Locale.getDefault()))
        val newAlbum = File(rootPath, newAlbumName.toUpperCase(Locale.getDefault()))

        return if (currentAlbum?.exists()!!)
            if (newAlbum.exists()) {
                Log.d(TAG, "Album already exists")
                false
            } else {
                val photoList = getPhotos(oldAlbumName)
                photoList.forEach {
                    it.uriString = it.uriString.replace(oldAlbumName, newAlbumName)
                }
                thread {
                    repository.updatePhotos(photoList)
                }.join()

                currentAlbum.renameTo(newAlbum)
                liveAlbumData.value = getAlbums()
                true
            }
        else {
            Log.d(TAG, "Album not found")
            false
        }
    }

    /*
     * Purpose: Delete an album folder and all its contents
     * Input:   albumName   - String representing album to delete
     * Output:  Boolean for if deletion was successful or not
     */
    fun deleteAlbum(albumName: String): Boolean {
        val rootPath = getOutputDirectory()
        val albumDir: File? = File(rootPath, albumName)

        return if (albumDir?.exists()!!) {
            if (albumDir.walkBottomUp().all {
                    thread {
                        repository.deletePhoto(it.absolutePath)
                    }.join()
                    it.delete()
                }) {
                Log.d(TAG, "Album deleted")
                liveAlbumData.value = getAlbums()
                true
            }
            else {
                Log.d(TAG, "Error deleting album")
                liveAlbumData.value = getAlbums()
                false
            }
        }else {
            Log.d(TAG, "Album not found")
            liveAlbumData.value = getAlbums()
            false
        }
    }

    fun makeQuestionFromWord(word: String, langCode: String): List<String>? {
        var wordsList: List<Word>?
        var chosenOptionsList: List<String>? = null
        thread {
            wordsList = categoryAPI.getWordsForQuestion(word)
            if (wordsList == null) {
                print("Unable to get related words")
            } else {
                val correct = Languages.translate(word, langCode).toString()
                do {
                    var options1Word = wordsList!![Random.nextInt(10, 36)].word
                    var options2Word = wordsList!![Random.nextInt(37, 67)].word
                    var options3Word = wordsList!![Random.nextInt(67, 100)].word

                    var translatedWrong1  = Languages.translate(options1Word, langCode).toString()
                    while(translatedWrong1 == options1Word){
                        options1Word = wordsList!![Random.nextInt(10, 36)].word
                        translatedWrong1  = Languages.translate(options1Word, langCode).toString()

                    }
                    var translatedWrong2  = Languages.translate(options2Word, langCode).toString()
                    while(translatedWrong2 == options2Word){
                        options2Word = wordsList!![Random.nextInt(37, 66)].word
                        translatedWrong2  = Languages.translate(options2Word, langCode).toString()

                    }
                    var translatedWrong3  = Languages.translate(options3Word, langCode).toString()
                    while(translatedWrong3 == options3Word){
                        options3Word = wordsList!![Random.nextInt(67, 100)].word
                        translatedWrong3  = Languages.translate(options3Word, langCode).toString()

                    }


                    chosenOptionsList = listOf(correct, translatedWrong1, translatedWrong2, translatedWrong3)
                }while (options1Word == options2Word
                    || options1Word == options3Word
                    || options2Word == options3Word)


            }
        }.join()



        return chosenOptionsList
    }

    companion object {
        private const val TAG = "Gallery Controller"
    }
}
