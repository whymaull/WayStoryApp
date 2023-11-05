package com.example.waystoryapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.waystoryapp.data.database.dao.RemoteKeyDao
import com.example.waystoryapp.data.database.dao.StoryDao

@Database(entities = [Entities::class,RemoteKeys::class], version = 2, exportSchema = false)
abstract class StoryDB : RoomDatabase() {

    abstract fun storyDao() : StoryDao
    abstract fun remoteKeysDao(): RemoteKeyDao

    companion object{
        @Volatile
        private var INSTANCE: StoryDB? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDB {
            if(INSTANCE == null){
                synchronized(StoryDB::class.java){
                    INSTANCE = databaseBuilder(context.applicationContext,
                        StoryDB::class.java,"story_db"
                    ).fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE as StoryDB
        }
    }

}
