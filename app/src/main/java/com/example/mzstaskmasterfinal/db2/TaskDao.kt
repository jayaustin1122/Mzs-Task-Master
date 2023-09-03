package com.example.mzstaskmasterfinal.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
//Queries in Database
@Dao
interface TaskDao {
    @Insert
    fun addTask(task: Task)

    @Query("UPDATE Task SET title = :title, `desc` = :desc, isDone = :isDone WHERE id = :id")
    fun updateTask(title: String, desc: String, isDone: Boolean, id: Int)

    @Query("Delete From Task where id =:id")
    fun deleteTask(id: Int)

    @Query("SELECT * FROM Task")
    fun getAllTasks(): MutableList<Task>

    @Query("UPDATE Task SET isDone = :isDone WHERE id = :taskId")
    fun updateIsDone(taskId: Int, isDone: Boolean)
}