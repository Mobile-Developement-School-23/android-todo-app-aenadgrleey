package com.aenadgrleey.tobedone.data.datasources

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aenadgrleey.tobedone.data.db.TodoItemsDao
import com.aenadgrleey.tobedone.data.models.RoomConverters
import com.aenadgrleey.tobedone.data.models.TodoItemData

class DatabaseDatasourceImpl {
    @TypeConverters(RoomConverters::class)
    @Database(
        entities = [TodoItemData::class],
        version = 1,
        exportSchema = true,
    )
    abstract class LocalDatabase : RoomDatabase() {
        abstract fun todoItemsDao(): TodoItemsDao

        companion object {
            fun getDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext, LocalDatabase::class.java, "xdatabase")
                    .build()
        }
    }
}