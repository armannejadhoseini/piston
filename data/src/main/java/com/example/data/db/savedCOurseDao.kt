package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.data.entities.savedCourse

@Dao
interface savedCOurseDao {

    @Query("SELECT * FROM saved_course")
    fun isSaved(): List<savedCourse>

    @Insert
    fun savePage(savedCourse: savedCourse)

    @Delete
    fun deletePage(savedCourse: savedCourse)
}