package com.example.lyngua.controllers

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.lyngua.models.categories.*
import com.example.lyngua.models.goals.Goal
import com.example.lyngua.models.words.Word
import java.util.*
import kotlin.concurrent.thread

class CategoryController(context: Context){

    val readAllData: LiveData<List<Category>>
    private val repository: CategoryRepository

    init{
        val categoryDao = CategoryDatabase.getDatabase(context).categoryDao()
        repository = CategoryRepository(categoryDao)
        readAllData = repository.readAllData
    }


    fun addCategory(catName: String): Boolean {
        val cateogryAPI = CategoryAPI()
        val goal: Goal = Goal(0, Calendar.getInstance(), 0,0, -1, -1)
        val category = Category(0, catName,  6, emptyList(), 1, goal)
        thread {
            val id_added = repository.addCategory(category)
            Log.d("Words", "The id is ${id_added}")
            cateogryAPI.getWordsForCategory(id_added, catName, repository)

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

    fun deleteCategory(category: Category): Boolean {
        thread{
            repository.deleteCategory(category)
        }
        return true
    }

    fun getAllCategories(){

    }
}