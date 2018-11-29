package pl.cobaltan.getshitdone.Database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import pl.cobaltan.getshitdone.CalendarActivity.Event
import pl.cobaltan.getshitdone.MainActivity.Task
import java.util.*

@Dao
interface EventDao {

    @Query("SELECT * FROM events")
    fun getEvents() : LiveData<List<Event>>

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEvent(id : Long) : Event

    @Query("SELECT * FROM events WHERE future_event_id = :futureId")
    fun getEventByFutureId(futureId: Long) : Event?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEvent(vararg events: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEvents(events: Collection<Event>) : List<Long>

    @Update
    fun updateEvent(event: Event)

    @Delete
    fun deleteEvent(event: Event)

    @Query("DELETE FROM events")
    fun deleteAllEvents()
}