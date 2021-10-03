package com.example.data

import android.graphics.BitmapFactory
import com.example.myapplication.domain.BoardList
import com.example.myapplication.domain.BoardMapper
import com.example.myapplication.domain.BoardModel

class BoardMapper_Imp: BoardMapper {
    override fun BoardEntityToBoardModel(boardEntityList: List<BoardModel>): List<BoardList> {
        val list = mutableListOf<BoardList>()
        list.clear()
        boardEntityList.forEach { item->
            list.add(
                BoardList(
                    item.id,
                    item.type,
                    item.title,
                    BitmapFactory.decodeByteArray(item.image,0,item.image!!.size)
                )
            )
        }
        return list
    }
}