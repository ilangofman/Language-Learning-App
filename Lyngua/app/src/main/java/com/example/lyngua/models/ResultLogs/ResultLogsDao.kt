package com.example.lyngua.models.ResultLogs

import com.example.lyngua.models.categories.WordsTypeConverter



import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lyngua.models.words.Results
import com.example.lyngua.models.ResultLogs.ResultsTypeConverter


/*
* This is the interface for the category database operations
* Here all the different queries are listed.
* */
@Dao
@TypeConverters(ResultsTypeConverter::class)
interface ResultLogsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addResult(results: ResultLogs): Long

    @Query("SELECT * FROM result_logs_database ORDER BY id DESC")
    fun readAllData(): LiveData<List<ResultLogs>>

    //read data from a certain time

}
