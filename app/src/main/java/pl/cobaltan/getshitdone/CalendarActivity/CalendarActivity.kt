package pl.cobaltan.getshitdone.CalendarActivity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.RectF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEvent
import kotlinx.android.synthetic.main.activity_calendar.*
import pl.cobaltan.getshitdone.Database.DatabaseProvider
import pl.cobaltan.getshitdone.R
import java.util.*
import kotlin.collections.ArrayList

class CalendarActivity : AppCompatActivity() {

    private val db = DatabaseProvider.getInstance(this)
    private val events = db.getEventDao().getEvents()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        events.observe(this, android.arch.lifecycle.Observer { list ->
            weekView.notifyDatasetChanged()
//            if (list != null){
//                for (event in list){
//                    val notifyIntent = Intent(this, NotificationReceiver::class.java)
//                    val pendingIntent = PendingIntent.getBroadcast(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, event.startTime.timeInMillis, pendingIntent)
//                }
//            }
        })


        addEventButton.setOnClickListener { EventDialog(this).show() }

        weekView.setMonthChangeListener { newYear, newMonth -> getWeekViewEvents(newYear, newMonth-1)}
        weekView.setOnEventClickListener { event, eventRect -> EventDetailsDialog(this, db.getEventDao().getEvent(event.id)).show() }
        weekView.eventLongPressListener = WeekView.EventLongPressListener { event, eventRect ->
            EventDialog(this, db.getEventDao().getEvent(event.id)).show()
        }
    }

    private fun getWeekViewEvents(year: Int, month: Int) : List<WeekViewEvent> {
        val result = ArrayList<WeekViewEvent>()
        if (events.value != null) {
            for (event in events.value!!) {
                if ((event.startTime[Calendar.YEAR] == year || event.endTime[Calendar.YEAR] == year)
                        && (event.startTime[Calendar.MONTH] == month || event.endTime[Calendar.MONTH] == month)) {
                    val weekViewEvent = WeekViewEvent(event.id ?: -1, event.title, event.startTime, event.endTime)
                    weekViewEvent.color = event.color
                    weekViewEvent.location = event.location
                    result.add(weekViewEvent)
                }
            }
        }
        return result
    }
}
