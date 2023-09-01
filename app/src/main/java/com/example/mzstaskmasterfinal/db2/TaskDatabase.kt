package com.example.mzstaskmasterfinal.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Task::class],
//    entities = [Task::class,Employee::class], sample if you want to add new table
    version = 1
)
abstract class TaskDatabase:RoomDatabase() {
    abstract fun getTasks():TaskDao

    companion object{
        @Volatile
        private var instance : TaskDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance?:buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext,
            TaskDatabase::class.java,
            "Task Master"
        ).build()
    }

}