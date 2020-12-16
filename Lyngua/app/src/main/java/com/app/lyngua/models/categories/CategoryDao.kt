package com.app.lyngua.models.categories

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.lyngua.models.goals.Goal
import com.app.lyngua.models.words.Word

/*
* This is the interface for the category database operations
* Here all the different queries are listed.
* */
@Dao
@TypeConverters(WordsTypeConverter::class)
interface CategoryDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCategory(category: Category): Long

    @Query("SELECT * FROM category_table ORDER BY id DESC")
    fun readAllData(): LiveData<List<Category>>

    @Query("SELECT * FROM category_table ORDER BY id DESC LIMIT 1")
    fun getRecentCategory(): Category

    @Update
    fun updateCategory(category: Category)

    //query for updating the category with the new word list
    @Query("UPDATE category_table SET wordsList=:wordsList WHERE id LIKE :catId")
    fun updateCategoryWords(catId: Int, wordsList: List<Word>) : Int

    @Query("UPDATE category_table SET goal=:goal WHERE id LIKE :catId")
    fun updateCategoryGoal(catId: Int, goal: Goal) : Int

    @Delete
    fun deleteCategory(category: Category)
}