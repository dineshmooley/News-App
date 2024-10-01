package com.example.assignment11.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg article: DatabaseArticles)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<DatabaseArticles>>

    @Delete
    suspend fun deleteArticle(article: DatabaseArticles)

}