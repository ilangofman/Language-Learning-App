package com.app.lyngua.models.categories

import android.content.Context
import androidx.room.*

/*

This is class to set up the database for the categories.

The database was set up through the tutorial found here:
https://www.youtube.com/watch?v=3USvr1Lz8g8

*/

@Database(
    entities = [Category::class],
    version = 10,
    exportSchema = false )

@TypeConverters(WordsTypeConverter::class)
abstract class CategoryDatabase: RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    companion object{
        @Volatile
        private var INSTANCE: CategoryDatabase? = null

        // Get an instance of the database. If one already exists, return it, otherwise create it
        // Input: The current app context
        // Output: the categoryDatabase object
        fun getDatabase(context: Context): CategoryDatabase {
            val instanceCopy = INSTANCE
            if(instanceCopy != null){
                return instanceCopy
            }

            return createDatabase(context)
        }

        // Purpose: If the database hasn't been created, create it
        // Input: context, the current app context
        // Output: the categoryDatabase object
        private fun createDatabase(context: Context): CategoryDatabase {
            synchronized(CategoryDatabase::class){
                val instance=Room.databaseBuilder(
                    context.applicationContext,
                    CategoryDatabase::class.java,
                    "category_database")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }


}