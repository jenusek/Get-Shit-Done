package pl.cobaltan.getshitdone.CalendarActivity

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_event_details.*
import pl.cobaltan.getshitdone.R
import java.util.*

class EventDetailsDialog(activity: Activity, private val event: Event) : Dialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_event_details)
        window.setBackgroundDrawableResource(android.R.color.transparent)

        eventTitleTextView.text = event.title
        eventPersonTextView.text = event.person
        eventLocationTextView.text = event.location
        eventHoursTextView.text = "${event.startTime.get(Calendar.HOUR_OF_DAY)}:${event.startTime.get(Calendar.MINUTE)} - ${event.endTime.get(Calendar.HOUR_OF_DAY)}:${event.endTime.get(Calendar.MINUTE)}"
    }
}