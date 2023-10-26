package com.example.waystoryapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.waystoryapp.data.local.dao.StoryDao
import com.example.waystoryapp.data.local.entity.StoryEntity

@Database(
    entities =[StoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class Database: RoomDatabase() {

    abstract fun getStoryDao(): StoryDao

}