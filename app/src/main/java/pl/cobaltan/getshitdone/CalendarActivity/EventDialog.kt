package pl.cobaltan.getshitdone.CalendarActivity

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.alamkanak.weekview.WeekView
import kotlinx.android.synthetic.main.dialog_event.*
import pl.cobaltan.getshitdone.Database.DatabaseProvider
import pl.cobaltan.getshitdone.R
import java.util.*
import kotlin.collections.ArrayList

class EventDialog(activity: Activity, private val event: Event? = null) : Dialog(activity) {

    private val eventDao = DatabaseProvider.getInstance(context).getEventDao()
    private var startTime = Calendar.getInstance()
    private var endTime = Calendar.getInstance()
    private var repeatAmount = 7

    private var futureEventsList = ArrayList<Event>()

    private val COLOR_YELLOW = Color.parseColor("#f1c40f")
    private val COLOR_GREEN = Color.parseColor("#2ecc71")
    private val COLOR_RED = Color.parseColor("#e74c3c")
    private val COLOR_BLUE = Color.parseColor("#3498db")

    override fun onCreate(savedInstanceState: Bundle?) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_event)
        window.setBackgroundDrawableResource(android.R.color.transparent)

        loadEventInfo()

        updateEventButton.setOnClickListener {
            var eventId = event!!.id
            deletePrevEventInfo(eventId!!)
            while (eventId != null) {
                val e = eventDao.getEvent(eventId)
                eventId = e.futureEventId
                eventDao.deleteEvent(e)
            }
            this.addEvents()
            this.cancel()
        }

        deleteEventButton.setOnClickListener {
            deletePrevEventInfo(event!!.id!!)
            DatabaseProvider.getInstance(context).getEventDao().deleteEvent(event)
            this.cancel()
        }

        repeatSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { repeatAmount = 7*(position+1) }
        }

        ArrayAdapter.createFromResource(context, R.array.event_dialog_repeat_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            repeatSpinner.adapter = adapter
        }

        dateTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) { }
            override fun onTabUnselected(tab: TabLayout.Tab?) { }
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab != null) {
                    if(tab.position == 0) {
                        datePicker.updateDate(startTime[Calendar.YEAR], startTime[Calendar.MONTH], startTime[Calendar.DAY_OF_MONTH])
                    }
                    else {
                        datePicker.updateDate(endTime[Calendar.YEAR], endTime[Calendar.MONTH], endTime[Calendar.DAY_OF_MONTH])
                    }
                }
            }
        })

        startTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            startTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            startTime.set(Calendar.MINUTE, minute)
            startTime.set(Calendar.SECOND, 0)
            startTime.set(Calendar.MILLISECOND, 0)

            endTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            endTime.set(Calendar.MINUTE, minute)
            endTime.set(Calendar.SECOND, 0)
            endTime.set(Calendar.MILLISECOND, 0)
        }

        datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            if (dateTabLayout.selectedTabPosition == 0)
                startTime.set(year, monthOfYear, dayOfMonth)
            else
                endTime.set(year, monthOfYear, dayOfMonth)
        }

        addEventButton.setOnClickListener {
            this.addEvents()
            this.cancel()
        }

    }

    private fun addEvents() {
        val title = titleText.text.toString()
        val location = locationText.text.toString()
        val person = personText.text.toString()
        val addMinutes = if (durationRadioGroup.checkedRadioButtonId == R.id.oneLessonRadioButton) 45 else 105
        val color = when (colorRadioGroup.checkedRadioButtonId) {
            R.id.yellowRadioButton -> COLOR_YELLOW
            R.id.greenRadioButton -> COLOR_GREEN
            R.id.redRadioButton -> COLOR_RED
            R.id.blueRadioButton -> COLOR_BLUE
            else -> Color.parseColor("#9b59b6")
        }

        val events = ArrayList<Event>()

        while (startTime <= endTime) {
            val tempEndTime = Calendar.getInstance()
            val tempStartTime = Calendar.getInstance()
            tempEndTime.time = startTime.time
            tempStartTime.time = startTime.time
            tempEndTime.add(Calendar.MINUTE, addMinutes)
            events.add(Event(title = title, startTime = tempStartTime, endTime = tempEndTime, color = color, location = location, person = person, repeatEveryDays = repeatAmount, duration = addMinutes))
            startTime.add(Calendar.DAY_OF_MONTH, repeatAmount)
        }

        val eventsId = eventDao.addEvents(events)

        events.clear()
        for (id in eventsId) {
            events.add(eventDao.getEvent(id))
        }

        for (i in 1 until events.size) {
            events[i-1].futureEventId = events[i].id
            eventDao.updateEvent(events[i-1])
        }
    }

    private fun deletePrevEventInfo(id: Long) {
        val prevEvent = eventDao.getEventByFutureId(id)
        if (prevEvent != null) {
            prevEvent.futureEventId = null
            eventDao.updateEvent(prevEvent)
        }

    }

    private fun loadEventInfo() {
        if(event != null) {
            editLayout.visibility = View.VISIBLE
            addEventButton.visibility = View.GONE

            titleText.setText(event.title)
            locationText.setText(event.location)
            personText.setText(event.person)

            when (event.color) {
                COLOR_BLUE -> colorRadioGroup.check(blueRadioButton.id)
                COLOR_RED -> colorRadioGroup.check(redRadioButton.id)
                COLOR_GREEN -> colorRadioGroup.check(greenRadioButton.id)
                COLOR_YELLOW -> colorRadioGroup.check(yellowRadioButton.id)
            }

            startTimePicker.hour = startTime.get(Calendar.HOUR_OF_DAY)
            startTimePicker.minute = startTime.get(Calendar.MINUTE)

            when (event.duration) {
                45 -> durationRadioGroup.check(oneLessonRadioButton.id)
                105 -> durationRadioGroup.check(twoLessonsRadioButton.id)
            }

            startTime = event.startTime
            endTime = event.endTime

            var futureEventId = event.futureEventId
            while (futureEventId != null) {
                val e = eventDao.getEvent(futureEventId)
                endTime = e.endTime
                futureEventsList.add(e)
                futureEventId = e.futureEventId
            }

            datePicker.updateDate(startTime[Calendar.YEAR], startTime[Calendar.MONTH], startTime[Calendar.DAY_OF_MONTH])

            repeatAmount = event.repeatEveryDays
            repeatSpinner.post { repeatSpinner.setSelection((repeatAmount/7)-1) }

        }
    }
}