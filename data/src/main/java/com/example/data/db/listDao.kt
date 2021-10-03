package com.example.data.db

import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.domain.*

@Dao
interface listDao {

    @Query("SELECT * FROM practical_course_table")
    fun getPracticalCourseList(): List<CourseListModel>

    @Query("SELECT * FROM theory_course_table")
    fun getTheoryCourseList(): List<TheoryList>

    @Query("SELECT * FROM quiz_table WHERE id IN (:id)")
    fun getTestList(id: IntArray): List<TestModel>

    @Query("SELECT * FROM Board_table")
    fun getAllBoradsList(): List<BoardModel>


}