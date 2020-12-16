package com.app.lyngua.models.ResultLogs

import androidx.lifecycle.LiveData

class ResultLogsRepository(private val resultLogsDao: ResultLogsDao) {
    val readAllData: LiveData<List<ResultLogs>> = resultLogsDao.readAllData()

    // Purpose: add the results to the database
    // Input: A results object to add
    // Output: Long, the id of the results added
    fun addResult(result: ResultLogs): Long {
        return resultLogsDao.addResult(result)
    }

    fun readLogs(timeFrame: Long): List<ResultLogs> {
        return resultLogsDao.readLogs(timeFrame)
    }

}