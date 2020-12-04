package com.example.lyngua.models.ResultLogs

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.TypeConverters
import com.example.lyngua.models.categories.Category
import com.example.lyngua.models.categories.CategoryDao
import com.example.lyngua.models.goals.Goal
import com.example.lyngua.models.words.Results
import com.example.lyngua.models.words.Word

class ResultLogsRepository(private val resultLogsDao: ResultLogsDao) {
    val readAllData: LiveData<List<ResultLogs>> = resultLogsDao.readAllData()

    // Purpose: add the results to the database
    // Input: A results object to add
    // Output: Long, the id of the results added
    fun addResult(result: ResultLogs): Long {
        return resultLogsDao.addResult(result)
    }


}