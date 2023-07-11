package com.aenadgrleey.tobedone.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aenadgrleey.tobedone.data.models.TodoItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemsDao {
    @Query("SELECT * FROM todo_items WHERE completed = NOT :excludeCompleted OR (NOT :excludeCompleted AND (1 OR 0 OR null)) ORDER BY lastModified DESC")
    fun getTodoItems(excludeCompleted: Boolean): Flow<List<TodoItemData>>

    @Query("SELECT * FROM todo_items")
    fun getTodoItems(): List<TodoItemData>

    @Query("DELETE FROM todo_items")
    fun clearDatabase()

    @Query("SELECT COUNT(*) FROM TODO_ITEMS WHERE completed = 1")
    fun completedItemsCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodoItem(todoItem: TodoItemData)

    @Delete
    suspend fun deleteTodoItem(todoItem: TodoItemData)
}