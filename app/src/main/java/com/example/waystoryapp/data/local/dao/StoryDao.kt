package com.example.waystoryapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.waystoryapp.data.local.entity.StoryEntity

@Dao
interface StoryDao {
    @Query("SELECT * FROM tblStory")
    fun getAllStories(): List<StoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStories(storyList: List<StoryEntity>)

    @Query("DELETE FROM tblStory")
    suspend fun deleteAllStories()

}