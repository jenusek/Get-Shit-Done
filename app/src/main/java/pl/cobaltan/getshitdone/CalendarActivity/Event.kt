package pl.cobaltan.getshitdone.CalendarActivity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "events")
data class Event(
        @PrimaryKey(autoGenerate = true) val id : Long? = null,

        @ColumnInfo(name = "title") var title : String,
        @ColumnInfo(name = "location") var location : String,
        @ColumnInfo(name = "person") var person : String,
        @ColumnInfo(name = "color") var color : Int,

        @ColumnInfo(name = "start_time") var startTime : Calendar,
        @ColumnInfo(name = "end_time") var endTime : Calendar,
        @ColumnInfo(name = "duration") var duration: Int,

        @ColumnInfo(name = "future_event_id") var futureEventId : Long? = null,
        @ColumnInfo(name = "repeat_option") var repeatEveryDays: Int
)