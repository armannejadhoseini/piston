package com.example.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.domain.model.TestPercentEntity

fun ExamPercentEntity.toTestPercent(): TestPercentEntity {
    return TestPercentEntity(this.id.toInt(),this.name.toString(),this.percent?.toInt()?:0)
}
fun List<ExamPercentEntity>.toTestPercent(): List<TestPercentEntity> {
    var list = ArrayList<TestPercentEntity>()
    this.forEach {
        list.add(it.toTestPercent())
    }
    return list.toList()
}

@Entity(tableName = "exam_percent")
data class ExamPercentEntity(
    @PrimaryKey
    var id: Long,
    var name: String?,
    var percent: Long?,
)


