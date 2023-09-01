package com.example.mzstaskmasterfinal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mzstaskmasterfinal.databinding.ItemTodoBinding
import com.example.mzstaskmasterfinal.db.Task

class TaskAdapter(var task: MutableList<Task>):RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    var onItemDelete : ((Task,Int) -> Unit)? = null
    var onItemUpdate : ((Task,Int) -> Unit)? = null
    inner class TaskViewHolder(var bindin : ItemTodoBinding): RecyclerView.ViewHolder(bindin.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTodoBinding.inflate(layoutInflater,parent,false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        holder.bindin.apply {
            txtShowTitle.text = task[position].title
            txtShowTask.text = task[position].desc

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