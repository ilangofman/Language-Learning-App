package com.example.lyngua.controllers

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.lyngua.models.categories.Category
import com.example.lyngua.models.categories.CategoryAPI
import com.example.lyngua.models.categories.CategoryDatabase
import com.example.lyngua.models.categories.CategoryRepository
import kotlin.concurrent.thread

class CategoryController(context: Context){

    val readAllData: LiveData<List<Category>>
    private val repository: CategoryRepository

    init{
        val userDao = CategoryDatabase.getDatabase(context).userDao()
        repository = CategoryRepository(userDao)
        readAllData = repository.readAllData
    }



    fun addCategory(catName: String): Boolean {
        val category = Category(0, catName,  6)
        val cateogryAPI = CategoryAPI()
        thread{
            repository.addCategory(category)
            cateogryAPI.getWordsForCategory()
        }
        return true
    }

    fun getAllCategories(){

    }
}