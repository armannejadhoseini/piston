package com.example.myapplication.domain

import com.example.myapplication.domain.model.QuizModel

interface QuizTestMapper {
    fun AllTestEntityToTestModel(tempList: List<AllTestModel>): List<QuizModel>
}