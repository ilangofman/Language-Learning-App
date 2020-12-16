package com.app.lyngua.models.ResultLogs


import android.content.Context
import androidx.room.*

/*

This is class to set up the database for the result logs.

The database was set up through the tutorial found here:
https://www.youtube.com/watch?v=3USvr1Lz8g8

*/

@Database(
    entities = [ResultLogs::class],
    version = 1,
    exportSchema = false )
@TypeConverters(ResultsTypeConverter::class)
abstract class ResultLogsDatabase: RoomDatabase() {

    abstract fun resultLogsDao(): ResultLogsDao

    companion object{
        @Volatile
        private var INSTANCE: ResultLogsDatabase? = null

        // Get an instance of the database. If one already exists, return it, otherwise create it
        // Input: The current app context
        // Output: the ResultLogsDatabase object
        fun getDatabase(context: Context): ResultLogsDatabase {
            val instanceCopy = INSTANCE
            if(instanceCopy != null){
                return instanceCopy
            }

            return createDatabase(context)
        }

        // Purpose: If the database hasn't been created, create it
        // Input: context, the current app context
        // Output: the ResultLogsDatabase object
        private fun createDatabase(context: Context): ResultLogsDatabase {
            synchronized(ResultLogsDatabase::class){
                val instance=Room.databaseBuilder(
                    context.applicationContext,
                    ResultLogsDatabase::class.java,
                    "result_logs_database")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }


}