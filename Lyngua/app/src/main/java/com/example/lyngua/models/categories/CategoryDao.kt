package com.example.lyngua.models.categories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lyngua.models.categories.Category

@Dao
interface CategoryDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCategory(category: Category)

    @Query("SELECT * FROM category_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Category>>
}