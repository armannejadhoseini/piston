package com.example.myapplication.domain

import android.graphics.Bitmap


data class LectureList(
    val id: Int,
    val image: Bitmap,
    val type: Int,
    val title: String,
    val page1: String,
    val page2: String,
    val page3: String,
    val page4: String,
    val page5: String,
    val page6: String,
    val quiz_id1: Int,
    val quiz_title1: String,
    val quiz1_answer1: String,
    val quiz1_answer2: String,
    val quiz1_answer3: String,
    val quiz1_answer4: String,
    val quiz1_true_answer: Int,
    val quiz_id2: Int,
    val quiz_title2: String,
    val quiz2_answer1: String,
    val quiz2_answer2: String,
    val quiz2_answer3: String,
    val quiz2_answer4: String,
    val quiz2_true_answer: Int,

    )
