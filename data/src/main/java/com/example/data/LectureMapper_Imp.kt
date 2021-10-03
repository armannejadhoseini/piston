package com.example.data

import android.graphics.BitmapFactory
import com.example.myapplication.domain.*
import com.example.myapplication.domain.LectureList

class LectureMapper_Imp : LectureMapper {
    override fun PracticalListToLectureList(practicalList: List<CourseListModel>, testModelList: List<TestModel>): MutableList<LectureList> {
        val LectureList = mutableListOf<LectureList>()
        LectureList.clear()
        practicalList.forEachIndexed  { index, item ->
            LectureList.add(
                LectureList(
                    item.id.toInt(),
                    BitmapFactory.decodeByteArray(item.image,0,item.image!!.size),
                    item.title,
                    item.page1,
                    String(item.page2),
                    item.page3,
                    item.page4,
                    item.page5,
                    item.page6,
                    item.id.toInt(),
                    testModelList[index].title,
                    testModelList[index].answer1,
                    testModelList[index].answer2,
                    testModelList[index].answer3,
                    testModelList[index].answer4,
                    testModelList[index].true_answer,
                    item.id.toInt(),
                    testModelList[index + 1].title,
                    testModelList[index + 1].answer1,
                    testModelList[index + 1].answer2,
                    testModelList[index + 1].answer3,
                    testModelList[index + 1].answer4,
                    testModelList[index + 1].true_answer
                )
            )
        }
        return LectureList
    }

    override fun TheoryListToLectureList(theoryList: List<TheoryList>, testModelList: List<TestModel>): MutableList<LectureList> {
        val LectureList = mutableListOf<LectureList>()
        LectureList.clear()
        theoryList.forEachIndexed  { index, item ->
            LectureList.add(
                LectureList(
                    item.id.toInt(),
                    BitmapFactory.decodeByteArray(item.image,0,item.image!!.size),
                    item.title,
                    item.page1,
                    item.page2,
                    item.page3,
                    item.page4,
                    item.page5,
                    item.page6,
                    item.id.toInt(),
                    testModelList[index].title,
                    testModelList[index].answer1,
                    testModelList[index].answer2,
                    testModelList[index].answer3,
                    testModelList[index].answer4,
                    testModelList[index].true_answer,
                    item.id.toInt(),
                    testModelList[index + 1].title,
                    testModelList[index + 1].answer1,
                    testModelList[index + 1].answer2,
                    testModelList[index + 1].answer3,
                    testModelList[index + 1].answer4,
                    testModelList[index + 1].true_answer
                )
            )
        }
        return LectureList
    }
}


