package com.example.mzstaskmasterfinal.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert
    fun addTask(task: Task)

    @Query("Update Task set title = :title where id =:id")
    fun updateTask(title:String,id:Int)

    @Query("Delete From Task where id =:id")
    fun deleteTask(id: Int)

    @Query("SELECT * FROM Task")
    fun getAllTasks(): MutableList<Task>
}