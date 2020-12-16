package com.app.lyngua.models.categories

import android.util.Log
import androidx.lifecycle.LiveData
import com.app.lyngua.models.goals.Goal
import com.app.lyngua.models.words.Word

class CategoryRepository(private val categoryDao: CategoryDao) {
    val readAllData: LiveData<List<Category>> = categoryDao.readAllData()

    // Purpose: add the category to the database
    // Input: A category object to add
    // Output: Long, the id of the category added
    fun addCategory(category: Category): Long {
        return categoryDao.addCategory(category)
    }

    // Purpose: get the most recent category added to the database
    // Input: None
    // Output: Category object
    fun getRecentCategory(): Category {
        return categoryDao.getRecentCategory()
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

    fun updateCategoryGoal(catId: Int, goal: Goal){
        val row = categoryDao.updateCategoryGoal(catId, goal)
    }

    // Purpose: Delete a category from the database
    // Input: A category object to delete
    // Output: None
    fun deleteCategory(category: Category){
        categoryDao.deleteCategory(category)
    }
}