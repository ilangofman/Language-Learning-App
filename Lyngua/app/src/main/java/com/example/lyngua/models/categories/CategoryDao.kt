package com.example.lyngua.models.categories

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lyngua.models.categories.Category

@Dao
@TypeConverters(WordsTypeConverter::class)
interface CategoryDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCategory(category: Category): Long

    @Query("SELECT * FROM category_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Category>>

    @Update
    fun updateCategory(category: Category)

    @Query("UPDATE category_table SET wordsList=:wordsList WHERE id LIKE :catId")
    fun updateCategoryWords(catId: Int, wordsList: List<Word>) : Int

    @Delete
    fun deleteCategory(category: Category)
}