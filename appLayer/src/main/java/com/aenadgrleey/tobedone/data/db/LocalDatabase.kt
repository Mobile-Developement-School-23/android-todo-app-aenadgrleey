package com.aenadgrleey.tobedone.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aenadgrleey.tobedone.data.models.RoomConverters
import com.aenadgrleey.tobedone.data.models.TodoItemData

@TypeConverters(RoomConverters::class)
@Database(
    entities = [TodoItemData::class],
    version = 1,
    exportSchema = true,
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun todoItemsDao(): TodoItemsDao

    companion object {
        @Volatile
        var DATABASE_INSTANCE: LocalDatabase? = null
        fun getDatabase(context: Context): LocalDatabase =
            synchronized(this) {
                if (DATABASE_INSTANCE == null)
                    DATABASE_INSTANCE = Room.databaseBuilder(context.applicationContext, LocalDatabase::class.java, "xdatabase")
                        .build()
                DATABASE_INSTANCE!!
            }
    }
}