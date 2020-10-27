package com.example.lyngua.models.Albums


class PhotoRepository(private val photoDao: PhotoDao) {

    /*
     * Purpose: add the Photo to the database
     * Input:   photo - Photo object to add
     * Output:  None
     */
    fun addPhoto(photo: Photo) {
        return photoDao.addPhoto(photo)
    }

    /*
     * Purpose: Get all the photos
     * Input:   None
     * Output:  List of Photo objects
     */
    fun getPhotos(): List<Photo> {
        return photoDao.getPhotos()
    }

    /*
     * Purpose: Get all the photos from an album
     * Input:   album - List of strings representing uris of Photos to retrieve
     * Output:  List of Photo objects
     */
    fun getPhotos(album: MutableList<String>): List<Photo> {
        return photoDao.getPhotos(album)
    }

    /*
     * Purpose: Delete a photo from the database
     * Input:   uriString   - String representing uri of photo to delete
     * Output:  None
     */
    fun deletePhoto(uriString: String) {
        photoDao.deletePhoto(uriString)
    }
}
