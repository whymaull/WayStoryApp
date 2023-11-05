package com.example.waystoryapp.data.tools

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.waystoryapp.data.database.EntitiesSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Settings private constructor(private val dataStore: DataStore<Preferences>) {

    private val ID = stringPreferencesKey("userId")
    private val NAME = stringPreferencesKey("name")
    private val TOKEN = stringPreferencesKey("token")
    private val LOGGEDIN = booleanPreferencesKey("status")
    private val STATUS = booleanPreferencesKey("status")

    fun getData(): Flow<EntitiesSettings> {
        return dataStore.data.map { preferences ->
            val id = preferences[ID] ?: ""
            val name = preferences[NAME] ?: ""
            val token = preferences[TOKEN] ?: ""
            val login = preferences[LOGGEDIN] ?: false
            EntitiesSettings(id,name,token,login)
        }
    }

    fun getSetting(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[ID] ?: ""
            preferences[NAME] ?: ""
            preferences[TOKEN] ?: ""
        }
    }


    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    fun getName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[NAME] ?: ""
        }
    }

    fun getId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[ID] ?: ""
        }
    }

    fun getStatus(): Flow<Boolean> {
        return dataStore.data.map {
            it[STATUS] ?: false
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    suspend fun deleteData() {
        dataStore.edit {
            it.clear()
        }
    }

    suspend fun setData(userid: String, name: String, token: String, login: Boolean) {
        dataStore.edit { preferences ->
            preferences[ID] = userid
            preferences[NAME] = name
            preferences[TOKEN] = token
            preferences[LOGGEDIN] = login
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: Settings? = null

        fun getInstance(dataStore: DataStore<Preferences>): Settings {
            return INSTANCE ?: synchronized(this) {
                val instance = Settings(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}