package com.example.lyngua.models.categories

import android.content.Context
import androidx.room.*

/*

The database was set up through the tutorial found here:
https://www.youtube.com/watch?v=3USvr1Lz8g8

*/

@Database(
    entities = [Category::class],
    version = 3,
    exportSchema = false )

@TypeConverters(WordsTypeConverter::class)
abstract class CategoryDatabase: RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    companion object{
        @Volatile
        private var INSTANCE: CategoryDatabase? = null

        fun getDatabase(context: Context): CategoryDatabase {
            val instanceCopy = INSTANCE
            if(instanceCopy != null){
                return instanceCopy
            }

            return createDatabase(context)
        }

        fun createDatabase(context: Context): CategoryDatabase {
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