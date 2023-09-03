package com.example.mzstaskmasterfinal.ui

import android.app.DatePickerDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
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

// Add Task Fragment
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

        //if the Saved Button Is Click it will saved to the database if the text fields are blank it will show a toast
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
                //here notification
                sendNotification(title)
                //navigate to home after saving a task
                replaceFragment(HomeFragment())
            }
        }
        //to open date picker
        binding.dateEdt.setOnClickListener {
            setDate()

        }
        //time Picker
        binding.timeEdt.setOnClickListener {
            setTimeListener()
        }
    }
    private fun saved(task: Task) {
        GlobalScope.launch(Dispatchers.IO){
            taskDatabase.getTasks().addTask(task)
        }
    }

    // this function to call when you want to navigate to other fragments.
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

    //this function will be called when a user saved a task and it will automatically call this function. this is standard
    private fun sendNotification(taskTitle: String) {
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for Android 8.0 and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val imp = NotificationManager.IMPORTANCE_HIGH
            val mNotificationChannel = NotificationChannel("task_id", "Task Notification", imp)
            val ic = BitmapFactory.decodeResource(resources,R.drawable.logo)
            val bigNotification : Notification.BigTextStyle = Notification.BigTextStyle()
            bigNotification.bigText("The New Task Is Succesfully Saved!")
            bigNotification.setBigContentTitle(taskTitle)

            val notificationBuilder : Notification.Builder = Notification.Builder(this@AddTaskFragment.requireContext(),"task_id")
                .setContentText(taskTitle)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(ic)
                .setStyle(bigNotification)
                .setContentText(taskTitle)

            val notificationManager : NotificationManager = this@AddTaskFragment.requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mNotificationChannel)
            notificationManager.notify(0,notificationBuilder.build())

        }
        else{

            val ic = BitmapFactory.decodeResource(resources,R.drawable.logo)
            val bigNotification : NotificationCompat.BigTextStyle = NotificationCompat.BigTextStyle()
            bigNotification.bigText("The New Task Is Succesfully Saved!")
            bigNotification.setBigContentTitle(taskTitle)
            val notificationBuilder : NotificationCompat.Builder = NotificationCompat.Builder(this@AddTaskFragment.requireContext())
                .setContentText(taskTitle)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(ic)
                .setStyle(bigNotification)
                .setContentText(taskTitle)

            val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0,notificationBuilder.build())
        }
    }

}