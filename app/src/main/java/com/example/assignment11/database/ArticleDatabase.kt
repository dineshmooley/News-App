package com.example.assignment11.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DatabaseArticles::class],
    version = 1
)
abstract class ArticleDatabase : RoomDatabase() {
    abstract val getArticleDao: ArticleDao
}

private lateinit var INSTANCE: ArticleDatabase

fun getDatabase(context: Context): ArticleDatabase {
    synchronized(ArticleDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "games"
            ).build()
        }
    }

    return INSTANCE
}
