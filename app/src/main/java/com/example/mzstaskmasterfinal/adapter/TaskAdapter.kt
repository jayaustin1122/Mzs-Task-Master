package com.example.mzstaskmasterfinal.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.mzstaskmasterfinal.databinding.ItemTodoBinding
import com.example.mzstaskmasterfinal.db.Task
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date

class TaskAdapter(var task: MutableList<Task>):RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    var onItemDelete : ((Task,Int) -> Unit)? = null
    var onItemUpdate : ((Task,Int) -> Unit)? = null
    inner class TaskViewHolder(var bindin : ItemTodoBinding): RecyclerView.ViewHolder(bindin.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTodoBinding.inflate(layoutInflater,parent,false)
        return TaskViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        val currentTask = task[position]
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val timeFormat = SimpleDateFormat("HH:mm:ss")

        holder.bindin.apply {
            txtShowTitle.text = task[position].title
            txtShowTask.text = task[position].desc
            // Format the date and time
            val dateMillis = currentTask.date
            val timeMillis = currentTask.time

            val formattedDate = dateFormat.format(Date(dateMillis))
            val formattedTime = timeFormat.format(Date(timeMillis))

            txtShowDate.text = formattedDate
            txtShowTime.text = formattedTime
            btnDelete.setOnClickListener {
                onItemDelete?.invoke(task[position],position)
            }
             btnEdit.setOnClickListener {
                onItemUpdate?.invoke(task[position],position)
            }
        }
    }

    override fun getItemCount(): Int {
        return task.size
    }

}