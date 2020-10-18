package com.example.lyngua.controllers

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.lyngua.models.categories.*
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
        val category = Category(0, catName,  6, emptyList())
        thread {
            val id_added = repository.addCategory(category)
            Log.d("Words", "The id is ${id_added}")
            cateogryAPI.getWordsForCategory(id_added, repository)

        }
        return true
    }

    fun updateCategory(catId: Int, catName: String, catWords: Int, catWordsList: List<Word>): Boolean {
        val category = Category(catId, catName,  catWords, catWordsList)

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