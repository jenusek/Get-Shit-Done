package pl.cobaltan.getshitdone.Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import pl.cobaltan.getshitdone.CalendarActivity.Event
import pl.cobaltan.getshitdone.MainActivity.Task

@Database(entities = [Task::class, Event::class], version = 1)
@TypeConverters(DatabaseConverter::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        fun getInstance(context: Context) : pl.cobaltan.getshitdone.Database.AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .allowMainThreadQueries()
                    .build()
        }
    }

    abstract fun getTaskDao() : TaskDao

    abstract fun getEventDao() : EventDao
}