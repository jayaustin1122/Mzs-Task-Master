package com.example.mzstaskmasterfinal.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mzstaskmasterfinal.R
import com.example.mzstaskmasterfinal.databinding.ItemTodoBinding
import com.example.mzstaskmasterfinal.db.Task
import com.example.mzstaskmasterfinal.db.TaskDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
//Adapter - It is used to display the every item in a recycler view
class TaskAdapter(var task: MutableList<Task>,
                  private val taskDatabase: TaskDatabase,
                 ):RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    var onItemDelete : ((Task,Int) -> Unit)? = null
    var onItemUpdate : ((Task,Int) -> Unit)? = null
    var showNoItemsLayout = false // Flag to control the visibility of the "no more items" layout

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

            // Set the checkbox state
            checkboxIsDone.isChecked = currentTask.isDone

            // Handle checkbox state change
            checkboxIsDone.setOnCheckedChangeListener(null) // Remove previous listener

            checkboxIsDone.setOnCheckedChangeListener { _, isChecked ->
                // Update the isDone property of the current task
                currentTask.isDone = isChecked

                // Update the task in the database
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        // Use the TaskDatabase instance to execute a query to update the isDone property
                        taskDatabase.getTasks().updateIsDone(currentTask.id, isChecked)
                    } catch (e: Exception) {
                        // Handle errors if needed
                    }

                    // Notify the adapter that this item has changed to update its view
                    Handler(Looper.getMainLooper()).post {
                        notifyItemChanged(position)
                    }
                }
            }

            // Apply the custom background when the task is done
            if (currentTask.isDone) {
                bg.setBackgroundResource(R.color.colorAccent)
            } else {
                // Remove the background when the task is not done
                bg.setBackgroundResource(android.R.color.white) // Use a transparent background
            }

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