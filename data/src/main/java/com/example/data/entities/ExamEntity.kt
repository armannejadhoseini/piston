package com.example.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.domain.AllTestModel


fun List<ExamEntity>.toAllTestList(): ArrayList<AllTestModel> {
    var list = ArrayList<AllTestModel>()
    this.forEach {
        list.add(it.toAllTestModel())
    }
    return list
}
fun ExamEntity.toAllTestModel(): AllTestModel {
    return AllTestModel(
        this.id,
        this.exam_number,
        this.title,
        this.answer1,
        this.answer2,
        this.answer3,
        this.answer4,
        this.true_answer,
        this.image
    )
}
@Entity(tableName = "exam_tabel")
data class ExamEntity(
    @PrimaryKey
    val id: Long,
    val exam_number: Long,
    val title: String,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,
    val true_answer: Long,
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray?
)