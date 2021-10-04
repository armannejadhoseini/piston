package com.example.myapplication.domain.model

import android.graphics.Bitmap

data class QuizModel(
    val id: Int,
    val test_number: Int,
    val title: String,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,
    val true_answer: Int,
    val image: Bitmap? = null
)
