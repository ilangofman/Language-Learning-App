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
        val cateogryAPI = CategoryAPI()
        val res =  cateogryAPI.getWordsForCategory()
        if(res == null){
            val category = Category(0, catName,  6, emptyArray())
            thread{
                repository.addCategory(category)
            }
        }else{
            val category = Category(0, catName,  6, res)
            thread{
                repository.addCategory(category)
            }

        }

        return true
    }

    fun updateCategory(catId: Int, catName: String, catWords: Int): Boolean {
        val category = Category(catId, catName,  catWords)
        val cateogryAPI = CategoryAPI()
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