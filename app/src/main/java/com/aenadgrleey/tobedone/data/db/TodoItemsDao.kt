package com.aenadgrleey.tobedone.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aenadgrleey.tobedone.data.datasources.TodoItemsLocalDataSource
import com.aenadgrleey.tobedone.data.models.TodoItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemsDao : TodoItemsLocalDataSource {
    @Query("SELECT * FROM todo_items WHERE completed = NOT :excludeCompleted OR (NOT :excludeCompleted AND (1 OR 0 OR null)) ORDER BY lastModified DESC")
    override fun getTodoItems(excludeCompleted: Boolean): Flow<List<TodoItemData>>

    @Query("SELECT * FROM todo_items")
    override fun getLocalTodoItems(): List<TodoItemData>

    @Query("DELETE FROM todo_items")
    override fun clearDatabase()

    @Query("SELECT COUNT(*) FROM TODO_ITEMS WHERE completed = 1")
    override fun completedItemsCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun addTodoItem(todoItem: TodoItemData)

    @Delete
    override suspend fun deleteTodoItem(todoItem: TodoItemData)
}