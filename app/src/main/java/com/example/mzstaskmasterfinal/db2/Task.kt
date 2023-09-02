package com.example.mzstaskmasterfinal.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    var title: String? = null,
    var desc: String? = null,
    var date: Long = 0L,
    var time: Long = 0L
){

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}