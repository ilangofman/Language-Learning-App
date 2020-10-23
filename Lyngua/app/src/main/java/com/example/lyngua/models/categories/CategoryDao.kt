package com.example.lyngua.models.categories

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lyngua.models.categories.Category
import com.example.lyngua.models.words.Word

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

    @Update
    fun updateCategory(category: Category)

    //query for updating the category with the new word list
    @Query("UPDATE category_table SET wordsList=:wordsList WHERE id LIKE :catId")
    fun updateCategoryWords(catId: Int, wordsList: List<Word>) : Int

    @Delete
    fun deleteCategory(category: Category)
}