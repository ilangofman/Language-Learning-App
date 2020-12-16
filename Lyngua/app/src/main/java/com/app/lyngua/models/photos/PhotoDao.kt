package com.app.lyngua.models.photos

import androidx.room.*

/*
* This is the interface for the photo database operations
* Here all the different queries are listed.
* */
@Dao
@TypeConverters(PhotoTypeConverter::class)
interface PhotoDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPhoto(photo: Photo)

    @Query("SELECT * FROM photo_table ORDER BY uriString DESC")
    fun getPhotos(): List<Photo>

    @Query("SELECT * FROM photo_table WHERE uriString IN (:album) ORDER BY uriString DESC")
    fun getPhotos(album: MutableList<String>): List<Photo>

    @Update
    fun updatePhotos(photoList: List<Photo>): Int

    @Query("DELETE FROM photo_table WHERE uriString = :uriString")
    fun deletePhoto(uriString: String)

    @Delete
    fun deletePhotos(photoList: List<Photo>)
}