package com.aenadgrleey.todo.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aenadgrleey.todo.data.local.models.TodoItemDataDb
import kotlinx.coroutines.flow.Flow

/*
Dao to access todos stored in Room DB
 */
@Dao
internal interface TodoItemsDao {
    @Query(
        "SELECT * FROM todo_items WHERE completed = NOT :excludeCompleted " +
                "OR (NOT :excludeCompleted AND (1 OR 0 OR null)) ORDER BY lastModified DESC"
    )
    fun getTodoItems(excludeCompleted: Boolean): Flow<List<TodoItemDataDb>>

    @Query("SELECT * FROM todo_items")
    fun getTodoItems(): List<TodoItemDataDb>

    @Query("SELECT * FROM todo_items WHERE id=:id")
    fun todoItem(id: String): TodoItemDataDb?

    @Query("DELETE FROM todo_items")
    fun clearDatabase()

    @Query("SELECT COUNT(*) FROM TODO_ITEMS WHERE completed = 1")
    fun completedItemsCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodoItem(todoItem: TodoItemDataDb)

    @Delete
    suspend fun deleteTodoItem(todoItem: TodoItemDataDb)
}