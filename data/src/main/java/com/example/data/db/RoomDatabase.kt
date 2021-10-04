package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.entities.ExamEntity
import com.example.data.entities.ExamPercentEntity
import com.example.data.entities.QuizEntity
import com.example.data.entities.QuizPercentEntity

@Database(entities = arrayOf(
//    AllTestEntity::class,
    CourseListEntity::class,
    TheoryListEntity::class,
    BoardEntity::class,
    ExamPercentEntity::class,
    QuizPercentEntity::class,
    QuizEntity::class,
    ExamEntity::class,
), version = 2)
abstract class RoomDatabase: RoomDatabase() {
    abstract fun listDao(): listDao
}
