package com.example.lyngua.models.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.TypeConverters
import com.example.lyngua.models.categories.Category
import com.example.lyngua.models.categories.CategoryDao
import com.example.lyngua.models.words.Word

class CategoryRepository(private val categoryDao: CategoryDao) {
    val readAllData: LiveData<List<Category>> = categoryDao.readAllData()

    // Purpose: add the category to the database
    // Input: A category object to add
    // Output: Long, the id of the category added
    fun addCategory(category: Category): Long {
        return categoryDao.addCategory(category)
    }

    // Purpose: Update the category in the database
    // Input: A category object to update
    // Output: None
    fun updateCategory(category: Category){
        categoryDao.updateCategory(category)
    }

    // Purpose: Update the category with the new words list in the database
    // Input: the id of the category to update, the list of word objects to update with
    // Output: None
    fun updateCategoryWords(catId: Int, wordsList: List<Word>){
        Log.d("Words", "UPDATING words # ${wordsList.size}")
        val row = categoryDao.updateCategoryWords(catId, wordsList)
        Log.d("Words Api", "Row: $row")
    }

    // Purpose: Delete a category from the database
    // Input: A category object to delete
    // Output: None
    fun deleteCategory(category: Category){
        categoryDao.deleteCategory(category)
    }
}