package pl.cobaltan.getshitdone.Database

import android.content.Context

object DatabaseProvider {

    private var appDatabase : AppDatabase? = null

    fun getInstance(context: Context) : AppDatabase {
        if (appDatabase == null) {
            appDatabase = AppDatabase.getInstance(context)
        }
        return appDatabase!!
    }

    fun getInstance() : AppDatabase {
        if (appDatabase == null) throw UninitializedPropertyAccessException("AppDatabase uninitialized, call getInstance(Context) first")
        return appDatabase!!
    }
}