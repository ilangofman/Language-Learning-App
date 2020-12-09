package com.example.lyngua.controllers

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.lyngua.models.ResultLogs.ResultLogs
import com.example.lyngua.models.ResultLogs.ResultLogsDatabase
import com.example.lyngua.models.ResultLogs.ResultLogsRepository
import com.example.lyngua.models.categories.*
import com.example.lyngua.models.goals.Goal
import com.example.lyngua.models.words.Results
import com.example.lyngua.models.words.Word
import java.util.*
import kotlin.concurrent.thread

class CategoryController(context: Context){

    val readAllData: LiveData<List<Category>>
    private val repository: CategoryRepository
    private val resultsRepository: ResultLogsRepository

    init{
        val categoryDao = CategoryDatabase.getDatabase(context).categoryDao()
        repository = CategoryRepository(categoryDao)
        readAllData = repository.readAllData

        val resultsDao = ResultLogsDatabase.getDatabase(context).resultLogsDao()
        resultsRepository = ResultLogsRepository(resultsDao)
    }


    fun addCategory(catName: String): Boolean {

        val categoryAPI = CategoryAPI
        val goal: Goal = Goal(Calendar.getInstance(), 0,0, 0,0,0,0.0,0.0)

        val category = Category(0, catName,  6, emptyList(), 1, goal)
        thread {
            val id_added = repository.addCategory(category)
            Log.d("Words", "The id is ${id_added}")
            categoryAPI.getWordsForCategory(id_added, catName, repository)

        }
        return true
    }

    fun updateCategory(catId: Int, catName: String, catWords: Int, catWordsList: List<Word>, catSessionNumber: Int, catGoal: Goal): Boolean {
        val category = Category(catId, catName,  catWords, catWordsList, catSessionNumber, catGoal)

        thread{
            repository.updateCategory(category)
        }
        return true
    }

    fun updateCategoryGoal(catId: Int, catGoal: Goal): Boolean{
        thread {
            repository.updateCategoryGoal(catId, catGoal)
        }
        return true
    }

    fun deleteCategory(category: Category): Boolean {
        thread{
            repository.deleteCategory(category)
        }
        return true
    }

    fun getRecentCategory(): Category? {
        var category: Category? = null

        thread {
            category = repository.getRecentCategory()
        }.join()

        return category
    }

    fun getAllCategories(){

    }


    fun logResults(result: Results){
        val resultLogs: ResultLogs = ResultLogs(0, result.catId, result.catName, result.epochTimestamp, result.numCorrect, result.numQuestions)
        thread{
            resultsRepository.addResult(resultLogs)
        }
    }

    fun getResults(timeFrame: Long): List<ResultLogs>?{
        var results: List<ResultLogs>? = null

        thread{
            results = resultsRepository.readLogs(timeFrame)
        }.join()

        return results
    }

}