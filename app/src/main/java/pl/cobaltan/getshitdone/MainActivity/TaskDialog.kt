package pl.cobaltan.getshitdone.MainActivity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import kotlinx.android.synthetic.main.dialog_task.*
import pl.cobaltan.getshitdone.Database.DatabaseProvider
import pl.cobaltan.getshitdone.R
import java.text.SimpleDateFormat
import java.util.*

class TaskDialog(private val activity: Context, private val task : Task? = null) : Dialog(activity) {

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_task)
        window.setBackgroundDrawableResource(android.R.color.transparent)

        val selectedDateCalendar = Calendar.getInstance()
        selectedDateCalendar.deleteTimeInfo()

        if(task != null) {
            addTaskButton.visibility = View.GONE
            editLayout.visibility = View.VISIBLE
            nameText.setText(task.name)
            selectedDateCalendar.time = task.date
            datePicker.updateDate(selectedDateCalendar[Calendar.YEAR], selectedDateCalendar[Calendar.MONTH], selectedDateCalendar[Calendar.DAY_OF_MONTH])
        }



        val formatter = SimpleDateFormat("EE, dd.MM")
        selectedDateText.text = formatter.format(selectedDateCalendar.time)

        datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            selectedDateCalendar.set(year, monthOfYear, dayOfMonth)
            selectedDateText.text = formatter.format(selectedDateCalendar.time)
        }

        addTaskButton.setOnClickListener { _ ->
            val task = Task(name = nameText.text.toString(), date = selectedDateCalendar.time)
            DatabaseProvider.getInstance(activity).getTaskDao().addTask(task)
            this.cancel()
        }

        updateTaskButton.setOnClickListener { _ ->
            task!!.name = nameText.text.toString()
            task.date = selectedDateCalendar.time
            DatabaseProvider.getInstance(activity).getTaskDao().updateTask(task)
            this.cancel()
        }

        deleteTaskButton.setOnClickListener { _ ->
            DatabaseProvider.getInstance(activity).getTaskDao().deleteTask(task!!)
            this.cancel()
        }

    }
}