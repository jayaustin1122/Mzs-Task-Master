package com.example.mzstaskmasterfinal.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mzstaskmasterfinal.R
import com.example.mzstaskmasterfinal.adapter.TaskAdapter
import com.example.mzstaskmasterfinal.databinding.AddDialogBinding
import com.example.mzstaskmasterfinal.databinding.FragmentHomeBinding
import com.example.mzstaskmasterfinal.databinding.UpdateDialogBinding
import com.example.mzstaskmasterfinal.db.Task
import com.example.mzstaskmasterfinal.db.TaskDatabase
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar


class HomeFragment : Fragment() {
    private lateinit var adapter : TaskAdapter
    private lateinit var taskDatabase: TaskDatabase
    private lateinit var binding : FragmentHomeBinding
    private lateinit var myCalendar : Calendar
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    var finalDate = 0L
    var finalTime =0L
    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init
        taskDatabase = TaskDatabase.invoke(this@HomeFragment.requireContext())
        viewAllTasks()

        binding.btn.setOnClickListener{
            replaceFragment(AddTaskFragment())
        }

    }

    private fun viewAllTasks() {
        var task : MutableList<Task>
        GlobalScope.launch(Dispatchers.IO) {
            task = taskDatabase.getTasks().getAllTasks()

            withContext(Dispatchers.Main){
                adapter = TaskAdapter(task)
                binding.todoRv.adapter = adapter
                binding.todoRv.layoutManager = LinearLayoutManager(this@HomeFragment.requireContext())

                adapter.onItemDelete = { item: Task, position: Int ->
                    try {
                        deleteTask(item)
                        adapter.task.removeAt(position)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this@HomeFragment.requireContext(), "Task deleted", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@HomeFragment.requireContext(), "Error deleting task: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                adapter.onItemUpdate = { item: Task, position: Int ->
                    try {
                        updateDialog(item.id)
                        adapter.notifyDataSetChanged()

                    } catch (e: Exception) {
                        Toast.makeText(this@HomeFragment.requireContext(), "Error updating task: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updateDialog(id: Int) {
        val dialog = Dialog(this@HomeFragment.requireContext())
        val binding: UpdateDialogBinding = UpdateDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        // Fetch the task based on the provided taskId
        val taskToUpdate = adapter.task.firstOrNull { it.id == id }

        // Populate initial title and description values into the EditText fields
        binding.editTitleInpLay.editText?.setText(taskToUpdate?.title)
        binding.editTaskInpLay.editText?.setText(taskToUpdate?.desc)

        dialog.show()

        binding.editBtn.setOnClickListener {
            val updatedTitle = binding.editTitleInpLay.editText?.text.toString()
            val updatedDesc = binding.editTaskInpLay.editText?.text.toString()

            // Update the task in the database
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    taskDatabase.getTasks().updateTask(updatedTitle, id)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@HomeFragment.requireContext(), "Successfully updated", Toast.LENGTH_SHORT).show()
                        viewAllTasks()
                    }

                    dialog.dismiss()
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@HomeFragment.requireContext(), "Error updating task: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun deleteTask(item:Task){
        GlobalScope.launch (Dispatchers.IO){
            taskDatabase.getTasks().deleteTask(item.id)
            viewAllTasks()
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val transaction : FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container,fragment)
        transaction.commit()
    }

}