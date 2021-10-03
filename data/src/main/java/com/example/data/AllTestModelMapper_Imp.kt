package com.example.data

import com.example.myapplication.domain.AllTestMapper
import com.example.myapplication.domain.AllTestModel
import com.example.myapplication.domain.TestModel

class AllTestModelMapper_Imp: AllTestMapper {
    override fun AllTestEntityToTestModel(tempList: List<AllTestModel>): List<TestModel> {
        val list = mutableListOf<TestModel>()
        list.clear()
        tempList.forEach { item ->
            list.add(
                TestModel(
                    item.id.toInt(),
                    item.title,
                    item.answer1,
                    item.answer2,
                    item.answer3,
                    item.answer4,
                    item.true_answer.toInt()
                )
            )
        }
        return list
    }
}