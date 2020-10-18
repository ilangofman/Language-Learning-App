package com.example.lyngua.models.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.TypeConverters
import com.example.lyngua.models.categories.Category
import com.example.lyngua.models.categories.CategoryDao

class CategoryRepository(private val categoryDao: CategoryDao) {
    val readAllData: LiveData<List<Category>> = categoryDao.readAllData()

//    suspend fun addUser(user: User){
     fun addCategory(category: Category): Long {
        return categoryDao.addCategory(category)
    }

    fun updateCategory(category: Category){
        categoryDao.updateCategory(category)
    }

    fun updateCategoryWords(catId: Int, wordsList: List<Word>){
        Log.d("Words", "UPDATING words # ${wordsList.size}")
        val row = categoryDao.updateCategoryWords(catId, wordsList)
        Log.d("Words Api", "Row: $row")
    }

    fun deleteCategory(category: Category){
        categoryDao.deleteCategory(category)
    }
}