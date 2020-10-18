package com.example.lyngua.models.categories

import androidx.lifecycle.LiveData
import com.example.lyngua.models.categories.Category
import com.example.lyngua.models.categories.CategoryDao

class CategoryRepository(private val categoryDao: CategoryDao) {
    val readAllData: LiveData<List<Category>> = categoryDao.readAllData()

//    suspend fun addUser(user: User){
     fun addCategory(category: Category){
        categoryDao.addCategory(category)
    }

    fun updateCategory(category: Category){
        categoryDao.updateCategory(category)
    }

    fun deleteCategory(category: Category){
        categoryDao.deleteCategory(category)
    }
}