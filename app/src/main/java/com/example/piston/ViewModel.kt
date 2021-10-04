package com.example.piston

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.AllTestModelMapper_Imp
import com.example.data.BoardMapper_Imp
import com.example.data.LectureMapper_Imp
import com.example.data.QuizTestModelMapper_Imp
import com.example.data.db.RoomDatabase
import com.example.data.entities.toAllTestList
import com.example.data.entities.toTestPercent
import com.example.myapplication.domain.BoardList
import com.example.myapplication.domain.LectureList
import com.example.myapplication.domain.model.QuizModel
import com.example.myapplication.domain.model.TestPercentEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val lectureMapper = LectureMapper_Imp()
    private val boardMapper = BoardMapper_Imp()
    private val applicationContext = application
    private var practicalList = listOf<LectureList>()
    private var theoryList = listOf<LectureList>()
    private var allBoardList = listOf<BoardList>()
    private val quizTestMapper_Imp = QuizTestModelMapper_Imp()
    private val allTestModelMapper_Imp = AllTestModelMapper_Imp()
    fun examList(number: Int) = flow<List<QuizModel>?> {
        var examList = Database.getInstance(getApplication()).listDao().getExamList(number.toLong())
        emit(quizTestMapper_Imp.AllTestEntityToTestModel(examList.toAllTestList()))
    }

    fun getQuizList2(number: Int): MutableLiveData<List<QuizModel>> {
        val liveData = MutableLiveData<List<QuizModel>>()
        viewModelScope.launch(Dispatchers.IO) {
            var quizList = Database.getInstance(getApplication()).listDao().getQuizList(number.toLong())
            liveData.postValue(quizTestMapper_Imp.AllTestEntityToTestModel(quizList.toAllTestList()))
        }
        return liveData
    }

    fun quizList(number: Int) = flow<List<QuizModel>?> {
        var quizList = Database.getInstance(getApplication()).listDao().getQuizList(number.toLong())
        emit(quizTestMapper_Imp.AllTestEntityToTestModel(quizList.toAllTestList()))
    }

    suspend fun getQuizList(number: Int): List<QuizModel> {
        var quizList = Database.getInstance(getApplication()).listDao().getQuizList(number.toLong())
        return quizTestMapper_Imp.AllTestEntityToTestModel(quizList.toAllTestList())
    }


    val examPercentList = flow<List<TestPercentEntity>?> {
        val list = Database.getInstance(getApplication()).listDao().getExamPercentList().toTestPercent()
        emit(list)
    }

    val quizPercentList = flow<List<TestPercentEntity>?> {
        val list = Database.getInstance(getApplication()).listDao().getQuizPercentList().toTestPercent()
        emit(list)
    }

    fun setQuizPercent(id: Int, percent: Int) = viewModelScope.launch(Dispatchers.IO) {
        Database.getInstance(getApplication()).listDao().setQuizPercent(id.toLong(), percent.toLong())
    }

    fun setExamPercent(id: Int, percent: Int) = viewModelScope.launch(Dispatchers.IO) {
        Database.getInstance(getApplication()).listDao().setExamPercent(id.toLong(), percent.toLong())
    }

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
                .fallbackToDestructiveMigration()
                .build()
    }
}