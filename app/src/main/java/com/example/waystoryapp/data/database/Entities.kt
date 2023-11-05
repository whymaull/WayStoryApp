package com.example.waystoryapp.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Story")
class Entities (

    @field:ColumnInfo(name ="name")
    val name: String? = null,

    @field:ColumnInfo(name="photoUrl")
    val photoUrl: String? = null,

    @field:ColumnInfo(name="createdAt")
    val createdAt: String? = null,

    @field:ColumnInfo(name="sender")
    val sender: String? = null,

    @field:ColumnInfo(name="description")
    val description: String? = null,

    @field:ColumnInfo(name="lon")
    val lon: Double? = null,

    @field:ColumnInfo(name="idStory")
    @field:PrimaryKey
    val idStory: String,

    @field:ColumnInfo(name="lat")
    val lat: Double? = null

) : Parcelable