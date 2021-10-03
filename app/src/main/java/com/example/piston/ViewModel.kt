package com.example.piston

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.BoardMapper_Imp
import com.example.data.LectureMapper_Imp
import com.example.data.db.RoomDatabase
import com.example.myapplication.domain.BoardList
import com.example.myapplication.domain.LectureList
import kotlinx.coroutines.*

class ViewModel(application: Application) : AndroidViewModel(application) {


    private val lectureMapper = LectureMapper_Imp()
    private val boardMapper = BoardMapper_Imp()
    private val applicationContext = application
    private var practicalList = listOf<LectureList>()
    private var theoryList = listOf<LectureList>()
    private var allBoardList = listOf<BoardList>()


    fun getPracticalListFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val tempList1 =
                Database.getInstance(applicationContext).listDao().getPracticalCourseList()
            val tempList2 = Database.getInstance(applicationContext).listDao().getTestList(
                intArrayOf(56, 76, 77, 98, 92, 225, 112, 57, 211, 43, 2, 221, 73, 70, 1, 244)
            )
            practicalList = lectureMapper.PracticalListToLectureList(tempList1, tempList2)
        }
    }

    fun getTheoryListFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val tempList1 = Database.getInstance(applicationContext).listDao().getTheoryCourseList()
            val tempList2 = Database.getInstance(applicationContext).listDao().getTestList(
                intArrayOf(107, 201, 60, 198, 216, 84, 167, 245)
            )
            theoryList = lectureMapper.TheoryListToLectureList(tempList1, tempList2)
        }
    }

    fun getAllBoardsListFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val tempList = Database.getInstance(applicationContext).listDao().getAllBoradsList()
            allBoardList = boardMapper.BoardEntityToBoardModel(tempList)
        }
    }

    fun getAllBoards(): List<BoardList> {
        return allBoardList
    }

    fun getPracticalList(): List<LectureList> {
        return practicalList
    }

    fun getTheoryList(): List<LectureList> {
        return theoryList
    }

    object Database {

        @Volatile
        private var INSTANCE: RoomDatabase? = null

        fun getInstance(application: Application): RoomDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(application).also { INSTANCE = it }
            }

        private fun buildDatabase(application: Application) =
            Room.databaseBuilder(
                application.applicationContext,
                RoomDatabase::class.java, "lecture_content"
            )
                .createFromAsset("database/database.db")
                .fallbackToDestructiveMigrationOnDowngrade()
                .build()
    }
}