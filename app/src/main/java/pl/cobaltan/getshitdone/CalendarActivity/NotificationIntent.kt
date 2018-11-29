package pl.cobaltan.getshitdone.CalendarActivity

import android.app.IntentService
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import pl.cobaltan.getshitdone.R

class NotificationIntent : IntentService("EventNotificationService") {
    override fun onHandleIntent(intent: Intent?) {
        val notificationBuilder = NotificationCompat.Builder(this, "chanel_id")
                .setSmallIcon(R.drawable.ic_poop_checked)
                .setContentTitle("Powiadomienie")
                .setContentText("To jest powiadomienie z zajebistej apki!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(0, notificationBuilder.build())
        }
    }
}