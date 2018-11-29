package pl.cobaltan.getshitdone.MainActivity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import pl.cobaltan.getshitdone.CalendarActivity.CalendarActivity
import pl.cobaltan.getshitdone.Database.DatabaseProvider
import pl.cobaltan.getshitdone.R
import pl.cobaltan.getshitdone.SettingsActivity.SettingsActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        addTaskButton.setOnClickListener { _ -> TaskDialog(this).show() }

        val cal = Calendar.getInstance()

        if (intent.hasExtra("date")) {
            cal.time = Date(intent.extras.getLong("date"))
            headerText.text = getText(R.string.main_header_other_day)
        }
        else {
            cal.deleteTimeInfo()
            headerText.text = getText(R.string.main_header_today)
        }

        val date = cal.time
        val formatter = SimpleDateFormat("dd\nMMMM\nyyyy")
        currentDateText.text = formatter.format(date)

        val db = DatabaseProvider.getInstance(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TaskAdapter(this, listOf())

        db.getTaskDao().getTasks(cal.time).observe(this, android.arch.lifecycle.Observer { list ->
            recyclerView.swapAdapter(TaskAdapter(this, list!!), false)
        })

        val datePicker = DatePickerDialog(this)
        datePicker.setOnDateSetListener { _, year, month, day->

            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            calendar.deleteTimeInfo()

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("date", calendar.time.time)

            startActivity(intent)
            if (this.intent.hasExtra("date")) finish()
        }

        dateView.setOnClickListener { _ ->
            datePicker.show()
        }

        calendarButton.setOnClickListener { startActivity(Intent(this, CalendarActivity::class.java)) }

        settingsButton.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "event_notification"
            val descriptionText = "Event Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("chanel_id", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
