package com.example.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_course")
data class savedCourse(
    @PrimaryKey
    val id: Int,
    val type: Int,
    val page: Int
)
