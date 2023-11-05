package com.example.waystoryapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.waystoryapp.data.database.Entities

@Dao
interface StoryDao {

    @Query("SELECT * FROM Story")
    fun getListStory() : LiveData<List<Entities>>

    @Query("SELECT * FROM Story")
    fun getListStoryPaging() : PagingSource<Int, Entities>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStory(list: List<Entities>)

    @Query("DELETE FROM Story")
    suspend fun deleteAllStory()

}