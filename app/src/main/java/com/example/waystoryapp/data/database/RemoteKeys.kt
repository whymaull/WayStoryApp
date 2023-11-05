package com.example.waystoryapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "remoteKeys")
data class RemoteKeys (
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)