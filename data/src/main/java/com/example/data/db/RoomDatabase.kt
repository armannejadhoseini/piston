package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(
    AllTestEntity::class,
    CourseListEntity::class,
    TheoryListEntity::class,
    BoardEntity::class
), version = 2)
abstract class RoomDatabase: RoomDatabase() {
    abstract fun listDao(): listDao
}
