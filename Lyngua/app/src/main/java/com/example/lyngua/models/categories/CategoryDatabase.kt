package com.example.lyngua.models.categories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*

The database was set up through the tutorial found here:
https://www.youtube.com/watch?v=3USvr1Lz8g8

*/

@Database(entities = [Category::class], version = 1, exportSchema = false )
abstract class CategoryDatabase: RoomDatabase() {

    abstract fun userDao(): CategoryDao

    companion object{
        @Volatile
        private var INSTANCE: CategoryDatabase? = null

        fun getDatabase(context: Context): CategoryDatabase {
            val instanceCopy = INSTANCE
            if(instanceCopy != null){
                return instanceCopy
            }

            synchronized(CategoryDatabase::class){
                val instance=Room.databaseBuilder(
                    context.applicationContext,
                    CategoryDatabase::class.java,
                    "category_database").build()

                INSTANCE = instance
                return instance
            }
        }
    }


}