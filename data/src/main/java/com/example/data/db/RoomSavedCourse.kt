package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.entities.savedCourse

@Database(entities = arrayOf(savedCourse::class),version = 1)
abstract class RoomSavedCourse: RoomDatabase() {
    abstract fun savedCoursesDao(): savedCOurseDao
}