package com.example.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.domain.AllTestModel
fun List<QuizEntity>.toAllTestList(): ArrayList<AllTestModel> {
    var list = ArrayList<AllTestModel>()
    this.forEach {
        list.add(it.toAllTestModel())
    }
    return list
}
fun QuizEntity.toAllTestModel(): AllTestModel {
    return AllTestModel(
        this.id,
        this.quiz_number?:0,
        this.title,
        this.answer1,
        this.answer2,
        this.answer3,
        this.answer4,
        this.true_answer,
        this.image
    )
}
@Entity(tableName = "quiz_table")
data class QuizEntity(
    @PrimaryKey
    val id: Long,
    val quiz_number : Long?,
    val title: String,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,
    val true_answer: Long,
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray?
)

