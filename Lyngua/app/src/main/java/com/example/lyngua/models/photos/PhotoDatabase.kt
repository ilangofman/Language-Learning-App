package com.example.lyngua.models.photos

import android.content.Context
import androidx.room.*

/*

This is class to set up the database for the categories.

The database was set up through the tutorial found here:
https://www.youtube.com/watch?v=3USvr1Lz8g8

*/

@Database(
    entities = [Photo::class],
    version = 10,
    exportSchema = false )

@TypeConverters(PhotoTypeConverter::class)
abstract class PhotoDatabase: RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    companion object{
        @Volatile
        private var INSTANCE: PhotoDatabase? = null

        /*
         * Purpose: Get an instance of the database. If one already exists, return it, otherwise create it
         * Input:   context - The current app context
         * Output:  the PhotoDatabase object
         */
        fun getDatabase(context: Context): PhotoDatabase {
            val instanceCopy = INSTANCE
            if(instanceCopy != null){
                return instanceCopy
            }

            return createDatabase(context)
        }

        /*
         * Purpose: If the database hasn't been created, create it
         * Input:   context - The current app context
         * Output:  the PhotoDatabase object
         */
        private fun createDatabase(context: Context): PhotoDatabase {
            synchronized(PhotoDatabase::class){
                val instance=Room.databaseBuilder(
                    context.applicationContext,
                    PhotoDatabase::class.java,
                    "Photo_database")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }


}