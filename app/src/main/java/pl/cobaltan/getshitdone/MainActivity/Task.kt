package pl.cobaltan.getshitdone.MainActivity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "tasks")
data class Task (
        @PrimaryKey(autoGenerate = true) var id : Long? = null,
        @ColumnInfo(name = "name") var name : String,
        @ColumnInfo(name = "date") var date : Date,
        @ColumnInfo(name = "is_done") var isDone: Boolean = false
)