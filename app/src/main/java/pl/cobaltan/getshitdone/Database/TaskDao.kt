package pl.cobaltan.getshitdone.Database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import pl.cobaltan.getshitdone.MainActivity.Task
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getTasks() : LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE date = :date")
    fun getTasks(date: Date) : LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(vararg task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)
}