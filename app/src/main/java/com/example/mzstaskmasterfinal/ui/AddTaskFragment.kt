package com.example.mzstaskmasterfinal.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.mzstaskmasterfinal.MainActivity
import com.example.mzstaskmasterfinal.R
import com.example.mzstaskmasterfinal.databinding.FragmentAddTaskBinding
import com.example.mzstaskmasterfinal.db.Task
import com.example.mzstaskmasterfinal.db.TaskDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar


class AddTaskFragment : Fragment() {
    private lateinit var taskDatabase: TaskDatabase
    private lateinit var binding : FragmentAddTaskBinding
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    private lateinit var myCalendar : Calendar

    var finalDate =0L
    var finalTime =0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setToolbarTitle("Add New Task")
        taskDatabase = TaskDatabase.invoke(this@AddTaskFragment.requireContext())
        binding.saveBtn.setOnClickListener {
            val title = binding.titleInpLay.editText?.text.toString()
            val desc = binding.taskInpLay.editText?.text.toString()

            if (title.isBlank() || desc.isBlank()) {
                Toast.makeText(this@AddTaskFragment.requireContext(), "Title and description cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (finalDate ==0L || finalTime == 0L) {
                Toast.makeText(this@AddTaskFragment.requireContext(), "Please select a date and time", Toast.LENGTH_SHORT).show()
            } else {
                val task = Task(title, desc,finalDate,finalTime)
                saved(task)

                Toast.makeText(this@AddTaskFragment.requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                replaceFragment(HomeFragment())
            }
        }
        binding.dateEdt.setOnClickListener {
            setDate()

        }
        binding.timeEdt.setOnClickListener {
            setTimeListener()
        }
    }
    private fun saved(task: Task) {
        GlobalScope.launch(Dispatchers.IO){
            taskDatabase.getTasks().addTask(task)
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val transaction : FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container,fragment)
        transaction.commit()
    }


    private fun setDate() {
        myCalendar = Calendar.getInstance()

        dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year:Int, month : Int, dayOfMonth:Int ->
                myCalendar.set(Calendar.YEAR,year)
                myCalendar.set(Calendar.MONTH,month)
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                updateDate()
            }

        val datePickerDialog = DatePickerDialog(
            this@AddTaskFragment.requireContext(),dateSetListener,myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
        val myformat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myformat)
        finalDate = myCalendar.time.time
        binding.dateEdt.setText(sdf.format(myCalendar.time))

        binding.timeInptLay.visibility = View.VISIBLE

    }
    private fun setTimeListener() {
        myCalendar = Calendar.getInstance()

        timeSetListener =
            TimePickerDialog.OnTimeSetListener() { _: TimePicker, hourOfDay: Int, min: Int ->
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                myCalendar.set(Calendar.MINUTE, min)
                updateTime()
            }

        val timePickerDialog = TimePickerDialog(
            this@AddTaskFragment.requireContext(), timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE), false
        )
        timePickerDialog.show()
    }

    private fun updateTime() {
        //Mon, 5 Jan 2020
        val myformat = "h:mm a"
        val sdf = SimpleDateFormat(myformat)
        finalTime = myCalendar.time.time
        binding.timeEdt.setText(sdf.format(myCalendar.time))

    }
}